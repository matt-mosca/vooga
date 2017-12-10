package networking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import engine.play_engine.PlayController;
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
	public static final String ERROR_CLIENT_ENGAGED = "You are already in another game room";
	public static final String ERROR_NONEXISTENT_ROOM = "This game room does not exist";
	public static final String ERROR_WRONG_ROOM = "You do not belong to this room";
	public static final String GAME_ROOM_CREATION_ERROR_NONEXISTENT_GAME = "This game does not exist";
	public static final String GAME_ROOM_JOIN_ERROR_USERNAME_TAKEN = "This username has already been taken for this game room";
	public static final String LOAD_LEVEL_ERROR_NOT_READY = "Your peers are not yet ready to load this level";

	private static final String GAME_ROOM_ID_DELIMITER = "_";

	// Should support multiple concurrent game rooms, i.e. need multiple
	// concurrent engines
	private Map<Integer, PlayController> clientIdsToPlayEngines = new HashMap<>();
	private Map<String, List<Integer>> roomMembers = new HashMap<>();
	private Map<Integer, String> clientIdsToUserNames = new HashMap<>();
	private Map<String, Integer> waitingInRoom = new HashMap<>();
	private AtomicInteger gameCounter = new AtomicInteger();

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
			String gameName = gameRoomCreationRequest.getRoomName();
			// Only allow a given client process to play one game at a time
			if (clientIdsToPlayEngines.containsKey(clientId)) {
				serverMessageBuilder.setGameRoomCreationStatus(
						gameRoomCreationStatusBuilder.setError(ERROR_CLIENT_ENGAGED).build());
				return;
			}
			String gameId = generateGameRoomNameFromGameName(gameName);
			// Verify that gameName is valid
			PlayController controllerForGame = new PlayController();
			if (!controllerForGame.getAvailableGames().containsKey(gameName)) {
				serverMessageBuilder.setGameRoomCreationStatus(
						gameRoomCreationStatusBuilder.setError(GAME_ROOM_CREATION_ERROR_NONEXISTENT_GAME).build());
				return;
			}
			clientIdsToPlayEngines.put(clientId, controllerForGame);
			roomMembers.put(gameId, new ArrayList<>());
			clearWaitingRoom(gameId);
			serverMessageBuilder.setGameRoomCreationStatus(gameRoomCreationStatusBuilder.setRoomId(gameId).build());
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
			clientIdsToPlayEngines.put(clientId, getPlayControllerForGameRoom(roomName));
			roomMembers.get(roomName).add(clientId);
			clientIdsToUserNames.put(clientId, userName);
			serverMessageBuilder.setGameRoomJoinStatus(gameRoomJoinStatusBuilder.setSuccess(true).build());
		}
	}

	void launchGameRoom(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		if (clientMessage.hasLaunchGameRoom()) {
			GameRoomLaunchStatus.Builder gameRoomLaunchStatusBuilder = GameRoomLaunchStatus.newBuilder();
			String gameRoomToLaunch = clientMessage.getLaunchGameRoom().getRoomName();
			if (!roomMembers.containsKey(gameRoomToLaunch)) {
				serverMessageBuilder
						.setGameRoomLaunchStatus(gameRoomLaunchStatusBuilder.setError(ERROR_NONEXISTENT_ROOM).build());
				return;
			}
			String gameName = getGameNameFromGameRoomName(gameRoomToLaunch);
			try {
				serverMessageBuilder.setGameRoomLaunchStatus(gameRoomLaunchStatusBuilder
						.setInitialState(clientIdsToPlayEngines.get(clientId).loadOriginalGameState(gameName, 1))
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
			String gameRoomName = clientMessage.getGetPlayerNames().getRoomName();
			if (!roomMembers.containsKey(gameRoomName)) {
				serverMessageBuilder.setPlayerNames(playerNamesBuilder.setError(ERROR_NONEXISTENT_ROOM).build());
				return;
			}
			if (!roomMembers.get(gameRoomName).contains(clientId)) {
				serverMessageBuilder.setPlayerNames(playerNamesBuilder.setError(ERROR_WRONG_ROOM).build());
				return;
			}
			serverMessageBuilder
					.setPlayerNames(playerNamesBuilder.addAllUserNames(getUserNamesInGameRoom(gameRoomName)).build());
		}
	}

	// TODO - Consider server push instead of client pull? Would be more complicated
	// but more accurate / realistic, and fewer packets exchanged
	void handleUpdate(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		if (clientMessage.hasPerformUpdate()) {
			PlayController playController = clientIdsToPlayEngines.get(clientId);
			// TODO - Handle case where client tries to perform update without belonging to
			// a game room?
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
			PlayController playController = clientIdsToPlayEngines.get(clientId);
			// TODO - Handle case where client tries to pause game without belonging to a
			// game room?
			playController.pause();
			serverMessageBuilder.setUpdate(playController.packageStatusUpdate());
		}
	}

	void handleResumeGame(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		if (clientMessage.hasResumeGame()) {
			PlayController playController = clientIdsToPlayEngines.get(clientId);
			// TODO - Handle case where client tries to resume game without belonging to a
			// game room?
			playController.resume();
			serverMessageBuilder.setUpdate(playController.packageStatusUpdate());
		}
	}

	void getInventory(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		if (clientMessage.hasGetInventory()) {
			// TODO - Handle case where client tries to get inventory without belonging to a
			// game room?
			serverMessageBuilder.setInventory(clientIdsToPlayEngines.get(clientId).packageInventory());
		}
	}

	void getTemplateProperties(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		if (clientMessage.hasGetTemplateProperties()) {
			// TODO - Handle case where client tries to get template properties without
			// belonging to a
			// game room?
			serverMessageBuilder.addTemplateProperties(clientIdsToPlayEngines.get(clientId)
					.packageTemplateProperties(clientMessage.getGetTemplateProperties().getElementName()));
		}
	}

	void getAllTemplateProperties(int clientId, ClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) {
		if (clientMessage.hasGetAllTemplateProperties()) {
			// TODO - Handle case where client tries to get template properties without
			// belonging to a
			// game room?
			serverMessageBuilder
					.addAllTemplateProperties(clientIdsToPlayEngines.get(clientId).packageAllTemplateProperties());
		}
	}

	void getElementCosts(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		if (clientMessage.hasGetElementCosts()) {
			// TODO - Handle case where client tries to get template properties without
			// belonging to a game room?
			serverMessageBuilder.addAllElementCosts(clientIdsToPlayEngines.get(clientId).packageAllElementCosts());
		}
	}

	void placeElement(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder)
			throws ReflectiveOperationException {
		if (clientMessage.hasPlaceElement()) {
			PlayController playController = clientIdsToPlayEngines.get(clientId);
			// TODO - Handle case where client tries to place element without belonging to a
			// game room?
			PlaceElement placeElementRequest = clientMessage.getPlaceElement();
			serverMessageBuilder.setElementPlaced(playController.placeElement(placeElementRequest.getElementName(),
					new Point2D(placeElementRequest.getXCoord(), placeElementRequest.getYCoord())));
		}
	}

	void upgradeElement(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) throws
			ReflectiveOperationException {
		if (clientMessage.hasUpgradeElement()) {
			PlayController playController = clientIdsToPlayEngines.get(clientId);
			// TODO - Handle case where client tries to upgrade element without belonging to
			// a game room?
			playController.upgradeElement(clientMessage.getUpgradeElement().getSpriteId());
		}
	}

	void checkReadyForNextLevel(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		if (clientMessage.hasCheckReadyForNextLevel()) {
			// TODO - Handle case where client tries to place element without belonging to a
			// game room?
			serverMessageBuilder.setReadyForNextLevel(ReadyForNextLevel.newBuilder()
					.setIsReady(joinAndCheckIfWaitingRoomIsFull(getGameRoomNameOfClient(clientId))).build());
		}
	}

	void loadLevel(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		if (clientMessage.hasLoadLevel()) {
			LevelInitialized.Builder levelInitializationBuilder = LevelInitialized.newBuilder();
			// TODO - Handle case where client tries to load level without belonging to a
			// game room?
			LoadLevel loadLevelRequest = clientMessage.getLoadLevel();
			String roomName = getGameRoomNameOfClient(clientId);
			int levelToLoad = loadLevelRequest.getLevel();
			if (!checkIfWaitingRoomIsFull(roomName)) {
				// not ready to load
				serverMessageBuilder
						.setLevelInitialized(levelInitializationBuilder.setError(LOAD_LEVEL_ERROR_NOT_READY).build());
				return;
			}
			String gameName = getGameNameFromGameRoomName(roomName);
			try {
				serverMessageBuilder.setLevelInitialized(
						clientIdsToPlayEngines.get(clientId).loadOriginalGameState(gameName, levelToLoad));
			} catch (IOException e) {
				serverMessageBuilder.setLevelInitialized(LevelInitialized.getDefaultInstance());
			}
		}
	}

	void getLevelElements(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		if (clientMessage.hasGetLevelElements()) {
			// TODO - Handle case where client tries to load level without belonging to a
			// game room?
			serverMessageBuilder.addAllLevelSprites(clientIdsToPlayEngines.get(clientId)
					.getLevelSprites(clientMessage.getGetLevelElements().getLevel()));
		}
	}

	void getNumberOfLevels(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		if (clientMessage.hasGetNumLevels()) {
			GetNumberOfLevels getNumLevelsRequest = clientMessage.getGetNumLevels();
			serverMessageBuilder
					.setNumLevels(
							NumberOfLevels.newBuilder()
									.setNumLevels(clientIdsToPlayEngines.get(clientId).getNumLevelsForGame(
											getNumLevelsRequest.getGameName(), getNumLevelsRequest.getOriginalGame()))
									.build());
		}
	}

	void disconnectClient(int clientId) {
		clientIdsToPlayEngines.remove(clientId);
		clientIdsToUserNames.remove(clientId);
		for (String gameRoomName : roomMembers.keySet()) {
			if (roomMembers.get(gameRoomName).contains(clientId)) {
				roomMembers.get(gameRoomName).remove(clientId);
			}
		}
	}

	byte[] handleRequestAndSerializeResponse(int clientId, byte[] inputBytes) throws ReflectiveOperationException {
		System.out.println("ClientId: " + clientId);
		// Dispatch appropriate method - TODO : Reflection ?
		try {
			ServerMessage.Builder serverMessageBuilder = ServerMessage.newBuilder();
			ClientMessage clientMessage = ClientMessage.parseFrom(inputBytes);
			// Get available games
			getAvailableGames(clientMessage, serverMessageBuilder);
			// Handle game room creation request
			createGameRoom(clientId, clientMessage, serverMessageBuilder);
			// Handle game room join request
			joinGameRoom(clientId, clientMessage, serverMessageBuilder);
			// Launch game room
			launchGameRoom(clientId, clientMessage, serverMessageBuilder);
			// Handle game rooms request
			getGameRooms(clientMessage, serverMessageBuilder);
			// Handle player names request
			getPlayerNames(clientId, clientMessage, serverMessageBuilder);
			// Handle update request
			handleUpdate(clientId, clientMessage, serverMessageBuilder);
			// Handle pause request
			handlePauseGame(clientId, clientMessage, serverMessageBuilder);
			// Handle resume request
			handleResumeGame(clientId, clientMessage, serverMessageBuilder);
			// Get inventory
			getInventory(clientId, clientMessage, serverMessageBuilder);
			// Get template properties
			getTemplateProperties(clientId, clientMessage, serverMessageBuilder);
			// Get all template properties
			getAllTemplateProperties(clientId, clientMessage, serverMessageBuilder);
			// Get element costs for current level
			getElementCosts(clientId, clientMessage, serverMessageBuilder);
			// Handle place element request
			placeElement(clientId, clientMessage, serverMessageBuilder);
			// Handle upgrade element request
			upgradeElement(clientId, clientMessage, serverMessageBuilder);
			// Handle check ready for next level request
			checkReadyForNextLevel(clientId, clientMessage, serverMessageBuilder);
			// Handle load request
			loadLevel(clientId, clientMessage, serverMessageBuilder);
			// Get level elements
			getLevelElements(clientId, clientMessage, serverMessageBuilder);
			// Get number of levels
			getNumberOfLevels(clientId, clientMessage, serverMessageBuilder);
			return serverMessageBuilder.build().toByteArray();
		} catch (IOException e) {
			e.printStackTrace(); // TEMP
			return new byte[] {}; // TEMP - Should create a generic error message
		}
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
		return getUserNamesInGameRoom(getGameRoomNameOfClient(clientId)).iterator().next().equals(clientId);
	}

	private PlayController getPlayControllerForGameRoom(String gameRoomName) {
		return clientIdsToPlayEngines.get(roomMembers.get(gameRoomName).iterator().next());
	}

	private String generateGameRoomNameFromGameName(String gameName) {
		return gameName + GAME_ROOM_ID_DELIMITER + Integer.toString(gameCounter.incrementAndGet());
	}

	private String getGameNameFromGameRoomName(String roomName) {
		return roomName.substring(0, roomName.lastIndexOf(GAME_ROOM_ID_DELIMITER));
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

}
