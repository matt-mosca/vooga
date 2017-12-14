package networking;

import java.util.HashMap;
import java.util.Map;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;

import engine.authoring_engine.AuthoringController;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import networking.protocol.AuthorClient.AuthoringClientMessage;
import networking.protocol.AuthorServer.AuthoringNotification;
import networking.protocol.AuthorServer.AuthoringServerMessage;
import networking.protocol.PlayerServer.ServerMessage;

public class CollaborativeAuthoringController extends AbstractServerController {

	private Map<String, AuthoringController> roomsToEngines = new HashMap<>();

	private ObservableList<AuthoringServerMessage> messageQueue = FXCollections.observableArrayList();

	public byte[] handleRequestAndSerializeResponse(int clientId, byte[] requestBytes)
			throws InvalidProtocolBufferException {
		byte[] pregameRequestBytes = super.handleRequestAndSerializeResponse(clientId, requestBytes);
		if (pregameRequestBytes.length > 0) {
			return pregameRequestBytes;
		}
		// other ones
		return new byte[] {};
	}

	@Override
	public void registerNotificationStreamListener(ListChangeListener<Message> listener) {
		super.registerNotificationStreamListener(listener);
		messageQueue.addListener(listener);
	}

	@Override
	protected AuthoringController getEngineForRoom(String room) {
		return roomsToEngines.get(room);
	}

	@Override
	protected void createEngineForRoom(String room) {
		roomsToEngines.put(room, new AuthoringController());
	}

	// TODO - Handle 'Export game' request

	private byte[] handleSetLevel(int clientId, AuthoringClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) {
		int levelCreated = clientMessage.getSetLevel().getLevel();
		getEngineForRoom(getGameRoomNameOfClient(clientId)).setLevel(levelCreated);
		// Push notification
		messageQueue.add(AuthoringServerMessage.newBuilder()
				.setNotification(AuthoringNotification.newBuilder().setLevelCreated(levelCreated).build()).build());
		return new byte[] {}; // No response expected
	}

	private byte[] handleGetElementBaseConfigurationOptions(int clientId, AuthoringClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) {
		return AuthoringServerMessage.newBuilder()
				.addAllElementBaseConfigurationOptions(
						getEngineForRoom(getGameRoomNameOfClient(clientId)).packageElementBaseConfigurationOptions())
				.build().toByteArray();
	}

	private byte[] handleGetAuxiliaryElementConfigurationOptions(int clientId, AuthoringClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) {
		Map<String, String> baseConfigOptions = new HashMap<>();
		clientMessage.getGetAuxiliaryElementConfig().getBaseConfigurationChoicesList().stream()
				.forEach(property -> baseConfigOptions.put(property.getName(), property.getValue()));
		return AuthoringServerMessage.newBuilder()
				.addAllAuxiliaryElementConfigurationOptions(getEngineForRoom(getGameRoomNameOfClient(clientId))
						.packageAuxiliaryElementConfigurationOptions(baseConfigOptions))
				.build().toByteArray();
	}

	// TODO - Remaining methods

}
