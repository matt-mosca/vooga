package networking;

import java.util.HashMap;
import java.util.Map;

import engine.authoring_engine.AuthoringController;
import networking.protocol.AuthorClient.AuthoringClientMessage;
import networking.protocol.AuthorServer.AuthoringServerMessage;
import networking.protocol.PlayerServer.ServerMessage;

public class CollaborativeAuthoringController extends AbstractServerController {

	private Map<String, AuthoringController> roomsToEngines = new HashMap<>();

	@Override
	protected AuthoringController getEngineForRoom(String room) {
		return roomsToEngines.get(room);
	}

	@Override
	protected void createEngineForRoom(String room) {
		roomsToEngines.put(room, new AuthoringController());
	}
	
	private byte[] handleGetElementBaseConfigurationOptions(int clientId, AuthoringClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) {
		return AuthoringServerMessage.newBuilder()
				.addAllElementBaseConfigurationOptions(
						getEngineForRoom(getGameRoomNameOfClient(clientId)).packageElementBaseConfigurationOptions())
				.build().toByteArray();
	}
	
	private byte[] handleGetAuxiliaryElementConfigurationOptions(int clientId, AuthoringClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		//return AuthoringServerMessage.newBuilder().addAllAuxiliaryElementConfigurationOptions(getEngineForRoom(getGameRoomNameOfClient(clientId)).packageAuxiliaryElementConfigurationOptions(clientMessage.getGetAuxiliaryElementConfig().))
		// TODO
		return new byte[] {}; // TEMP
	}

	// TODO - Remaining methods

}
