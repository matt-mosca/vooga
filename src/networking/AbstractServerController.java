package networking;

import javafx.collections.ListChangeListener;
import networking.protocol.PlayerServer.ServerMessage;

public abstract class AbstractServerController {

	public abstract byte[] handleRequestAndSerializeResponse(int clientId, byte[] requestBytes);

	public abstract void registerNotificationStreamListener(ListChangeListener<? super ServerMessage> listener);

	public abstract void disconnectClient(int clientId);

}
