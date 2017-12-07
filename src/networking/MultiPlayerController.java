package networking;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import engine.play_engine.PlayController;
import javafx.geometry.Point2D;
import networking.protocol.PlayerClient.ClientMessage;
import networking.protocol.PlayerClient.CreateGameRoom;
import networking.protocol.PlayerClient.JoinRoom;
import networking.protocol.PlayerServer.GameRoomCreationStatus;
import networking.protocol.PlayerServer.GameRoomJoinStatus;
import networking.protocol.PlayerServer.GameRoomLaunchStatus;
import networking.protocol.PlayerServer.GameRooms;
import networking.protocol.PlayerServer.PlayerNames;
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
	public static final String GAME_ROOM_CREATION_ERROR_NONEXISTENT_GAME = "This game does not exist";
	public static final String GAME_ROOM_JOIN_ERROR_USERNAME_TAKEN = "This username has already been taken for this game room";
	public static final String PLAYER_NAMES_ERROR_WRONG_ROOM = "You do not belong to this room";

	// Should support multiple concurrent game rooms, i.e. need multiple
	// concurrent engines
	private Map<Integer, PlayController> clientIdsToPlayEngines = new HashMap<>();
	private Map<String, Set<Integer>> roomMembers = new HashMap<>();
	private Map<Integer, String> clientIdsToUserNames = new HashMap<>();
	private AtomicInteger gameCounter = new AtomicInteger();

	public MultiPlayerController() {
	}

	void getGameRooms(ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		if (clientMessage.hasGetAvailableGames()) {
			GameRooms.Builder gameRoomsBuilder = GameRooms.newBuilder();
			serverMessageBuilder.setGameRooms(
					gameRoomsBuilder.addAllRoomNames(new PlayController().getAvailableGames().keySet()).build());
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
			String gameId = gameName + Integer.toString(gameCounter.incrementAndGet());
			// Verify that gameName is valid
			PlayController controllerForGame = new PlayController();
			if (!controllerForGame.getAvailableGames().containsKey(gameName)) {
				serverMessageBuilder.setGameRoomCreationStatus(
						gameRoomCreationStatusBuilder.setError(GAME_ROOM_CREATION_ERROR_NONEXISTENT_GAME).build());
				return;
			}
			clientIdsToPlayEngines.put(clientId, controllerForGame);
			roomMembers.put(gameId, new HashSet<>());
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
			serverMessageBuilder.setGameRoomLaunchStatus(gameRoomLaunchStatusBuilder
					.setInitialState(clientIdsToPlayEngines.get(clientId).packageInitialState()).build());// TEMP
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
				serverMessageBuilder.setPlayerNames(playerNamesBuilder.setError(PLAYER_NAMES_ERROR_WRONG_ROOM).build());
				return;
			}
			serverMessageBuilder
					.setPlayerNames(playerNamesBuilder.addAllUserNames(getUserNamesInGameRoom(gameRoomName)).build());
		}
	}

	void update(String gameRoomName) {
		// TODO Auto-generated method stub

	}

	void pause(int clientId) {
		// TODO Auto-generated method stub

	}

	void resume(int clientId) {
		// TODO Auto-generated method stub

	}

	boolean isLost(int clientId) {
		// TODO Auto-generated method stub
		return false;
	}

	boolean isLevelCleared(int clientId) {
		// TODO Auto-generated method stub
		return false;
	}

	boolean isWon(int clientId) {
		// TODO Auto-generated method stub
		return false;
	}

	int placeElement(int clientId, String elementName, Point2D startCoordinates) {
		// TODO Auto-generated method stub
		return 0;
	}

	Map<String, String> getAvailableGames() {
		// TODO Auto-generated method stub
		return null;
	}

	Map<String, String> getTemplateProperties(int clientId, String elementName) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	Map<String, Map<String, String>> getAllDefinedTemplateProperties(int clientId) {
		// TODO Auto-generated method stub
		return null;
	}

	Set<String> getInventory(int clientId) {
		// TODO Auto-generated method stub
		return null;
	}

	Map<String, Double> getStatus(int clientId) {
		// TODO Auto-generated method stub
		return null;
	}

	Map<String, Double> getResourceEndowments(int clientId) {
		// TODO Auto-generated method stub
		return null;
	}

	Map<String, Map<String, Double>> getElementCosts(int clientId) {
		// TODO Auto-generated method stub
		return null;
	}

	Collection<Integer> getLevelSprites(int level) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	void saveGameState(File fileToSaveTo) throws UnsupportedOperationException {
		// TODO Auto-generated method stub

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

	byte[] handleRequestAndSerializeResponse(int clientId, byte[] inputBytes) {
		System.out.println("ClientId: " + clientId);
		// Dispatch appropriate method - TODO : Reflection ?
		try {
			ServerMessage.Builder serverMessageBuilder = ServerMessage.newBuilder();
			ClientMessage clientMessage = ClientMessage.parseFrom(inputBytes);
			// Get available games
			getGameRooms(clientMessage, serverMessageBuilder);
			// Handle game room creation request
			createGameRoom(clientId, clientMessage, serverMessageBuilder);
			// Handle game room join request
			joinGameRoom(clientId, clientMessage, serverMessageBuilder);
			// Handler player names request
			getPlayerNames(clientId, clientMessage, serverMessageBuilder);
			// TODO - Process other message types
			return serverMessageBuilder.build().toByteArray();
		} catch (IOException e) {
			e.printStackTrace(); // TEMP
			return new byte[] {}; // TEMP - Should create a generic error message
		}
	}

	private boolean clientIsInAGameRoom(int clientId) {
		return roomMembers.values().stream().filter(clientIds -> clientIds.contains(clientId)).count() > 0;
	}

	private boolean userNameExistsInGameRoom(String userName, String gameRoomName) {
		return roomMembers.get(gameRoomName).stream().map(clientId -> clientIdsToUserNames.get(clientId))
				.collect(Collectors.toSet()).contains(userName);
	}

	private String getGameRoomOfClient(int clientId) {
		return roomMembers.keySet().stream().filter(roomName -> roomMembers.get(roomName).contains(clientId))
				.findFirst().get();
	}

	private Set<String> getUserNamesInGameRoom(String gameRoomName) {
		return roomMembers.get(gameRoomName).stream().map(roomMemberId -> clientIdsToUserNames.get(roomMemberId))
				.collect(Collectors.toSet());
	}

	private PlayController getPlayControllerForGameRoom(String gameRoomName) {
		return clientIdsToPlayEngines.get(roomMembers.get(gameRoomName).iterator().next());
	}

}
