package networking;

import java.io.IOException;
import java.net.Socket;
import java.util.Collection;
import java.util.HashSet;

import javafx.stage.Stage;

/**
 * Handles requests from multi-player clients over the network
 * 
 * @author radithya
 */
public class MultiPlayerServer extends AbstractServer {

	public static final int PORT = 9041;

	private MultiPlayerController multiPlayerController = new MultiPlayerController();
	// Keep track of handlers for broadcasting
	private Collection<MultiPlayerServerHandler> handlers = new HashSet<>();

	public MultiPlayerServer() {
		super();
		// Register binding / observable on controller for push notifications
		multiPlayerController.registerNotificationStreamListener(e -> {
			while (e.next()) {
				e.getAddedSubList().stream().forEach(addedMessage -> pushNotification(addedMessage.toByteArray()));				
			}
		});
	}

	@Override
	public int getPort() {
		return PORT;
	}

	void pushNotification(byte[] notificationBytes) {
		System.out.println("Pushing notification");
		handlers.forEach(handler -> {
			try {
				handler.writeBytes(notificationBytes);
			} catch (IOException e) {
				// TODO - handle ?
			}
		});
	}

	@Override
	protected AbstractServerHandler getServerHandler(Socket acceptSocket) {
		MultiPlayerServerHandler handler = new MultiPlayerServerHandler(acceptSocket, multiPlayerController);
		handlers.add(handler);
		return handler;
	}

	public static void main(String[] args) {
		//launch(args);
		MultiPlayerServer multiPlayerServer = new MultiPlayerServer();
		multiPlayerServer.startServer();
	}

}
