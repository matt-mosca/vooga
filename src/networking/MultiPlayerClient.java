package networking;

import java.util.HashMap;
import java.util.Map;

import engine.PlayModelController;
import networking.protocol.PlayerClient.CheckReadyForNextLevel;
import networking.protocol.PlayerClient.ClientMessage;
import networking.protocol.PlayerClient.PauseGame;
import networking.protocol.PlayerClient.PerformUpdate;
import networking.protocol.PlayerClient.ResumeGame;
import networking.protocol.PlayerClient.UpgradeElement;
import networking.protocol.PlayerServer.LevelInitialized;
import networking.protocol.PlayerServer.ResourceUpdate;
import networking.protocol.PlayerServer.ServerMessage;
import networking.protocol.PlayerServer.StatusUpdate;
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
public class MultiPlayerClient extends AbstractClient implements PlayModelController { // Is this weird?

	private Update latestUpdate;
	private final int PORT = 9042;

	// Game client state (keeping track of which multi-player game it is in, etc)

	public MultiPlayerClient() {
		super();
		latestUpdate = Update.getDefaultInstance();
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