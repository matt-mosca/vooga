package networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Handles a game connection with a client, handling client-server messages
 * related to the multi-player game
 * 
 * @author radithya
 *
 */
public class MultiPlayerServerHandler extends AbstractServerHandler {

	private DataInputStream input;
	private DataOutputStream byteWriter;

	private MultiPlayerController multiPlayerController;

	public MultiPlayerServerHandler(Socket socket, MultiPlayerController multiPlayerController) {
		super(socket);
		this.multiPlayerController = multiPlayerController;
	}

	void writeBytes(byte[] bytes) throws IOException {
		byteWriter.writeInt(bytes.length);
		byteWriter.write(bytes, 0, bytes.length);
	}
	
	@Override
	protected void processMessages() throws IOException, ReflectiveOperationException {
		while (true) {
			int len = input.readInt();
			if (len > 0) {
				byte[] readBytes = new byte[len];
				input.readFully(readBytes);
				byte[] response = multiPlayerController
						.handleRequestAndSerializeResponse(getSocket().getRemoteSocketAddress().hashCode(), readBytes);
				writeBytes(response);
			}
		}
	}

	@Override
	protected void initializeStreams() throws IOException {
		Socket socket = getSocket();
		input = new DataInputStream(socket.getInputStream());
		byteWriter = new DataOutputStream(socket.getOutputStream());
	}

	@Override
	protected void closeClient() {
		super.closeClient();
		multiPlayerController.disconnectClient(getSocket().getRemoteSocketAddress().hashCode());
	}
	

}
