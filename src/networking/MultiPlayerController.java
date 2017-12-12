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
import networking.protocol.PlayerClient.ClientMessage;
import networking.protocol.PlayerClient.CreateGameRoom;
import networking.protocol.PlayerClient.GetNumberOfLevels;
import networking.protocol.PlayerClient.JoinRoom;
import networking.protocol.PlayerClient.LoadLevel;
import networking.protocol.PlayerClient.PlaceElement;
import networking.protocol.PlayerServer.Game;
import networking.protocol.PlayerServer.GameRoomCreationStatus;
import networking.protocol.PlayerServer.GameRoomJoinStatus;
import networking.protocol.PlayerServer.GameRoomLaunchStatus;
import networking.protocol.PlayerServer.GameRooms;
import networking.protocol.PlayerServer.Games;
import networking.protocol.PlayerServer.LevelInitialized;
import networking.protocol.PlayerServer.NumberOfLevels;
import networking.protocol.PlayerServer.PlayerJoined;
import networking.protocol.PlayerServer.PlayerNames;
import networking.protocol.PlayerServer.ReadyForNextLevel;
import networking.protocol.PlayerServer.ServerMessage;

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

	void getAvailableGames(ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		if (clientMessage.hasGetAvailableGames()) {
			Games.Builder gamesBuilder = Games.newBuilder();
			Map<String, String> availableGames = new PlayController().getAvailableGames();
			availableGames.keySet().forEach(gameName -> gamesBuilder.addGames(
					Game.newBuilder().setName(gameName).setDescription(availableGames.get(gameName)).build()));
			serverMessageBuilder.setAvailableGames(gamesBuilder.build());
		}
	}

	void createGameRoom(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		if (clientMessage.hasCreateGameRoom()) {
			CreateGameRoom gameRoomCreationRequest = clientMessage.getCreateGameRoom();
			GameRoomCreationStatus.Builder gameRoomCreationStatusBuilder = GameRoomCreationStatus.newBuilder();
			// Only allow a given client process to play one game at a time
			if (clientIdsToUserNames.containsKey(clientId)) {
				serverMessageBuilder.setGameRoomCreationStatus(
						gameRoomCreationStatusBuilder.setError(ERROR_CLIENT_ENGAGED).build());
				return;
			}
			String gameName = gameRoomCreationRequest.getGameName();
			String roomName = generateUniqueRoomName(gameRoomCreationRequest.getRoomName());
			// Verify that gameName is valid
			PlayController controllerForGame = new PlayController();
			if (!controllerForGame.getAvailableGames().containsKey(gameName)) {
				serverMessageBuilder.setGameRoomCreationStatus(
						gameRoomCreationStatusBuilder.setError(GAME_ROOM_CREATION_ERROR_NONEXISTENT_GAME).build());
				return;
			}
			roomNamesToPlayEngines.put(roomName, controllerForGame);
			roomNamesToGameNames.put(roomName, gameName);
			roomMembers.put(roomName, new ArrayList<>());
			clearWaitingRoom(roomName);
			serverMessageBuilder.setGameRoomCreationStatus(gameRoomCreationStatusBuilder.setRoomId(roomName).build());
		}
	}

	void joinGameRoom(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		if (clientMessage.hasJoinRoom()) {
			JoinRoom joinRoomRequest = clientMessage.getJoinRoom();
			GameRoomJoinStatus.Builder gameRoomJoinStatusBuilder = GameRoomJoinStatus.newBuilder();
			// Check if client is already in some other game
			if (clientIsInAGameRoom(clientId)) {
				serverMessageBuilder.setGameRoomJoinStatus(
						gameRoomJoinStatusBuilder.setSuccess(false).setError(ERROR_CLIENT_ENGAGED).build());
				return;
			}
			String roomName = joinRoomRequest.getRoomName();
			if (!roomMembers.containsKey(roomName)) {
				serverMessageBuilder.setGameRoomJoinStatus(
						gameRoomJoinStatusBuilder.setSuccess(false).setError(ERROR_NONEXISTENT_ROOM).build());
				return;
			}
			String userName = joinRoomRequest.getUserName();
			// Check if username is taken within this room
			if (userNameExistsInGameRoom(userName, roomName)) {
				serverMessageBuilder.setGameRoomJoinStatus(gameRoomJoinStatusBuilder.setSuccess(false)
						.setError(GAME_ROOM_JOIN_ERROR_USERNAME_TAKEN).build());
				return;
			}
			processUserJoinRoom(clientId, roomName, userName);
			/*
			// Push message to other clients
			messageQueue.add(ServerMessage.newBuilder()
					.setPlayerJoined(PlayerJoined.newBuilder().setUserName(userName).build()).build());
			*/
			serverMessageBuilder.setGameRoomJoinStatus(gameRoomJoinStatusBuilder.setSuccess(true).build());
		}
	}

	void exitGameRoom(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		if (clientMessage.hasExitRoom()) {
			if (clientIsInAGameRoom(clientId)) {
				processUserExitRoom(clientId);
			}
			// just ignore if client is not in a game room
		}
	}

	void launchGameRoom(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		if (clientMessage.hasLaunchGameRoom()) {
			GameRoomLaunchStatus.Builder gameRoomLaunchStatusBuilder = GameRoomLaunchStatus.newBuilder();
			String roomName = getGameRoomNameOfClient(clientId);
			String gameName = retrieveGameNameFromRoomName(roomName);
			try {
				serverMessageBuilder.setGameRoomLaunchStatus(gameRoomLaunchStatusBuilder
						.setInitialState(roomNamesToPlayEngines.get(roomName).loadOriginalGameState(gameName, 1))
						.build());
			} catch (IOException e) {
				serverMessageBuilder.setGameRoomLaunchStatus(
						gameRoomLaunchStatusBuilder.setError(GAME_ROOM_CREATION_ERROR_NONEXISTENT_GAME).build());
			}
		}
	}

	void getGameRooms(ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		if (clientMessage.hasGetGameRooms()) {
			serverMessageBuilder.setGameRooms(GameRooms.newBuilder().addAllRoomNames(roomMembers.keySet()).build());
		}
	}

	void getPlayerNames(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		if (clientMessage.hasGetPlayerNames()) {
			PlayerNames.Builder playerNamesBuilder = PlayerNames.newBuilder();
			serverMessageBuilder.setPlayerNames(playerNamesBuilder
					.addAllUserNames(getUserNamesInGameRoom(getGameRoomNameOfClient(clientId))).build());
		}
	}

	// TODO - Consider server push instead of client pull? Would be more complicated
	// but more accurate / realistic, and fewer packets exchanged
	void handleUpdate(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		if (clientMessage.hasPerformUpdate()) {
			PlayController playController = getPlayEngineForClient(clientId);
			if (clientIsFirstMemberOfGameRoom(clientId)) {
				// only do actual update if primary client, simply send state for the rest
				playController.update();
			}
			serverMessageBuilder.setUpdate(playController.getLatestUpdate());
		}
	}

	void handlePauseGame(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		if (clientMessage.hasPauseGame()) {
			// Verify clientId, retrieve appropriate game room / controller
			PlayController playController = getPlayEngineForClient(clientId);
			playController.pause();
			serverMessageBuilder.setUpdate(playController.packageStatusUpdate());
		}
	}

	void handleResumeGame(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		if (clientMessage.hasResumeGame()) {
			PlayController playController = getPlayEngineForClient(clientId);
			playController.resume();
			serverMessageBuilder.setUpdate(playController.packageStatusUpdate());
		}
	}

	void getInventory(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		if (clientMessage.hasGetInventory()) {
			serverMessageBuilder.setInventory(getPlayEngineForClient(clientId).packageInventory());
		}
	}

	void getTemplateProperties(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		if (clientMessage.hasGetTemplateProperties()) {
			serverMessageBuilder.addTemplateProperties(getPlayEngineForClient(clientId)
					.packageTemplateProperties(clientMessage.getGetTemplateProperties().getElementName()));
		}
	}

	void getAllTemplateProperties(int clientId, ClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) {
		if (clientMessage.hasGetAllTemplateProperties()) {
			serverMessageBuilder
					.addAllTemplateProperties(getPlayEngineForClient(clientId).packageAllTemplateProperties());
		}
	}

	void getElementCosts(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		if (clientMessage.hasGetElementCosts()) {
			serverMessageBuilder.addAllElementCosts(getPlayEngineForClient(clientId).packageAllElementCosts());
		}
	}

	void placeElement(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder)
			throws ReflectiveOperationException {
		if (clientMessage.hasPlaceElement()) {
			PlayController playController = getPlayEngineForClient(clientId);
			PlaceElement placeElementRequest = clientMessage.getPlaceElement();
			serverMessageBuilder.setElementPlaced(playController.placeElement(placeElementRequest.getElementName(),
					new Point2D(placeElementRequest.getXCoord(), placeElementRequest.getYCoord())));
		}
	}

	void upgradeElement(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder)
			throws ReflectiveOperationException {
		if (clientMessage.hasUpgradeElement()) {
			PlayController playController = getPlayEngineForClient(clientId);
			playController.upgradeElement(clientMessage.getUpgradeElement().getSpriteId());
		}
	}

	void checkReadyForNextLevel(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		if (clientMessage.hasCheckReadyForNextLevel()) {
			serverMessageBuilder.setReadyForNextLevel(ReadyForNextLevel.newBuilder()
					.setIsReady(joinAndCheckIfWaitingRoomIsFull(getGameRoomNameOfClient(clientId))).build());
		}
	}

	void loadLevel(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		if (clientMessage.hasLoadLevel()) {
			LevelInitialized.Builder levelInitializationBuilder = LevelInitialized.newBuilder();
			LoadLevel loadLevelRequest = clientMessage.getLoadLevel();
			String roomName = getGameRoomNameOfClient(clientId);
			int levelToLoad = loadLevelRequest.getLevel();
			if (!checkIfWaitingRoomIsFull(roomName)) {
				// not ready to load
				serverMessageBuilder
						.setLevelInitialized(levelInitializationBuilder.setError(LOAD_LEVEL_ERROR_NOT_READY).build());
				return;
			}
			String gameName = retrieveGameNameFromRoomName(roomName);
			try {
				serverMessageBuilder.setLevelInitialized(
						getPlayEngineForClient(clientId).loadOriginalGameState(gameName, levelToLoad));
			} catch (IOException e) {
				serverMessageBuilder.setLevelInitialized(LevelInitialized.getDefaultInstance());
			}
		}
	}

	void getLevelElements(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		if (clientMessage.hasGetLevelElements()) {
			serverMessageBuilder.addAllLevelSprites(getPlayEngineForClient(clientId)
					.getLevelSprites(clientMessage.getGetLevelElements().getLevel()));
		}
	}

	void getNumberOfLevels(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		if (clientMessage.hasGetNumLevels()) {
			GetNumberOfLevels getNumLevelsRequest = clientMessage.getGetNumLevels();
			serverMessageBuilder
					.setNumLevels(
							NumberOfLevels.newBuilder()
									.setNumLevels(getPlayEngineForClient(clientId).getNumLevelsForGame(
											getNumLevelsRequest.getGameName(), getNumLevelsRequest.getOriginalGame()))
									.build());
		}
	}

	void disconnectClient(int clientId) {
		clientIdsToUserNames.remove(clientId);
		roomMembers.entrySet().forEach(roomEntry -> {
			if (roomEntry.getValue().contains(clientId)) {
				roomEntry.getValue().remove(new Integer(clientId));
			}
		});
	}

	byte[] handleRequestAndSerializeResponse(int clientId, byte[] inputBytes) throws ReflectiveOperationException {
		try {
			ServerMessage.Builder serverMessageBuilder = ServerMessage.newBuilder();
			ClientMessage clientMessage = ClientMessage.parseFrom(inputBytes);
			getAvailableGames(clientMessage, serverMessageBuilder);
			getGameRooms(clientMessage, serverMessageBuilder);
			createGameRoom(clientId, clientMessage, serverMessageBuilder);
			joinGameRoom(clientId, clientMessage, serverMessageBuilder);
			if (!clientIsInAGameRoom(clientId)) {
				return serverMessageBuilder.setError(ERROR_UNAUTHORIZED).build().toByteArray();
			}
			exitGameRoom(clientId, clientMessage, serverMessageBuilder);
			launchGameRoom(clientId, clientMessage, serverMessageBuilder);
			getPlayerNames(clientId, clientMessage, serverMessageBuilder);
			handleUpdate(clientId, clientMessage, serverMessageBuilder);
			handlePauseGame(clientId, clientMessage, serverMessageBuilder);
			handleResumeGame(clientId, clientMessage, serverMessageBuilder);
			getInventory(clientId, clientMessage, serverMessageBuilder);
			getTemplateProperties(clientId, clientMessage, serverMessageBuilder);
			getAllTemplateProperties(clientId, clientMessage, serverMessageBuilder);
			getElementCosts(clientId, clientMessage, serverMessageBuilder);
			placeElement(clientId, clientMessage, serverMessageBuilder);
			upgradeElement(clientId, clientMessage, serverMessageBuilder);
			checkReadyForNextLevel(clientId, clientMessage, serverMessageBuilder);
			loadLevel(clientId, clientMessage, serverMessageBuilder);
			getLevelElements(clientId, clientMessage, serverMessageBuilder);
			getNumberOfLevels(clientId, clientMessage, serverMessageBuilder);
			return serverMessageBuilder.build().toByteArray();
		} catch (IOException e) {
			e.printStackTrace(); // TEMP
			return new byte[] {}; // TEMP - Should create a generic error message
		}
	}

	void registerNotificationStreamListener(ListChangeListener<? super ServerMessage> listener) {
		messageQueue.addListener(listener);
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
