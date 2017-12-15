package networking;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.protobuf.InvalidProtocolBufferException;

import engine.play_engine.PlayController;
import networking.protocol.PlayerClient.ClientMessage;
import networking.protocol.PlayerClient.LoadLevel;
import networking.protocol.PlayerServer.LevelInitialized;
import networking.protocol.PlayerServer.NumberOfLevels;
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
class MultiPlayerController extends AbstractServerController {

	private final String LOAD_LEVEL_ERROR_NOT_READY = "Your peers are not yet ready to load this level";

	private Map<String, PlayController> roomsToEngines = new HashMap<>();

	@Override
	public byte[] handleSpecificRequestAndSerializeResponse(int clientId, byte[] requestBytes) {
		try {
			return handlePlayRequestAndSerializeResponse(clientId, ClientMessage.parseFrom(requestBytes),
					ServerMessage.newBuilder());			
		} catch (InvalidProtocolBufferException | ReflectiveOperationException e) {
			return new byte[] {};
		}
	}

	@Override
	protected PlayController getEngineForRoom(String room) {
		return roomsToEngines.get(room);
	}

	@Override
	protected void createEngineForRoom(String room) {
		roomsToEngines.put(room, new PlayController());
	}

	// Try refactoring / replacing following 4 methods using Reflection instead
	private byte[] handlePlayRequestAndSerializeResponse(int clientId, ClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) throws ReflectiveOperationException {
		if (clientMessage.hasPerformUpdate()) {
			return handleUpdate(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasPauseGame()) {
			return handlePauseGame(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasResumeGame()) {
			return handleResumeGame(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasGetInventory()) {
			return getInventory(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasGetTemplateProperties()) {
			return getTemplateProperties(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasGetAllTemplateProperties()) {
			return getAllTemplateProperties(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasGetElementCosts()) {
			return getElementCosts(clientId, clientMessage, serverMessageBuilder);
		}
		return handleLateGameRequestAndSerializeResponse(clientId, clientMessage, serverMessageBuilder);
	}

	private byte[] handleLateGameRequestAndSerializeResponse(int clientId, ClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) throws ReflectiveOperationException {
		if (clientMessage.hasUpgradeElement()) {
			return upgradeElement(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasCheckReadyForNextLevel()) {
			return checkReadyForNextLevel(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasLoadLevel()) {
			return loadLevel(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasGetLevelElements()) {
			return getLevelElements(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasGetNumLevels()) {
			return getNumberOfLevels(clientId, clientMessage, serverMessageBuilder);
		}
		return ServerMessage.getDefaultInstance().toByteArray();
	}

	// TODO - Consider server push instead of client pull? Would be more complicated
	// but more accurate / realistic, and fewer packets exchanged
	private byte[] handleUpdate(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		PlayController playController = getEngineForClient(clientId);
		if (clientIsFirstMemberOfGameRoom(clientId)) {
			// only do actual update if primary client, simply send state for the rest
			playController.update();
		}
		return serverMessageBuilder.setUpdate(playController.getLatestUpdate()).build().toByteArray();
	}

	private byte[] handlePauseGame(int clientId, ClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) {
		// Verify clientId, retrieve appropriate game room / controller
		PlayController playController = getEngineForClient(clientId);
		playController.pause();
		return serverMessageBuilder.setUpdate(playController.packageStatusUpdate()).build().toByteArray();
	}

	private byte[] handleResumeGame(int clientId, ClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) {
		PlayController playController = getEngineForClient(clientId);
		playController.resume();
		return serverMessageBuilder.setUpdate(playController.packageStatusUpdate()).build().toByteArray();
	}

	private byte[] getInventory(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		return serverMessageBuilder.setInventory(getEngineForClient(clientId).packageInventory()).build().toByteArray();
	}

	private byte[] getTemplateProperties(int clientId, ClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) {
		return serverMessageBuilder
				.addTemplateProperties(getEngineForClient(clientId)
						.packageTemplateProperties(clientMessage.getGetTemplateProperties().getElementName()))
				.build().toByteArray();
	}

	private byte[] getAllTemplateProperties(int clientId, ClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) {
		return serverMessageBuilder
				.addAllTemplateProperties(getEngineForClient(clientId).packageAllTemplateProperties()).build()
				.toByteArray();
	}

	private byte[] getElementCosts(int clientId, ClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) {
		return serverMessageBuilder.addAllElementCosts(getEngineForClient(clientId).packageAllElementCosts()).build()
				.toByteArray();
	}

	private byte[] upgradeElement(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder)
			throws ReflectiveOperationException {
		PlayController playController = getEngineForClient(clientId);
		playController.upgradeElement(clientMessage.getUpgradeElement().getSpriteId());
		return serverMessageBuilder.build().toByteArray();
	}

	private byte[] checkReadyForNextLevel(int clientId, ClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) {
		return serverMessageBuilder
				.setReadyForNextLevel(ReadyForNextLevel.newBuilder()
						.setIsReady(joinAndCheckIfWaitingRoomIsFull(getGameRoomNameOfClient(clientId))).build())
				.build().toByteArray();
	}

	private byte[] loadLevel(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		LevelInitialized.Builder levelInitializationBuilder = LevelInitialized.newBuilder();
		LoadLevel loadLevelRequest = clientMessage.getLoadLevel();
		String roomName = getGameRoomNameOfClient(clientId);
		int levelToLoad = loadLevelRequest.getLevel();
		if (!checkIfWaitingRoomIsFull(roomName)) {
			// not ready to load
			return serverMessageBuilder
					.setLevelInitialized(levelInitializationBuilder.setError(LOAD_LEVEL_ERROR_NOT_READY).build())
					.build().toByteArray();
		}
		String gameName = retrieveGameNameFromRoomName(roomName);
		try {
			return serverMessageBuilder
					.setLevelInitialized(getEngineForClient(clientId).loadOriginalGameState(gameName, levelToLoad))
					.build().toByteArray();
		} catch (IOException e) {
			return serverMessageBuilder.setLevelInitialized(LevelInitialized.getDefaultInstance()).build()
					.toByteArray();
		}
	}

	private byte[] getLevelElements(int clientId, ClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) {
		return serverMessageBuilder
				.addAllLevelSprites(
						getEngineForClient(clientId).getLevelSprites(clientMessage.getGetLevelElements().getLevel()))
				.build().toByteArray();
	}

	private byte[] getNumberOfLevels(int clientId, ClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) {
		return serverMessageBuilder
				.setNumLevels(NumberOfLevels.newBuilder()
						.setNumLevels(getEngineForClient(clientId).getNumLevelsForGame()).build())
				.build().toByteArray();
	}

	private boolean joinAndCheckIfWaitingRoomIsFull(String roomName) {
		Map<String, Integer> waitingRoom = getWaitingRoom();
		waitingRoom.put(roomName, waitingRoom.getOrDefault(roomName, 0) + 1);
		return checkIfWaitingRoomIsFull(roomName);
	}

	private boolean checkIfWaitingRoomIsFull(String roomName) {
		return getWaitingRoom().get(roomName) < getRoomSize(roomName);
	}

	@Override
	protected PlayController getEngineForClient(int clientId) {
		return getEngineForRoom(getGameRoomNameOfClient(clientId));
	}

}