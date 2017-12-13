package networking;

import java.net.Socket;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import networking.protocol.PlayerServer.ServerMessage;

public class ClientNotificationReceiver extends AbstractServer {

	private ObservableList<ServerMessage> messageQueue;

	public ClientNotificationReceiver() {
		super();
		System.out.println("Client notification server running");
		messageQueue = FXCollections.observableArrayList();
	}

	public void registerNotificationListener(ListChangeListener<? super ServerMessage> listener) {
		messageQueue.addListener(listener);
	}

	@Override
	public int getPort() {
		return Constants.NOTIFICATION_RECEIVER_PORT;
	}

	@Override
	protected AbstractServerHandler getServerHandler(Socket acceptSocket) {
		return new ClientNotificationHandler(acceptSocket, messageQueue);
	}

}
