package networking;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.protobuf.Field;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;

import engine.play_engine.PlayController;
import javafx.geometry.Point2D;
import networking.protocol.PlayerServer;

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
class MultiPlayerController implements MultiPlayerModelController {

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

	@Override
	public String createGameRoom(int clientId, String gameName) {
		// Only allow a given client process to play one game at a time
		if (clientIdsToPlayEngines.containsKey(clientId)) {
			return GAME_ROOM_CREATION_ERROR_CLIENT_ENGAGED;
		}
		String gameId = gameName + Integer.toString(gameCounter.incrementAndGet());
		// Verify that gameName is valid
		PlayController controllerForGame = new PlayController();
		if (!controllerForGame.getAvailableGames().containsKey(gameName)) {
			return GAME_ROOM_CREATION_ERROR_NONEXISTENT_GAME;
		}
		clientIdsToPlayEngines.put(clientId, controllerForGame);
		roomMembers.put(gameId, new HashSet<>());
		return gameId;
	}

	@Override
	public boolean joinGameRoom(int clientId, String gameRoomName, String userName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void launchGameRoom(int clientId, String gameRoomName) {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<String> getGameRooms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getPlayerNames(int clientId, String gameRoomName) {
		// TODO Auto-generated method stub
		return null;
	}

	public String handleRequestAndSerializeResponse(int clientId, String requestBuffer) {
		// Dispatch appropriate method - TODO : Reflection instead of switch case

		// Serialize the return value of the method
		
		return "";// TEMP

	}

	// TODO - Serialization for other message types OR figure out a way to write
	// generic method that works for all message types
	private String serializeGameRoomCreationStatus(boolean success, String description) {
		PlayerServer.GameRoomCreationStatus.Builder builder = PlayerServer.GameRoomCreationStatus.newBuilder();
		if (success) {
			builder.setRoomId(description);
		} else {
			builder.setError(description);
		}
		return builder.toString();
	}

	/*
	 * private String serializeMessage(Class<? extends Message> messageClass,
	 * Map<String, ?> messageData) { java.lang.reflect.Field[] messageFields =
	 * messageClass.getDeclaredFields(); Builder message = Builder messageBuilder =
	 * ((Message) messageClass).newBuilderForType(); for (java.lang.reflect.Field
	 * field : messageFields) { if (messageData.containsKey(field)) {
	 * 
	 * } }
	 * 
	 * Message message = return message.toString(); }
	 */

	public static void main(String[] args) {

	}

	@Override
	public void update(String gameRoomName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause(int clientId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume(int clientId) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLost(int clientId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLevelCleared(int clientId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isWon(int clientId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int placeElement(int clientId, String elementName, Point2D startCoordinates) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Map<String, String> getAvailableGames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getTemplateProperties(int clientId, String elementName) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Map<String, String>> getAllDefinedTemplateProperties(int clientId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getInventory(int clientId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Double> getStatus(int clientId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Double> getResourceEndowments(int clientId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Map<String, Double>> getElementCosts(int clientId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Integer> getLevelSprites(int level) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveGameState(File fileToSaveTo) throws UnsupportedOperationException {
		// TODO Auto-generated method stub

	}

}
