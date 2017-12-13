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
public abstract class BinaryStreamServerHandler extends AbstractServerHandler {

	private DataInputStream input;
	private DataOutputStream byteWriter;
	
	public BinaryStreamServerHandler(Socket socket) {
		super(socket);
	}

	void writeBytes(byte[] bytes) throws IOException {
		byteWriter.writeInt(bytes.length);
		byteWriter.write(bytes, 0, bytes.length);
	}
	
	protected abstract void respondToInput(byte[] inputBytes) throws IOException, ReflectiveOperationException;
	
	protected DataInputStream getInputStream() {
		return input;
	}
	
	protected DataOutputStream getOutputStream() {
		return byteWriter;
	}
	
	@Override
	protected void processMessages() throws IOException, ReflectiveOperationException {
		while (true) {
			int len = input.readInt();
			if (len > 0) {
				byte[] inputBytes = new byte[len];
				input.readFully(inputBytes);
				respondToInput(inputBytes);
			}
		}
	}

	@Override
	protected void initializeStreams() throws IOException {
		Socket socket = getSocket();
		input = new DataInputStream(socket.getInputStream());
		byteWriter = new DataOutputStream(socket.getOutputStream());
	}

}
