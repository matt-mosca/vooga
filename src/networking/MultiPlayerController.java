package networking;

import java.io.IOException;
import java.util.Map;

import engine.play_engine.PlayController;
import javafx.geometry.Point2D;
import networking.protocol.PlayerClient.DeleteElement;
import networking.protocol.PlayerClient.ClientMessage;
import networking.protocol.PlayerClient.LoadLevel;
import networking.protocol.PlayerClient.MoveElement;
import networking.protocol.PlayerClient.PlaceElement;
import networking.protocol.PlayerServer.LevelInitialized;
import networking.protocol.PlayerServer.NewSprite;
import networking.protocol.PlayerServer.Notification;
import networking.protocol.PlayerServer.NumberOfLevels;
import networking.protocol.PlayerServer.ReadyForNextLevel;
import networking.protocol.PlayerServer.ServerMessage;
import networking.protocol.PlayerServer.SpriteDeletion;
import networking.protocol.PlayerServer.SpriteUpdate;

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

	// TODO - Consider server push instead of client pull? Would be more complicated
	// but more accurate / realistic, and fewer packets exchanged
	byte[] handleUpdate(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		PlayController playController = getPlayEngineForClient(clientId);
		if (clientIsFirstMemberOfGameRoom(clientId)) {
			// only do actual update if primary client, simply send state for the rest
			playController.update();
		}
		return serverMessageBuilder.setUpdate(playController.getLatestUpdate()).build().toByteArray();
	}

	byte[] handlePauseGame(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		// Verify clientId, retrieve appropriate game room / controller
		PlayController playController = getPlayEngineForClient(clientId);
		playController.pause();
		return serverMessageBuilder.setUpdate(playController.packageStatusUpdate()).build().toByteArray();
	}

	byte[] handleResumeGame(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		PlayController playController = getPlayEngineForClient(clientId);
		playController.resume();
		return serverMessageBuilder.setUpdate(playController.packageStatusUpdate()).build().toByteArray();
	}

	byte[] getInventory(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		return serverMessageBuilder.setInventory(getPlayEngineForClient(clientId).packageInventory()).build()
				.toByteArray();
	}

	byte[] getTemplateProperties(int clientId, ClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) {
		return serverMessageBuilder
				.addTemplateProperties(getPlayEngineForClient(clientId)
						.packageTemplateProperties(clientMessage.getGetTemplateProperties().getElementName()))
				.build().toByteArray();
	}

	byte[] getAllTemplateProperties(int clientId, ClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) {
		return serverMessageBuilder
				.addAllTemplateProperties(getPlayEngineForClient(clientId).packageAllTemplateProperties()).build()
				.toByteArray();
	}

	byte[] getElementCosts(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		return serverMessageBuilder.addAllElementCosts(getPlayEngineForClient(clientId).packageAllElementCosts())
				.build().toByteArray();
	}

	byte[] placeElement(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder)
			throws ReflectiveOperationException {
		PlayController playController = getPlayEngineForClient(clientId);
		PlaceElement placeElementRequest = clientMessage.getPlaceElement();
		NewSprite placedElement = playController.placeElement(placeElementRequest.getElementName(),
				new Point2D(placeElementRequest.getXCoord(), placeElementRequest.getYCoord()));
		// Broadcast
		enqueueMessage(ServerMessage.newBuilder()
				.setNotification(Notification.newBuilder().setElementPlaced(placedElement).build()).build());
		return serverMessageBuilder.setElementPlaced(placedElement).build().toByteArray();
	}

	byte[] moveElement(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		PlayController playController = getPlayEngineForClient(clientId);
		MoveElement moveElementRequest = clientMessage.getMoveElement();
		SpriteUpdate updatedSprite = playController.moveElement(moveElementRequest.getElementId(),
				moveElementRequest.getNewXCoord(), moveElementRequest.getNewYCoord());
		// Broadcast
		enqueueMessage(ServerMessage.newBuilder()
				.setNotification(Notification.newBuilder().setElementMoved(updatedSprite).build()).build());
		return serverMessageBuilder.setElementMoved(updatedSprite).build().toByteArray();
	}

	byte[] deleteElement(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		PlayController playController = getPlayEngineForClient(clientId);
		DeleteElement deleteElementRequest = clientMessage.getDeleteElement();
		try {
			SpriteDeletion deletedElement = playController.deleteElement(deleteElementRequest.getElementId());
			enqueueMessage(ServerMessage.newBuilder()
					.setNotification(Notification.newBuilder().setElementDeleted(deletedElement).build()).build());
			return serverMessageBuilder.setElementDeleted(deletedElement).build().toByteArray();
		} catch (IllegalArgumentException e) {
			return serverMessageBuilder.setError(e.getMessage()).build().toByteArray();
		}
	}

	byte[] upgradeElement(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder)
			throws ReflectiveOperationException {
		PlayController playController = getPlayEngineForClient(clientId);
		playController.upgradeElement(clientMessage.getUpgradeElement().getSpriteId());
		return serverMessageBuilder.build().toByteArray();
	}

	byte[] checkReadyForNextLevel(int clientId, ClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) {
		return serverMessageBuilder
				.setReadyForNextLevel(ReadyForNextLevel.newBuilder()
						.setIsReady(joinAndCheckIfWaitingRoomIsFull(getGameRoomNameOfClient(clientId))).build())
				.build().toByteArray();
	}

	byte[] loadLevel(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
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
					.setLevelInitialized(getPlayEngineForClient(clientId).loadOriginalGameState(gameName, levelToLoad))
					.build().toByteArray();
		} catch (IOException e) {
			return serverMessageBuilder.setLevelInitialized(LevelInitialized.getDefaultInstance()).build()
					.toByteArray();
		}
	}

	byte[] getLevelElements(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		return serverMessageBuilder.addAllLevelSprites(
				getPlayEngineForClient(clientId).getLevelSprites(clientMessage.getGetLevelElements().getLevel()))
				.build().toByteArray();
	}

	byte[] getNumberOfLevels(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		return serverMessageBuilder
				.setNumLevels(NumberOfLevels.newBuilder()
						.setNumLevels(getPlayEngineForClient(clientId).getNumLevelsForGame()).build())
				.build().toByteArray();
	}

	@Override
	public byte[] handleRequestAndSerializeResponse(int clientId, byte[] requestBytes) {
		try {
			byte[] pregameResponseBytes = super.handleRequestAndSerializeResponse(clientId, requestBytes);
			if (pregameResponseBytes.length > 0) {
				return pregameResponseBytes;
			}
			return handlePlayRequestAndSerializeResponse(clientId, ClientMessage.parseFrom(requestBytes),
					ServerMessage.newBuilder());
		} catch (IOException | ReflectiveOperationException e) {
			return new byte[] {}; // TEMP - Should create a generic error message
		}
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
		if (clientMessage.hasPlaceElement()) {
			return placeElement(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasMoveElement()) {
			return moveElement(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasUpgradeElement()) {
			return upgradeElement(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasDeleteElement()) {
			return deleteElement(clientId, clientMessage, serverMessageBuilder);
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

	private boolean joinAndCheckIfWaitingRoomIsFull(String roomName) {
		Map<String, Integer> waitingRoom = getWaitingRoom();
		waitingRoom.put(roomName, waitingRoom.getOrDefault(roomName, 0) + 1);
		return checkIfWaitingRoomIsFull(roomName);
	}

	private boolean checkIfWaitingRoomIsFull(String roomName) {
		return getWaitingRoom().get(roomName) < getRoomSize(roomName);
	}

}