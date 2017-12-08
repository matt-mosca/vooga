package networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.protobuf.InvalidProtocolBufferException;

import engine.PlayModelController;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import networking.protocol.PlayerClient.ClientMessage;
import networking.protocol.PlayerClient.CreateGameRoom;
import networking.protocol.PlayerClient.GetAvailableGames;
import networking.protocol.PlayerClient.GetGameRooms;
import networking.protocol.PlayerClient.GetPlayerNames;
import networking.protocol.PlayerClient.JoinRoom;
import networking.protocol.PlayerClient.LaunchGameRoom;
import networking.protocol.PlayerClient.LoadLevel;
import networking.protocol.PlayerServer.GameRoomCreationStatus;
import networking.protocol.PlayerServer.GameRoomJoinStatus;
import networking.protocol.PlayerServer.GameRoomLaunchStatus;
import networking.protocol.PlayerServer.Games;
import networking.protocol.PlayerServer.LevelInitialized;
import networking.protocol.PlayerServer.PlayerNames;
import networking.protocol.PlayerServer.ServerMessage;

/**
 * 
 * @author radithya
 *
 */
public class MultiPlayerClient implements PlayModelController { // Is this weird?

	// TODO - Refactor common data / set-up logic of ChatClient and
	// MultiPlayerClient into
	// abstract class
	// get from some properties file
	private final String SERVER_ADDRESS = "127.0.0.1"; // Change to "152.3.53.39" once uploaded to VM
	private final int PORT = 9041;
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream outputWriter;

	// Game client state (keeping track of which multi-player game it is in, etc)

	public MultiPlayerClient() {
		setupChatSocketAndStreams();
	}

	public Map<String, String> getAvailableGames() {
		ClientMessage.Builder clientMessageBuilder = ClientMessage.newBuilder();
		writeRequestBytes(clientMessageBuilder.setGetAvailableGames(GetAvailableGames.newBuilder().build()).build()
				.toByteArray());
		return handleAvailableGamesResponse(readServerResponse());
	}

	public String createGameRoom(String gameName) {
		ClientMessage.Builder clientMessageBuilder = ClientMessage.newBuilder();
		CreateGameRoom gameRoomCreationRequest = CreateGameRoom.newBuilder().setRoomName(gameName).build();
		writeRequestBytes(clientMessageBuilder.setCreateGameRoom(gameRoomCreationRequest).build().toByteArray());
		return handleGameRoomCreationResponse(readServerResponse());
	}

	public void joinGameRoom(String roomName, String userName) {
		JoinRoom gameRoomJoinRequest = JoinRoom.newBuilder().setRoomName(roomName).setUserName(userName).build();
		writeRequestBytes(ClientMessage.newBuilder().setJoinRoom(gameRoomJoinRequest).build().toByteArray());
		handleGameRoomJoinResponse(readServerResponse());
	}

	public LevelInitialized launchGameRoom(String roomName) {
		writeRequestBytes(ClientMessage.newBuilder()
				.setLaunchGameRoom(LaunchGameRoom.newBuilder().setRoomName(roomName).build()).build().toByteArray());
		return handleLevelInitializedResponse(readServerResponse());
	}

	public Set<String> getGameRooms() {
		writeRequestBytes(
				ClientMessage.newBuilder().setGetGameRooms(GetGameRooms.getDefaultInstance()).build().toByteArray());
		return handleGameRoomsResponse(readServerResponse());
	}

	public Set<String> getPlayerNames(String roomName) {
		writeRequestBytes(ClientMessage.newBuilder()
				.setGetPlayerNames(GetPlayerNames.newBuilder().setRoomName(roomName).build()).build().toByteArray());
		return handlePlayerNamesResponse(readServerResponse());
	}

	@Override
	public void saveGameState(File fileToSaveTo) throws UnsupportedOperationException {
		// TODO - Define custom exception in exceptions properties file and pass that
		// string here
		throw new UnsupportedOperationException();
	}

	// TODO - Will be modified in interface to return LevelInitialized message
	@Override
	public void loadOriginalGameState(String saveName, int level) throws IOException {
		writeRequestBytes(ClientMessage.newBuilder()
				.setLoadLevel(LoadLevel.newBuilder().setGameName(saveName).setLevel(level)).build().toByteArray());
		// Return the following line when front end is ready
		handleLoadOriginalGameStateResponse(readServerResponse());
	}

	// Since saving is not allowed, this won't be allowed either
	@Override
	public void loadSavedPlayState(String savePlayStateName) throws UnsupportedOperationException {
		// TODO - Define custom exception in exceptions properties file and pass that
		// string here
		throw new UnsupportedOperationException();
	}

	@Override
	public void update() {
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLost() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLevelCleared() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isReadyForNextLevel() {
		// TODO - implement after corresponding logic has been added on
		// MultiPlayerController
		return false; // TEMP
	}

	@Override
	public boolean isWon() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int placeElement(String elementName, Point2D startCoordinates) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void upgradeElement(int elementId) throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<String, String> getTemplateProperties(String elementName) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Map<String, String>> getAllDefinedTemplateProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getInventory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageView getRepresentationFromSpriteId(int spriteId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Double> getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Double> getResourceEndowments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Map<String, Double>> getElementCosts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Integer> getLevelSprites(int level) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	private Map<String, String> handleAvailableGamesResponse(ServerMessage serverMessage) {
		Map<String, String> availableGamesMap = new HashMap<>();
		if (serverMessage.hasAvailableGames()) {
			Games availableGames = serverMessage.getAvailableGames();
			availableGames.getGamesList().forEach(game -> {
				availableGamesMap.put(game.getName(), game.getDescription());
				System.out.println("Game name: " + game.getName() + "; Game Description: " + game.getDescription());
			});
		}
		return availableGamesMap;
	}

	private String handleGameRoomCreationResponse(ServerMessage serverMessage) {
		String gameRoomId = "";
		if (serverMessage.hasGameRoomCreationStatus()) {
			GameRoomCreationStatus gameRoomCreationStatus = serverMessage.getGameRoomCreationStatus();
			if (!gameRoomCreationStatus.hasError()) {
				gameRoomId = gameRoomCreationStatus.getRoomId();
				System.out.println("Setting gameRoomId to " + gameRoomId);
			} else {
				// TODO - throw exception to be handled by front end?
				System.out.println("Error creating game room: " + gameRoomCreationStatus.getError());
				throw new IllegalArgumentException(gameRoomCreationStatus.getError());
			}
		}
		return gameRoomId;
	}

	private void handleGameRoomJoinResponse(ServerMessage serverMessage) {
		if (serverMessage.hasGameRoomJoinStatus()) {
			GameRoomJoinStatus gameRoomJoinStatus = serverMessage.getGameRoomJoinStatus();
			if (gameRoomJoinStatus.hasError()) {
				// TODO - throw exception to be handled by front end?
				System.out.println("Error joining game room: " + gameRoomJoinStatus.getError());
				throw new IllegalArgumentException(gameRoomJoinStatus.getError());
			}
		}
	}

	private LevelInitialized handleLevelInitializedResponse(ServerMessage serverMessage) {
		if (serverMessage.hasGameRoomLaunchStatus()) {
			GameRoomLaunchStatus gameRoomLaunchStatus = serverMessage.getGameRoomLaunchStatus();
			if (gameRoomLaunchStatus.hasError()) {
				// TODO - throw exception to be handled by front end?
				System.out.println("Error initializing level: " + gameRoomLaunchStatus.getError());
				throw new IllegalArgumentException(gameRoomLaunchStatus.getError());
			}
			return gameRoomLaunchStatus.getInitialState();
		}
		return LevelInitialized.getDefaultInstance();
	}

	private Set<String> handleGameRoomsResponse(ServerMessage serverMessage) {
		if (serverMessage.hasGameRooms()) {
			return serverMessage.getGameRooms().getRoomNamesList().stream().collect(Collectors.toSet());
		}
		return new HashSet<>();
	}

	private Set<String> handlePlayerNamesResponse(ServerMessage serverMessage) {
		if (serverMessage.hasPlayerNames()) {
			PlayerNames playerNames = serverMessage.getPlayerNames();
			if (playerNames.hasError()) {
				throw new IllegalArgumentException(playerNames.getError());
			}
			return serverMessage.getPlayerNames().getUserNamesList().stream().collect(Collectors.toSet());
		}
		return new HashSet<>();
	}
	
	private LevelInitialized handleLoadOriginalGameStateResponse(ServerMessage serverMessage) {
		if (serverMessage.hasLevelInitialized()) {
			LevelInitialized levelInitialized = serverMessage.getLevelInitialized();
			if (levelInitialized.hasError()) {
				throw new IllegalArgumentException(levelInitialized.getError());
			}
			return levelInitialized;
		}
		return LevelInitialized.getDefaultInstance();
	}

	private void writeRequestBytes(byte[] requestBytes) {
		try {
			outputWriter.writeInt(requestBytes.length);
			outputWriter.write(requestBytes);
		} catch (IOException e) {
			e.printStackTrace();// TEMP
		}
	}

	private ServerMessage readServerResponse() {
		try {
			return ServerMessage.parseFrom(readResponseBytes());
		} catch (InvalidProtocolBufferException e) {
			return ServerMessage.getDefaultInstance(); // empty message
		}
	}

	private byte[] readResponseBytes() {
		int len = 0;
		try {
			if (!socket.isClosed()) {
				len = input.readInt();
				byte[] readBytes = new byte[len];
				input.readFully(readBytes);
				return readBytes;
			}
		} catch (IOException e) {
			e.printStackTrace(); // TEMP
		}
		return new byte[len];
	}

	private synchronized void setupChatSocketAndStreams() {
		try {
			// Make connection and initialize streams
			socket = new Socket(SERVER_ADDRESS, PORT);
			outputWriter = new DataOutputStream(socket.getOutputStream());
			input = new DataInputStream(socket.getInputStream());
		} catch (IOException socketException) {
			socketException.printStackTrace();
		}
	}

	// Test client-server integration
	public static void main(String[] args) {
		MultiPlayerClient testClient = new MultiPlayerClient();
		testClient.getAvailableGames();
		testClient.createGameRoom("abc.voog");
	}

}
