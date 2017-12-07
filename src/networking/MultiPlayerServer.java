package networking;

import java.net.Socket;

/**
 * Handles requests from multi-player clients over the network
 * 
 * @author radithya
 */
public class MultiPlayerServer extends AbstractServer {

	public static final int PORT = 9041;

	private MultiPlayerController multiPlayerController = new MultiPlayerController();

	public MultiPlayerServer() {
		super();
	}

	@Override
	public int getPort() {
		return PORT;
	}

	@Override
	protected AbstractServerHandler getServerHandler(Socket acceptSocket) {
		return new MultiPlayerServerHandler(acceptSocket, multiPlayerController);
	}

	public static void main(String[] args) {
		MultiPlayerServer multiPlayerServer = new MultiPlayerServer();
		multiPlayerServer.startServer();
	}

}
