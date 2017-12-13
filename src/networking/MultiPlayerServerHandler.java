package networking;

import java.io.IOException;
import java.net.Socket;

/**
 * Handles a game connection with a client, handling client-server messages
 * related to the multi-player game
 * 
 * @author radithya
 *
 */
public class MultiPlayerServerHandler extends BinaryStreamServerHandler {

	private MultiPlayerController multiPlayerController;

	public MultiPlayerServerHandler(Socket socket, MultiPlayerController multiPlayerController) {
		super(socket);
		this.multiPlayerController = multiPlayerController;
	}

	@Override
	protected void respondToInput(byte[] inputBytes) throws IOException, ReflectiveOperationException {
		byte[] response = multiPlayerController
				.handleRequestAndSerializeResponse(getSocket().getRemoteSocketAddress().hashCode(), inputBytes);
		writeBytes(response);
	}
	
	@Override
	protected void closeClient() {
		super.closeClient();
		multiPlayerController.disconnectClient(getSocket().getRemoteSocketAddress().hashCode());
	}

}
