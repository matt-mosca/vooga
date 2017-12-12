package networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.protobuf.InvalidProtocolBufferException;

import engine.AbstractGameModelController;
import javafx.geometry.Point2D;
import networking.protocol.PlayerClient.ClientMessage;
import networking.protocol.PlayerClient.CreateGameRoom;
import networking.protocol.PlayerClient.ExitRoom;
import networking.protocol.PlayerClient.GetAllTemplateProperties;
import networking.protocol.PlayerClient.GetAvailableGames;
import networking.protocol.PlayerClient.GetElementCosts;
import networking.protocol.PlayerClient.GetGameRooms;
import networking.protocol.PlayerClient.GetInventory;
import networking.protocol.PlayerClient.GetLevelElements;
import networking.protocol.PlayerClient.GetNumberOfLevels;
import networking.protocol.PlayerClient.GetPlayerNames;
import networking.protocol.PlayerClient.GetTemplateProperties;
import networking.protocol.PlayerClient.JoinRoom;
import networking.protocol.PlayerClient.LaunchGameRoom;
import networking.protocol.PlayerClient.LoadLevel;
import networking.protocol.PlayerClient.PlaceElement;
import networking.protocol.PlayerServer.ElementCost;
import networking.protocol.PlayerServer.GameRoomCreationStatus;
import networking.protocol.PlayerServer.GameRoomJoinStatus;
import networking.protocol.PlayerServer.GameRoomLaunchStatus;
import networking.protocol.PlayerServer.Games;
import networking.protocol.PlayerServer.LevelInitialized;
import networking.protocol.PlayerServer.NewSprite;
import networking.protocol.PlayerServer.PlayerNames;
import networking.protocol.PlayerServer.ServerMessage;
import networking.protocol.PlayerServer.TemplateProperties;
import networking.protocol.PlayerServer.Update;
import util.io.SerializationUtils;

public abstract class AbstractClient implements AbstractGameModelController {

	// get from some properties file
	private final String SERVER_ADDRESS = "127.0.0.1"; // Change to "152.3.53.39" once uploaded to VM
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream outputWriter;
	private SerializationUtils serializationUtils;

	private Update latestUpdate;
	
	public AbstractClient() {
		setupChatSocketAndStreams();
		serializationUtils = new SerializationUtils();
		System.out.println("Set up chat socket and streams");
		latestUpdate = Update.getDefaultInstance();
	}

	protected abstract int getPort();

	public String createGameRoom(String gameName, String roomName) {
		ClientMessage.Builder clientMessageBuilder = ClientMessage.newBuilder();
		CreateGameRoom gameRoomCreationRequest = CreateGameRoom.newBuilder().setGameName(gameName).setRoomName(roomName)
				.build();
		writeRequestBytes(clientMessageBuilder.setCreateGameRoom(gameRoomCreationRequest).build().toByteArray());
		return handleGameRoomCreationResponse(readServerResponse());
	}

	public void joinGameRoom(String roomName, String userName) {
		JoinRoom gameRoomJoinRequest = JoinRoom.newBuilder().setRoomName(roomName).setUserName(userName).build();
		writeRequestBytes(ClientMessage.newBuilder().setJoinRoom(gameRoomJoinRequest).build().toByteArray());
		handleGameRoomJoinResponse(readServerResponse());
	}

	public void exitGameRoom() {
		writeRequestBytes(ClientMessage.newBuilder().setExitRoom(ExitRoom.newBuilder().getDefaultInstanceForType())
				.build().toByteArray());
	}

	public LevelInitialized launchGameRoom() {
		writeRequestBytes(ClientMessage.newBuilder()
				.setLaunchGameRoom(LaunchGameRoom.newBuilder().getDefaultInstanceForType()).build().toByteArray());
		return handleLevelInitializedResponse(readServerResponse());
	}

	public Set<String> getGameRooms() {
		writeRequestBytes(
				ClientMessage.newBuilder().setGetGameRooms(GetGameRooms.getDefaultInstance()).build().toByteArray());
		return handleGameRoomsResponse(readServerResponse());
	}

	public Set<String> getPlayerNames() {
		writeRequestBytes(ClientMessage.newBuilder()
				.setGetPlayerNames(GetPlayerNames.newBuilder().getDefaultInstanceForType()).build().toByteArray());
		return handlePlayerNamesResponse(readServerResponse());
	}

	/**
	 * Save the current state of the current level a game being played or authored.
	 *
	 * @param fileToSaveTo
	 *            the name to assign to the save file
	 */
	@Override
	public void saveGameState(File fileToSaveTo) throws UnsupportedOperationException {
		// TODO - Define custom exception in exceptions properties file and pass that
		// string here
		throw new UnsupportedOperationException();
	}

	/**
	 * Load the detailed state of the original authored game for a particular level,
	 * including high-level information and elements present.
	 *
	 * @param saveName
	 *            the name used to save the game authoring data
	 * @param level
	 *            the level of the game which should be loaded
	 * @throws IOException
	 *             if the save name does not refer to existing files
	 */
	@Override
	public LevelInitialized loadOriginalGameState(String saveName, int level) throws IOException {
		writeRequestBytes(ClientMessage.newBuilder()
				.setLoadLevel(LoadLevel.newBuilder().setGameName(saveName).setLevel(level)).build().toByteArray());
		return handleLoadOriginalGameStateResponse(readServerResponse());
	}

	@Override
	public Map<String, Object> getTemplateProperties(String elementName) throws IllegalArgumentException {
		writeRequestBytes(ClientMessage.newBuilder()
				.setGetTemplateProperties(GetTemplateProperties.newBuilder().setElementName(elementName).build())
				.build().toByteArray());
		Map<String, String> serializedTemplate = handleAllTemplatePropertiesResponse(readServerResponse()).values()
				.iterator().next();
		return serializationUtils.deserializeElementTemplate(serializedTemplate);
	}

	@Override
	public Map<String, Map<String, Object>> getAllDefinedTemplateProperties() {
		writeRequestBytes(ClientMessage.newBuilder()
				.setGetAllTemplateProperties(GetAllTemplateProperties.getDefaultInstance()).build().toByteArray());
		Map<String, Map<String, String>> serializedTemplates = handleAllTemplatePropertiesResponse(
				readServerResponse());
		return serializationUtils.deserializeTemplates(serializedTemplates);
	}

	@Override
	public NewSprite placeElement(String elementName, Point2D startCoordinates) {
		writeRequestBytes(ClientMessage.newBuilder()
				.setPlaceElement(PlaceElement.newBuilder().setElementName(elementName)
						.setXCoord(startCoordinates.getX()).setYCoord(startCoordinates.getY()).build())
				.build().toByteArray());
		return handlePlaceElementResponse(readServerResponse());
	}

	@Override
	public int getNumLevelsForGame(String gameName, boolean originalGame) {
		writeRequestBytes(ClientMessage.newBuilder()
				.setGetNumLevels(GetNumberOfLevels.newBuilder().setGameName(gameName).setOriginalGame(originalGame))
				.build().toByteArray());
		return handleNumLevelsForGameResponse(readServerResponse());
	}

	@Override
	public Set<String> getInventory() {
		writeRequestBytes(
				ClientMessage.newBuilder().setGetInventory(GetInventory.getDefaultInstance()).build().toByteArray());
		return handleInventoryResponse(readServerResponse());
	}

	// TODO - Deprecate? Doesn't seem to be used anywhere?
	@Deprecated
	@Override
	public Map<String, Double> getStatus() {
		return new HashMap<>();
	}

	@Override
	public Map<String, Map<String, Double>> getElementCosts() {
		writeRequestBytes(ClientMessage.newBuilder().setGetElementCosts(GetElementCosts.getDefaultInstance()).build()
				.toByteArray());
		return handleElementCostsResponse(readServerResponse());
	}

	/**
	 * Get the elements of a game (represented as sprites) for a particular level.
	 *
	 * TODO - custom exception?
	 *
	 * @param level
	 *            the level of the game which should be loaded
	 * @return all the game elements (sprites) represented in the level
	 * @throws IllegalArgumentException
	 *             if there is no corresponding level in the current game
	 */
	@Override
	public Collection<NewSprite> getLevelSprites(int level) throws IllegalArgumentException {
		writeRequestBytes(ClientMessage.newBuilder()
				.setGetLevelElements(GetLevelElements.newBuilder().setLevel(level).build()).build().toByteArray());
		return handleLevelSpritesResponse(readServerResponse());
	}

	/**
	 * Fetch all available game names and their corresponding descriptions
	 * 
	 * @return map where keys are game names and values are game descriptions
	 */
	public Map<String, String> getAvailableGames() {
		ClientMessage.Builder clientMessageBuilder = ClientMessage.newBuilder();
		writeRequestBytes(clientMessageBuilder.setGetAvailableGames(GetAvailableGames.newBuilder().build()).build()
				.toByteArray());
		return handleAvailableGamesResponse(readServerResponse());
	}

	protected void writeRequestBytes(byte[] requestBytes) {
		try {
			outputWriter.writeInt(requestBytes.length);
			outputWriter.write(requestBytes);
		} catch (IOException e) {
			e.printStackTrace();// TEMP
		}
	}

	protected ServerMessage readServerResponse() {
		try {
			return ServerMessage.parseFrom(readResponseBytes());
		} catch (InvalidProtocolBufferException e) {
			return ServerMessage.getDefaultInstance(); // empty message
		}
	}

	protected byte[] readResponseBytes() {
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

	protected SerializationUtils getSerializationUtils() {
		return serializationUtils;
	}

	protected Update getLatestUpdate() {
		return latestUpdate;
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

	private Map<String, String> handleAvailableGamesResponse(ServerMessage serverMessage) {
		Map<String, String> availableGamesMap = new HashMap<>();
		if (serverMessage.hasAvailableGames()) {
			Games availableGames = serverMessage.getAvailableGames();
			availableGames.getGamesList().forEach(game -> {
				availableGamesMap.put(game.getName(), game.getDescription());
			});
		}
		return availableGamesMap;
	}

	private Map<String, Map<String, String>> handleAllTemplatePropertiesResponse(ServerMessage serverMessage) {
		Map<String, Map<String, String>> allTemplatePropertiesMap = new HashMap<>();
		serverMessage.getTemplatePropertiesList().forEach(templateProperties -> allTemplatePropertiesMap
				.put(templateProperties.getElementName(), handleTemplatePropertiesResponse(templateProperties)));
		return allTemplatePropertiesMap;
	}

	private NewSprite handlePlaceElementResponse(ServerMessage serverMessage) {
		if (serverMessage.hasElementPlaced()) {
			return serverMessage.getElementPlaced();
		}
		return NewSprite.getDefaultInstance(); // Should be careful not to interpret as having spriteId = 0
	}

	private int handleNumLevelsForGameResponse(ServerMessage serverMessage) {
		if (serverMessage.hasNumLevels()) {
			return serverMessage.getNumLevels().getNumLevels();
		}
		return 0;
	}

	private Set<String> handleInventoryResponse(ServerMessage serverMessage) {
		if (serverMessage.hasInventory()) {
			return serverMessage.getInventory().getTemplatesList().stream().collect(Collectors.toSet());
		}
		return new HashSet<>();
	}

	private Map<String, Map<String, Double>> handleElementCostsResponse(ServerMessage serverMessage) {
		Map<String, Map<String, Double>> allElementCostsMap = new HashMap<>();
		serverMessage.getElementCostsList().stream().forEach(elementCosts -> allElementCostsMap
				.put(elementCosts.getElementName(), handleElementCosts(elementCosts)));
		return allElementCostsMap;
	}

	private Collection<NewSprite> handleLevelSpritesResponse(ServerMessage serverMessage) {
		System.out.print("No. of level sprites: " + serverMessage.getLevelSpritesCount());
		return serverMessage.getLevelSpritesList();
	}

	private Map<String, Double> handleElementCosts(ElementCost elementCost) {
		Map<String, Double> elementCosts = new HashMap<>();
		elementCost.getCostsList().stream()
				.forEach(resource -> elementCosts.put(resource.getName(), resource.getAmount()));
		return elementCosts;
	}

	private Map<String, String> handleTemplatePropertiesResponse(TemplateProperties templateProperties) {
		Map<String, String> templatePropertiesMap = new HashMap<>();
		templateProperties.getPropertyList().forEach(
				templateProperty -> templatePropertiesMap.put(templateProperty.getName(), templateProperty.getValue()));
		return templatePropertiesMap;
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

	private String handleGameRoomCreationResponse(ServerMessage serverMessage) {
		String gameRoomId = "";
		if (serverMessage.hasGameRoomCreationStatus()) {
			GameRoomCreationStatus gameRoomCreationStatus = serverMessage.getGameRoomCreationStatus();
			if (!gameRoomCreationStatus.hasError()) {
				gameRoomId = gameRoomCreationStatus.getRoomId();
			} else {
				// TODO - throw exception to be handled by front end?
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
				throw new IllegalArgumentException(gameRoomJoinStatus.getError());
			}
		}
	}

	private LevelInitialized handleLevelInitializedResponse(ServerMessage serverMessage) {
		if (serverMessage.hasGameRoomLaunchStatus()) {
			GameRoomLaunchStatus gameRoomLaunchStatus = serverMessage.getGameRoomLaunchStatus();
			if (gameRoomLaunchStatus.hasError()) {
				// TODO - throw exception to be handled by front end?
				throw new IllegalArgumentException(gameRoomLaunchStatus.getError());
			}
			latestUpdate = gameRoomLaunchStatus.getInitialState().getSpritesAndStatus();
			return gameRoomLaunchStatus.getInitialState();
		}
		return LevelInitialized.getDefaultInstance();
	}

	private synchronized void setupChatSocketAndStreams() {
		try {
			// Make connection and initialize streams
			socket = new Socket(SERVER_ADDRESS, getPort());
			outputWriter = new DataOutputStream(socket.getOutputStream());
			input = new DataInputStream(socket.getInputStream());
		} catch (IOException socketException) {
			socketException.printStackTrace();
		}
	}

}
