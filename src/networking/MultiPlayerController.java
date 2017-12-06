package networking;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import engine.play_engine.PlayController;
import javafx.geometry.Point2D;
import networking.protocol.PlayerClient.ClientMessage;
import networking.protocol.PlayerClient.CreateGameRoom;
import networking.protocol.PlayerServer.GameRoomCreationStatus;
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
	public static final String GAME_ROOM_CREATION_ERROR_CLIENT_ENGAGED = "You are already in another game room";
	public static final String GAME_ROOM_CREATION_ERROR_NONEXISTENT_GAME = "This game does not exist";

	// Should support multiple concurrent game rooms, i.e. need multiple
	// concurrent engines
	private Map<Integer, PlayController> clientIdsToPlayEngines = new HashMap<>();
	private Map<String, Set<Integer>> roomMembers = new HashMap<>();
	private Map<Integer, String> clientIdsToUserNames = new HashMap<>();
	private AtomicInteger gameCounter = new AtomicInteger();

	public MultiPlayerController() {
	}

	GameRoomCreationStatus createGameRoom(int clientId, CreateGameRoom gameRoomCreationRequest) {
		GameRoomCreationStatus.Builder builder = GameRoomCreationStatus.newBuilder();
		String gameName = gameRoomCreationRequest.getRoomName();
		// Only allow a given client process to play one game at a time
		if (clientIdsToPlayEngines.containsKey(clientId)) {
			return builder.setError(GAME_ROOM_CREATION_ERROR_CLIENT_ENGAGED).build();
		}
		String gameId = gameName + Integer.toString(gameCounter.incrementAndGet());
		// Verify that gameName is valid
		PlayController controllerForGame = new PlayController();
		if (!controllerForGame.getAvailableGames().containsKey(gameName)) {
			return builder.setError(GAME_ROOM_CREATION_ERROR_NONEXISTENT_GAME).build();
		}
		clientIdsToPlayEngines.put(clientId, controllerForGame);
		roomMembers.put(gameId, new HashSet<>());
		return builder.setRoomId(gameId).build();
	}

	boolean joinGameRoom(int clientId, String gameRoomName, String userName) {
		// TODO Auto-generated method stub
		return false;
	}

	void launchGameRoom(int clientId, String gameRoomName) {
		// TODO Auto-generated method stub

	}

	Set<String> getGameRooms() {
		// TODO Auto-generated method stub
		return null;
	}

	Set<String> getPlayerNames(int clientId, String gameRoomName) {
		// TODO Auto-generated method stub
		return null;
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

	byte[] handleRequestAndSerializeResponse(int clientId, byte[] inputBytes) {
		// Dispatch appropriate method - TODO : Reflection instead of switch case
		try {
			ServerMessage.Builder serverMessageBuilder = ServerMessage.newBuilder();
			ClientMessage clientMessage = ClientMessage.parseFrom(inputBytes);
			if (clientMessage.hasCreateGameRoom()) {
				serverMessageBuilder
						.setGameRoomCreationStatus(createGameRoom(clientId, clientMessage.getCreateGameRoom()));
			} 
			// TODO - Process other message types
			return serverMessageBuilder.build().toByteArray();
		} catch (IOException e) {
			e.printStackTrace(); // TEMP 
			return new byte[] {}; // TEMP - Should create a generic error message
		}
	}

	public static void main(String[] args) {
		MultiPlayerController testController = new MultiPlayerController();
		// Mock client
		ClientMessage.Builder testRequestBuilder = ClientMessage.newBuilder();
		testRequestBuilder.setCreateGameRoom(CreateGameRoom.newBuilder().setRoomName("abc.voog").build());
		try {
			byte[] serverResponse = testController.handleRequestAndSerializeResponse(1, testRequestBuilder.build().toByteArray());			
			ServerMessage serverMessage = ServerMessage.parseFrom(serverResponse);
			if (serverMessage.hasGameRoomCreationStatus()) {
				GameRoomCreationStatus gameRoomCreationStatus = serverMessage.getGameRoomCreationStatus();
				if (!gameRoomCreationStatus.hasError()) {
					String gameRoomId = serverMessage.getGameRoomCreationStatus().getRoomId();
					System.out.println("Successfully retrieved gameRoomId: " + gameRoomId);
				} else {
					System.out.println("Error creating game room: " + gameRoomCreationStatus.getError());
				}
			} else {
				System.out.println("No game room creation response");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
