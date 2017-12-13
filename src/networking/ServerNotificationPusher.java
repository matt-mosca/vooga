package networking;

import java.io.IOException;
import java.net.Socket;

public class ServerNotificationPusher extends BinaryStreamServerHandler {

	public ServerNotificationPusher(Socket socket) {
		super(socket);
	}

	@Override
	protected void respondToInput(byte[] inputBytes) throws IOException, ReflectiveOperationException {
	}


}
