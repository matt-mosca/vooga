package networking;

import java.io.IOException;
import java.net.Socket;

import javafx.collections.ObservableList;
import networking.protocol.PlayerServer.ServerMessage;

public class ClientNotificationHandler extends BinaryStreamServerHandler {

	private ObservableList<ServerMessage> messageQueue;
	
	public ClientNotificationHandler(Socket socket, ObservableList<ServerMessage> messageQueue) {
		super(socket);
		this.messageQueue = messageQueue;
	}

	@Override
	protected void respondToInput(byte[] inputBytes) throws IOException, ReflectiveOperationException {
		System.out.println("Received input of " + inputBytes.length + " bytes");
		messageQueue.add(ServerMessage.parseFrom(inputBytes));
	}

}
