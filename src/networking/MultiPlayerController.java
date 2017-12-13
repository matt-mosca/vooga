package networking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import engine.play_engine.PlayController;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import networking.protocol.PlayerClient.DeleteElement;
import networking.protocol.PlayerClient.ClientMessage;
import networking.protocol.PlayerClient.CreateGameRoom;
import networking.protocol.PlayerClient.GetNumberOfLevels;
import networking.protocol.PlayerClient.JoinRoom;
import networking.protocol.PlayerClient.LoadLevel;
import networking.protocol.PlayerClient.MoveElement;
import networking.protocol.PlayerClient.PlaceElement;
import networking.protocol.PlayerServer.Game;
import networking.protocol.PlayerServer.GameRoomCreationStatus;
import networking.protocol.PlayerServer.GameRoomJoinStatus;
import networking.protocol.PlayerServer.GameRoomLaunchStatus;
import networking.protocol.PlayerServer.GameRooms;
import networking.protocol.PlayerServer.Games;
import networking.protocol.PlayerServer.LevelInitialized;
import networking.protocol.PlayerServer.NewSprite;
import networking.protocol.PlayerServer.Notification;
import networking.protocol.PlayerServer.NumberOfLevels;
import networking.protocol.PlayerServer.PlayerExited;
import networking.protocol.PlayerServer.PlayerJoined;
import networking.protocol.PlayerServer.PlayerNames;
import networking.protocol.PlayerServer.ReadyForNextLevel;
import networking.protocol.PlayerServer.ServerMessage;
import networking.protocol.PlayerServer.SpriteDeletion;
import networking.protocol.PlayerServer.SpriteUpdate;

/**
 * Gateway of multi-player player clients to server back end. Can handle
 * multiple game rooms at a time. Deserializes protocol buffers from client
 * requests (passed as raw string / bytes from server), calls the appropriate
 * engine methods to retrieve the result, packages the result into the
 * appropriate protocol buffer message, serializes and writes it back to
 * appropriate client socket
 * 
 * Class is package-private (not public) to ensure that only the networking
 * package (especially the server) can access it and clients cannot, to avoid
 * passing false clientIds (socketIds)
 * 
 * @author radithya
 *
 */
class MultiPlayerController {

	// TODO - Move to resources file
	private final String ERROR_UNAUTHORIZED = "You do not belong to any game room";
	private final String ERROR_CLIENT_ENGAGED = "You are already in another game room";
	private final String ERROR_NONEXISTENT_ROOM = "This game room does not exist";
	private final String GAME_ROOM_CREATION_ERROR_NONEXISTENT_GAME = "This game does not exist";
	private final String GAME_ROOM_JOIN_ERROR_USERNAME_TAKEN = "This username has already been taken for this game room";
	private final String LOAD_LEVEL_ERROR_NOT_READY = "Your peers are not yet ready to load this level";

	private final String ROOM_NAME_DEDUP_DELIMITER = "_";

	// Should support multiple concurrent game rooms, i.e. need multiple
	// concurrent engines
	private Map<String, PlayController> roomNamesToPlayEngines = new HashMap<>();
	private Map<String, String> roomNamesToGameNames = new HashMap<>();
	private Map<String, List<Integer>> roomMembers = new HashMap<>();
	private Map<String, Integer> roomNameCollisions = new HashMap<>();
	private Map<Integer, String> clientIdsToUserNames = new HashMap<>();
	private Map<String, Integer> waitingInRoom = new HashMap<>();

	private ObservableList<ServerMessage> messageQueue = FXCollections.observableArrayList();

	byte[] getAvailableGames(ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		Games.Builder gamesBuilder = Games.newBuilder();
		Map<String, String> availableGames = new PlayController().getAvailableGames();
		availableGames.keySet().forEach(gameName -> gamesBuilder
				.addGames(Game.newBuilder().setName(gameName).setDescription(availableGames.get(gameName)).build()));
		return serverMessageBuilder.setAvailableGames(gamesBuilder.build()).build().toByteArray();
	}

	byte[] createGameRoom(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		CreateGameRoom gameRoomCreationRequest = clientMessage.getCreateGameRoom();
		GameRoomCreationStatus.Builder gameRoomCreationStatusBuilder = GameRoomCreationStatus.newBuilder();
		// Only allow a given client process to play one game at a time
		if (clientIdsToUserNames.containsKey(clientId)) {
			return serverMessageBuilder
					.setGameRoomCreationStatus(gameRoomCreationStatusBuilder.setError(ERROR_CLIENT_ENGAGED).build())
					.build().toByteArray();
		}
		String gameName = gameRoomCreationRequest.getGameName();
		String roomName = generateUniqueRoomName(gameRoomCreationRequest.getRoomName());
		// Verify that gameName is valid
		PlayController controllerForGame = new PlayController();
		if (!controllerForGame.getAvailableGames().containsKey(gameName)) {
			return serverMessageBuilder
					.setGameRoomCreationStatus(
							gameRoomCreationStatusBuilder.setError(GAME_ROOM_CREATION_ERROR_NONEXISTENT_GAME).build())
					.build().toByteArray();
		}
		roomNamesToPlayEngines.put(roomName, controllerForGame);
		roomNamesToGameNames.put(roomName, gameName);
		roomMembers.put(roomName, new ArrayList<>());
		clearWaitingRoom(roomName);
		return serverMessageBuilder.setGameRoomCreationStatus(gameRoomCreationStatusBuilder.setRoomId(roomName).build())
				.build().toByteArray();
	}

	byte[] joinGameRoom(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		JoinRoom joinRoomRequest = clientMessage.getJoinRoom();
		GameRoomJoinStatus.Builder gameRoomJoinStatusBuilder = GameRoomJoinStatus.newBuilder();
		// Check if client is already in some other game
		if (clientIsInAGameRoom(clientId)) {
			return serverMessageBuilder
					.setGameRoomJoinStatus(
							gameRoomJoinStatusBuilder.setSuccess(false).setError(ERROR_CLIENT_ENGAGED).build())
					.build().toByteArray();
		}
		String roomName = joinRoomRequest.getRoomName();
		if (!roomMembers.containsKey(roomName)) {
			return serverMessageBuilder
					.setGameRoomJoinStatus(
							gameRoomJoinStatusBuilder.setSuccess(false).setError(ERROR_NONEXISTENT_ROOM).build())
					.build().toByteArray();
		}
		String userName = joinRoomRequest.getUserName();
		// Check if username is taken within this room
		if (userNameExistsInGameRoom(userName, roomName)) {
			return serverMessageBuilder.setGameRoomJoinStatus(
					gameRoomJoinStatusBuilder.setSuccess(false).setError(GAME_ROOM_JOIN_ERROR_USERNAME_TAKEN).build())
					.build().toByteArray();
		}
		processUserJoinRoom(clientId, roomName, userName);
		// Push message to other clients
		messageQueue
				.add(ServerMessage.newBuilder()
						.setNotification(Notification.newBuilder()
								.setPlayerJoined(PlayerJoined.newBuilder().setUserName(userName).build()).build())
						.build());
		return serverMessageBuilder.setGameRoomJoinStatus(gameRoomJoinStatusBuilder.setSuccess(true).build()).build()
				.toByteArray();
	}

	byte[] exitGameRoom(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		if (clientIsInAGameRoom(clientId)) {
			String exitingUserName = clientIdsToUserNames.get(clientId);
			processUserExitRoom(clientId);
			// Push message to other clients
			messageQueue.add(ServerMessage.newBuilder()
					.setNotification(Notification.newBuilder()
							.setPlayerExited(PlayerExited.newBuilder().setUserName(exitingUserName).build()).build())
					.build());
		}
		// just ignore if client is not in a game room
		return serverMessageBuilder.build().toByteArray();
	}

	byte[] launchGameRoom(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		GameRoomLaunchStatus.Builder gameRoomLaunchStatusBuilder = GameRoomLaunchStatus.newBuilder();
		String roomName = getGameRoomNameOfClient(clientId);
		String gameName = retrieveGameNameFromRoomName(roomName);
		try {
			LevelInitialized levelData = roomNamesToPlayEngines.get(roomName).loadOriginalGameState(gameName, 1);
			serverMessageBuilder
					.setGameRoomLaunchStatus(gameRoomLaunchStatusBuilder.setInitialState(levelData).build());
			// Push message to other clients
			messageQueue.add(ServerMessage.newBuilder()
					.setNotification(Notification.newBuilder().setLevelInitialized(levelData).build()).build());
			return serverMessageBuilder.build().toByteArray();
		} catch (IOException e) {
			return serverMessageBuilder
					.setGameRoomLaunchStatus(
							gameRoomLaunchStatusBuilder.setError(GAME_ROOM_CREATION_ERROR_NONEXISTENT_GAME).build())
					.build().toByteArray();
		}
	}

	byte[] getGameRooms(ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		return serverMessageBuilder.setGameRooms(GameRooms.newBuilder().addAllRoomNames(roomMembers.keySet()).build())
				.build().toByteArray();
	}

	byte[] getPlayerNames(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		PlayerNames.Builder playerNamesBuilder = PlayerNames.newBuilder();
		return serverMessageBuilder
				.setPlayerNames(playerNamesBuilder
						.addAllUserNames(getUserNamesInGameRoom(getGameRoomNameOfClient(clientId))).build())
				.build().toByteArray();
	}

	// TODO - Consider server push instead of client pull? Would be more complicated
	// but more accurate / realistic, and fewer packets exchanged
	byte[] handleUpdate(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		PlayController playController = getPlayEngineForClient(clientId);
		if (clientIsFirstMemberOfGameRoom(clientId)) {
			// only do actual update if primary client, simply send state for the rest
			playController.update();
		}
		return serverMessageBuilder.setUpdate(playController.getLatestUpdate()).build().toByteArray();
	}

	byte[] handlePauseGame(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		// Verify clientId, retrieve appropriate game room / controller
		PlayController playController = getPlayEngineForClient(clientId);
		playController.pause();
		return serverMessageBuilder.setUpdate(playController.packageStatusUpdate()).build().toByteArray();
	}

	byte[] handleResumeGame(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		PlayController playController = getPlayEngineForClient(clientId);
		playController.resume();
		return serverMessageBuilder.setUpdate(playController.packageStatusUpdate()).build().toByteArray();
	}

	byte[] getInventory(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		return serverMessageBuilder.setInventory(getPlayEngineForClient(clientId).packageInventory()).build()
				.toByteArray();
	}

	byte[] getTemplateProperties(int clientId, ClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) {
		return serverMessageBuilder
				.addTemplateProperties(getPlayEngineForClient(clientId)
						.packageTemplateProperties(clientMessage.getGetTemplateProperties().getElementName()))
				.build().toByteArray();
	}

	byte[] getAllTemplateProperties(int clientId, ClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) {
		return serverMessageBuilder
				.addAllTemplateProperties(getPlayEngineForClient(clientId).packageAllTemplateProperties()).build()
				.toByteArray();
	}

	byte[] getElementCosts(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		return serverMessageBuilder.addAllElementCosts(getPlayEngineForClient(clientId).packageAllElementCosts())
				.build().toByteArray();
	}

	byte[] placeElement(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder)
			throws ReflectiveOperationException {
		PlayController playController = getPlayEngineForClient(clientId);
		PlaceElement placeElementRequest = clientMessage.getPlaceElement();
		NewSprite placedElement = playController.placeElement(placeElementRequest.getElementName(),
				new Point2D(placeElementRequest.getXCoord(), placeElementRequest.getYCoord()));
		// Broadcast
		messageQueue.add(ServerMessage.newBuilder()
				.setNotification(Notification.newBuilder().setElementPlaced(placedElement).build()).build());
		return serverMessageBuilder.setElementPlaced(placedElement).build().toByteArray();
	}

	byte[] moveElement(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		PlayController playController = getPlayEngineForClient(clientId);
		MoveElement moveElementRequest = clientMessage.getMoveElement();
		SpriteUpdate updatedSprite = playController.moveElement(moveElementRequest.getElementId(),
				moveElementRequest.getNewXCoord(), moveElementRequest.getNewYCoord());
		// Broadcast
		messageQueue.add(ServerMessage.newBuilder()
				.setNotification(Notification.newBuilder().setElementMoved(updatedSprite).build()).build());
		return serverMessageBuilder.setElementMoved(updatedSprite).build().toByteArray();
	}

	byte[] deleteElement(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		PlayController playController = getPlayEngineForClient(clientId);
		DeleteElement deleteElementRequest = clientMessage.getDeleteElement();
		try {
			SpriteDeletion deletedElement = playController.deleteElement(deleteElementRequest.getElementId());
			messageQueue.add(ServerMessage.newBuilder()
					.setNotification(Notification.newBuilder().setElementDeleted(deletedElement).build()).build());
			return serverMessageBuilder.setElementDeleted(deletedElement).build().toByteArray();
		} catch (IllegalArgumentException e) {
			return serverMessageBuilder.setError(e.getMessage()).build().toByteArray();
		}
	}

	byte[] upgradeElement(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder)
			throws ReflectiveOperationException {
		PlayController playController = getPlayEngineForClient(clientId);
		playController.upgradeElement(clientMessage.getUpgradeElement().getSpriteId());
		return serverMessageBuilder.build().toByteArray();
	}

	byte[] checkReadyForNextLevel(int clientId, ClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) {
		return serverMessageBuilder
				.setReadyForNextLevel(ReadyForNextLevel.newBuilder()
						.setIsReady(joinAndCheckIfWaitingRoomIsFull(getGameRoomNameOfClient(clientId))).build())
				.build().toByteArray();
	}

	byte[] loadLevel(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		LevelInitialized.Builder levelInitializationBuilder = LevelInitialized.newBuilder();
		LoadLevel loadLevelRequest = clientMessage.getLoadLevel();
		String roomName = getGameRoomNameOfClient(clientId);
		int levelToLoad = loadLevelRequest.getLevel();
		if (!checkIfWaitingRoomIsFull(roomName)) {
			// not ready to load
			return serverMessageBuilder
					.setLevelInitialized(levelInitializationBuilder.setError(LOAD_LEVEL_ERROR_NOT_READY).build())
					.build().toByteArray();
		}
		String gameName = retrieveGameNameFromRoomName(roomName);
		try {
			return serverMessageBuilder
					.setLevelInitialized(getPlayEngineForClient(clientId).loadOriginalGameState(gameName, levelToLoad))
					.build().toByteArray();
		} catch (IOException e) {
			return serverMessageBuilder.setLevelInitialized(LevelInitialized.getDefaultInstance()).build()
					.toByteArray();
		}
	}

	byte[] getLevelElements(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		return serverMessageBuilder.addAllLevelSprites(
				getPlayEngineForClient(clientId).getLevelSprites(clientMessage.getGetLevelElements().getLevel()))
				.build().toByteArray();
	}

	byte[] getNumberOfLevels(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		GetNumberOfLevels getNumLevelsRequest = clientMessage.getGetNumLevels();
		return serverMessageBuilder
				.setNumLevels(NumberOfLevels.newBuilder()
						.setNumLevels(getPlayEngineForClient(clientId).getNumLevelsForGame(
								getNumLevelsRequest.getGameName(), getNumLevelsRequest.getOriginalGame()))
						.build())
				.build().toByteArray();
	}

	void disconnectClient(int clientId) {
		clientIdsToUserNames.remove(clientId);
		roomMembers.entrySet().forEach(roomEntry -> {
			if (roomEntry.getValue().contains(clientId)) {
				roomEntry.getValue().remove(new Integer(clientId));
			}
		});
	}

	void registerNotificationStreamListener(ListChangeListener<? super ServerMessage> listener) {
		messageQueue.addListener(listener);
	}

	// Try refactoring / replacing following 4 methods using Reflection instead

	byte[] handleRequestAndSerializeResponse(int clientId, byte[] inputBytes) throws ReflectiveOperationException {
		try {
			ServerMessage.Builder serverMessageBuilder = ServerMessage.newBuilder();
			ClientMessage clientMessage = ClientMessage.parseFrom(inputBytes);
			byte[] preGameResponse = handlePreGameRequestAndSerializeResponse(clientId, clientMessage,
					serverMessageBuilder);
			return preGameResponse.length > 0 ? preGameResponse
					: handleEarlyGameRequestAndSerializeResponse(clientId, clientMessage, serverMessageBuilder);

		} catch (IOException e) {
			e.printStackTrace(); // TEMP
			return new byte[] {}; // TEMP - Should create a generic error message
		}
	}

	private byte[] handlePreGameRequestAndSerializeResponse(int clientId, ClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) {
		if (clientMessage.hasGetAvailableGames()) {
			return getAvailableGames(clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasGetGameRooms()) {
			return getGameRooms(clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasCreateGameRoom()) {
			return createGameRoom(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasJoinRoom()) {
			return joinGameRoom(clientId, clientMessage, serverMessageBuilder);
		}
		// Following requests need user to be in a game room
		if (!clientIsInAGameRoom(clientId)) {
			return serverMessageBuilder.setError(ERROR_UNAUTHORIZED).build().toByteArray();
		}

		if (clientMessage.hasExitRoom()) {
			return exitGameRoom(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasLaunchGameRoom()) {
			return launchGameRoom(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasGetPlayerNames()) {
			return getPlayerNames(clientId, clientMessage, serverMessageBuilder);
		}
		return serverMessageBuilder.getDefaultInstanceForType().toByteArray();
	}

	private byte[] handleEarlyGameRequestAndSerializeResponse(int clientId, ClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) throws ReflectiveOperationException {
		if (clientMessage.hasPerformUpdate()) {
			return handleUpdate(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasPauseGame()) {
			return handlePauseGame(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasResumeGame()) {
			return handleResumeGame(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasGetInventory()) {
			return getInventory(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasGetTemplateProperties()) {
			return getTemplateProperties(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasGetAllTemplateProperties()) {
			return getAllTemplateProperties(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasGetElementCosts()) {
			return getElementCosts(clientId, clientMessage, serverMessageBuilder);
		}
		return handleLateGameRequestAndSerializeResponse(clientId, clientMessage, serverMessageBuilder);
	}

	private byte[] handleLateGameRequestAndSerializeResponse(int clientId, ClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) throws ReflectiveOperationException {
		if (clientMessage.hasPlaceElement()) {
			return placeElement(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasMoveElement()) {
			return moveElement(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasUpgradeElement()) {
			return upgradeElement(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasDeleteElement()) {
			return deleteElement(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasCheckReadyForNextLevel()) {
			return checkReadyForNextLevel(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasLoadLevel()) {
			return loadLevel(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasGetLevelElements()) {
			return getLevelElements(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasGetNumLevels()) {
			return getNumberOfLevels(clientId, clientMessage, serverMessageBuilder);
		}
		return ServerMessage.getDefaultInstance().toByteArray();
	}

	private boolean clientIsInAGameRoom(int clientId) {
		return roomMembers.values().stream().filter(clientIds -> clientIds.contains(clientId)).count() > 0;
	}

	private String getGameRoomNameOfClient(int clientId) {
		return roomMembers.keySet().stream().filter(roomName -> roomMembers.get(roomName).contains(clientId)).iterator()
				.next();
	}

	private boolean userNameExistsInGameRoom(String userName, String gameRoomName) {
		return roomMembers.get(gameRoomName).stream().map(clientId -> clientIdsToUserNames.get(clientId))
				.collect(Collectors.toSet()).contains(userName);
	}

	private Set<String> getUserNamesInGameRoom(String gameRoomName) {
		return roomMembers.get(gameRoomName).stream().map(roomMemberId -> clientIdsToUserNames.get(roomMemberId))
				.collect(Collectors.toSet());
	}

	private boolean clientIsFirstMemberOfGameRoom(int clientId) {
		if (!clientIsInAGameRoom(clientId)) {
			return false;
		}
		return roomMembers.get(getGameRoomNameOfClient(clientId)).iterator().next().equals(clientId);
	}

	private String generateUniqueRoomName(String roomName) {
		int numCollisions = roomNameCollisions.getOrDefault(roomName, 0);
		roomNameCollisions.put(roomName, numCollisions + 1);
		if (numCollisions > 0) {
			roomName += ROOM_NAME_DEDUP_DELIMITER + Integer.toString(numCollisions);
		}
		return roomName;
	}

	private String retrieveGameNameFromRoomName(String roomName) throws IllegalArgumentException {
		if (!roomNamesToGameNames.containsKey(roomName)) {
			throw new IllegalArgumentException(ERROR_NONEXISTENT_ROOM);
		}
		return roomNamesToGameNames.get(roomName);
	}

	private void clearWaitingRoom(String roomName) {
		waitingInRoom.put(roomName, 0);
	}

	private boolean joinAndCheckIfWaitingRoomIsFull(String roomName) {
		waitingInRoom.put(roomName, waitingInRoom.getOrDefault(roomName, 0) + 1);
		return checkIfWaitingRoomIsFull(roomName);
	}

	private boolean checkIfWaitingRoomIsFull(String roomName) {
		return waitingInRoom.get(roomName) < roomMembers.get(roomName).size();
	}

	private PlayController getPlayEngineForClient(int clientId) {
		return roomNamesToPlayEngines.get(getGameRoomNameOfClient(clientId));
	}

	private void processUserJoinRoom(int clientId, String roomName, String userName) {
		roomMembers.get(roomName).add(clientId);
		clientIdsToUserNames.put(clientId, userName);
	}

	private void processUserExitRoom(int clientId) {
		clientIdsToUserNames.remove(clientId);
		roomMembers.get(getGameRoomNameOfClient(clientId)).remove(new Integer(clientId));
	}

}