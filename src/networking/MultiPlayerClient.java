package networking;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import engine.PlayModelController;
import networking.protocol.PlayerClient.CheckReadyForNextLevel;
import networking.protocol.PlayerClient.ClientMessage;
import networking.protocol.PlayerClient.CreateGameRoom;
import networking.protocol.PlayerClient.GetGameRooms;
import networking.protocol.PlayerClient.GetPlayerNames;
import networking.protocol.PlayerClient.JoinRoom;
import networking.protocol.PlayerClient.LaunchGameRoom;
import networking.protocol.PlayerClient.PauseGame;
import networking.protocol.PlayerClient.PerformUpdate;
import networking.protocol.PlayerClient.ResumeGame;
import networking.protocol.PlayerClient.UpgradeElement;
import networking.protocol.PlayerServer.GameRoomCreationStatus;
import networking.protocol.PlayerServer.GameRoomJoinStatus;
import networking.protocol.PlayerServer.GameRoomLaunchStatus;
import networking.protocol.PlayerServer.LevelInitialized;
import networking.protocol.PlayerServer.PlayerNames;
import networking.protocol.PlayerServer.ResourceUpdate;
import networking.protocol.PlayerServer.ServerMessage;
import networking.protocol.PlayerServer.StatusUpdate;
import networking.protocol.PlayerServer.Update;

/**
 * Gateway of player in multi-player game to remote back-end data and logic
 * Provides abstraction of a local controller / back-end to the player front-end
 * by providing the same interface
 * 
 * @author radithya
 *
 */
public class MultiPlayerClient extends AbstractClient implements PlayModelController { // Is this weird?

	private Update latestUpdate;
	private final int PORT = 9042;

	// Game client state (keeping track of which multi-player game it is in, etc)

	public MultiPlayerClient() {
		super();
		latestUpdate = Update.getDefaultInstance();
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
	public int getCurrentLevel() {
		return getLatestStatusUpdate().getCurrentLevel();
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
	public void upgradeElement(int elementId) throws IllegalArgumentException {
		writeRequestBytes(ClientMessage.newBuilder()
				.setUpgradeElement(UpgradeElement.newBuilder().setSpriteId(elementId).build()).build().toByteArray());
		// This request doesn't care about response
	}
	
	@Override
	protected int getPort() {
		return PORT;
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

	private Update handleUpdateResponse(ServerMessage serverMessage) {
		latestUpdate = getUpdate(serverMessage);
		return latestUpdate;
	}

	private boolean handleCheckReadyResponse(ServerMessage serverMessage) {
		if (serverMessage.hasReadyForNextLevel()) {
			return serverMessage.getReadyForNextLevel().getIsReady();
		}
		return false;
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

	// Test client-server integration
	public static void main(String[] args) {
		MultiPlayerClient testClient = new MultiPlayerClient();
		testClient.getAvailableGames();
		testClient.createGameRoom("abc.voog");
	}

}
