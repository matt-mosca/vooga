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

import engine.PlayModelController;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import networking.protocol.PlayerClient.CheckReadyForNextLevel;
import networking.protocol.PlayerClient.ClientMessage;
import networking.protocol.PlayerClient.CreateGameRoom;
import networking.protocol.PlayerClient.GetAllTemplateProperties;
import networking.protocol.PlayerClient.GetAvailableGames;
import networking.protocol.PlayerClient.GetElementCosts;
import networking.protocol.PlayerClient.GetGameRooms;
import networking.protocol.PlayerClient.GetInventory;
import networking.protocol.PlayerClient.GetLevelElements;
import networking.protocol.PlayerClient.GetPlayerNames;
import networking.protocol.PlayerClient.GetTemplateProperties;
import networking.protocol.PlayerClient.JoinRoom;
import networking.protocol.PlayerClient.LaunchGameRoom;
import networking.protocol.PlayerClient.LoadLevel;
import networking.protocol.PlayerClient.PauseGame;
import networking.protocol.PlayerClient.PerformUpdate;
import networking.protocol.PlayerClient.PlaceElement;
import networking.protocol.PlayerClient.ResumeGame;
import networking.protocol.PlayerClient.UpgradeElement;
import networking.protocol.PlayerServer.ElementCost;
import networking.protocol.PlayerServer.GameRoomCreationStatus;
import networking.protocol.PlayerServer.GameRoomJoinStatus;
import networking.protocol.PlayerServer.GameRoomLaunchStatus;
import networking.protocol.PlayerServer.Games;
import networking.protocol.PlayerServer.LevelInitialized;
import networking.protocol.PlayerServer.NewSprite;
import networking.protocol.PlayerServer.PlayerNames;
import networking.protocol.PlayerServer.ResourceUpdate;
import networking.protocol.PlayerServer.ServerMessage;
import networking.protocol.PlayerServer.StatusUpdate;
import networking.protocol.PlayerServer.TemplateProperties;
import networking.protocol.PlayerServer.Update;
import util.io.SerializationUtils;

/**
 * Gateway of player in multi-player game to remote back-end data and logic
 * Provides abstraction of a local controller / back-end to the player front-end
 * by providing the same interface
 * 
 * @author radithya
 *
 */
public class MultiPlayerClient implements PlayModelController { // Is this weird?

	// get from some properties file
	private final String SERVER_ADDRESS = "127.0.0.1"; // Change to "152.3.53.39" once uploaded to VM
	private final int PORT = 9041;
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream outputWriter;
	private SerializationUtils serializationUtils = new SerializationUtils();

	private Update latestUpdate;

	// Game client state (keeping track of which multi-player game it is in, etc)

	public MultiPlayerClient() {
		setupChatSocketAndStreams();
		latestUpdate = Update.getDefaultInstance();
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
	public LevelInitialized loadOriginalGameState(String saveName, int level) throws IOException {
		writeRequestBytes(ClientMessage.newBuilder()
				.setLoadLevel(LoadLevel.newBuilder().setGameName(saveName).setLevel(level)).build().toByteArray());
		return handleLoadOriginalGameStateResponse(readServerResponse());
	}

	// Since saving is not allowed, this won't be allowed either
	@Override
	public LevelInitialized loadSavedPlayState(String savePlayStateName) throws UnsupportedOperationException {
		// TODO - Define custom exception in exceptions properties file and pass that
		// string here
		throw new UnsupportedOperationException();
	}

	@Override
	public Update update() {
		writeRequestBytes(
				ClientMessage.newBuilder().setPerformUpdate(PerformUpdate.getDefaultInstance()).build().toByteArray());
		return handleUpdateResponse(readServerResponse());
	}

	@Override
	public void pause() {
		writeRequestBytes(
				ClientMessage.newBuilder().setPauseGame(PauseGame.getDefaultInstance()).build().toByteArray());
		handleUpdateResponse(readServerResponse());
	}

	@Override
	public void resume() {
		writeRequestBytes(
				ClientMessage.newBuilder().setResumeGame(ResumeGame.getDefaultInstance()).build().toByteArray());
		handleUpdateResponse(readServerResponse());
	}

	@Override
	public boolean isLost() {
		return getLatestStatusUpdate().getIsLost();
	}

	@Override
	public boolean isLevelCleared() {
		return getLatestStatusUpdate().getLevelCleared();
	}

	@Override
	public boolean isReadyForNextLevel() {
		writeRequestBytes(ClientMessage.newBuilder()
				.setCheckReadyForNextLevel(CheckReadyForNextLevel.getDefaultInstance()).build().toByteArray());
		return handleCheckReadyResponse(readServerResponse());
	}

	@Override
	public boolean isWon() {
		return getLatestStatusUpdate().getIsWon();
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
	public void upgradeElement(int elementId) throws IllegalArgumentException {
		writeRequestBytes(ClientMessage.newBuilder()
				.setUpgradeElement(UpgradeElement.newBuilder().setSpriteId(elementId).build()).build().toByteArray());
		// This request doesn't care about response
	}

	@Override
	public Map<String, Object> getTemplateProperties(String elementName) throws IllegalArgumentException {
		writeRequestBytes(ClientMessage.newBuilder()
				.setGetTemplateProperties(GetTemplateProperties.newBuilder().setElementName(elementName).build())
				.build().toByteArray());
		Map<String, String> templateSerialization =  handleAllTemplatePropertiesResponse(readServerResponse()).values()
				.iterator().next();
		Map<String, Object> template = serializationUtils.deserializeElementTemplate(templateSerialization);
		return template;
	}

	@Override
	public Map<String, Map<String, Object>> getAllDefinedTemplateProperties() {
		writeRequestBytes(ClientMessage.newBuilder()
				.setGetAllTemplateProperties(GetAllTemplateProperties.getDefaultInstance()).build().toByteArray());
		Map<String, Map<String, String>> templateSerializations = handleAllTemplatePropertiesResponse(readServerResponse());
		Map<String, Map<String, Object>> templates = new HashMap<>();
		// TODO - i'm pretty sure i've done Map<String, Map<String, String>> -> Map<String, Map<String, Object>>
		// multiple times so we should make a method for it in SUtils
		for (String templateName : templateSerializations.keySet()) {
			templates.put(templateName,
					serializationUtils.deserializeElementTemplate(templateSerializations.get(templateName)));
		}
		return templates;
	}

	// TODO - Need to wrap this within LevelInitialized method
	@Override 
	public int getCurrentLevel() {
		return 1;//TEMP
	}
	
	// TODO - Need to wrap / implement
	@Override
	public int getNumLevelsForGame(String gameName, boolean originalGame) {
		return 1;// TEMP
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
	public Map<String, Double> getResourceEndowments() {
		ResourceUpdate resourceUpdate = latestUpdate.hasResourceUpdates() ? latestUpdate.getResourceUpdates()
				: ResourceUpdate.getDefaultInstance();
		Map<String, Double> resourcesMap = new HashMap<>();
		resourceUpdate.getResourcesList()
				.forEach(resource -> resourcesMap.put(resource.getName(), resource.getAmount()));
		return resourcesMap;
	}

	@Override
	public Map<String, Map<String, Double>> getElementCosts() {
		writeRequestBytes(ClientMessage.newBuilder().setGetElementCosts(GetElementCosts.getDefaultInstance()).build()
				.toByteArray());
		return handleElementCostsResponse(readServerResponse());
	}

	// TODO - Will be modified in interface to return Collection<NewSprite>
	// (NewSprite is a message type)
	@Override
	public Collection<Integer> getLevelSprites(int level) throws IllegalArgumentException {
		writeRequestBytes(ClientMessage.newBuilder()
				.setGetLevelElements(GetLevelElements.newBuilder().setLevel(level).build()).build().toByteArray());
		// Replace following line by the commented one after when front end is ready
		return handleLevelSpritesResponse(readServerResponse()).stream().map(newSprite -> newSprite.getSpriteId())
				.collect(Collectors.toList());
		// return handleLevelSpritesResponse(readServerResponse());
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

	private Update handleUpdateResponse(ServerMessage serverMessage) {
		return getUpdate(serverMessage);
	}

	private boolean handleCheckReadyResponse(ServerMessage serverMessage) {
		if (serverMessage.hasReadyForNextLevel()) {
			return serverMessage.getReadyForNextLevel().getIsReady();
		}
		return false;
	}

	private NewSprite handlePlaceElementResponse(ServerMessage serverMessage) {
		if (serverMessage.hasElementPlaced()) {
			return serverMessage.getElementPlaced();
		}
		return NewSprite.getDefaultInstance(); // Should be careful not to interpret as having spriteId = 0
	}

	private Map<String, String> handleTemplatePropertiesResponse(TemplateProperties templateProperties) {
		Map<String, String> templatePropertiesMap = new HashMap<>();
		templateProperties.getPropertyList().forEach(
				templateProperty -> templatePropertiesMap.put(templateProperty.getName(), templateProperty.getValue()));
		return templatePropertiesMap;
	}

	private Map<String, Map<String, String>> handleAllTemplatePropertiesResponse(ServerMessage serverMessage) {
		Map<String, Map<String, String>> allTemplatePropertiesMap = new HashMap<>();
		serverMessage.getTemplatePropertiesList().forEach(templateProperties -> allTemplatePropertiesMap
				.put(templateProperties.getElementName(), handleTemplatePropertiesResponse(templateProperties)));
		return allTemplatePropertiesMap;
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

	private Map<String, Double> handleElementCosts(ElementCost elementCost) {
		Map<String, Double> elementCosts = new HashMap<>();
		elementCost.getCostsList().stream()
				.forEach(resource -> elementCosts.put(resource.getName(), resource.getAmount()));
		return elementCosts;
	}

	private Collection<NewSprite> handleLevelSpritesResponse(ServerMessage serverMessage) {
		return serverMessage.getLevelSpritesList();
	}

	private Update getUpdate(ServerMessage serverMessage) {
		if (serverMessage.hasUpdate()) {
			return serverMessage.getUpdate();
		}
		return Update.getDefaultInstance();
	}

	private StatusUpdate getLatestStatusUpdate() {
		return latestUpdate.hasStatusUpdates() ? latestUpdate.getStatusUpdates() : StatusUpdate.getDefaultInstance();
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
