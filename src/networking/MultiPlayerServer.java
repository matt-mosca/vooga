package networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/**
 * Handles requests from multi-player clients over the network
 * 
 * @author radithya
 */
public class MultiPlayerServer {

	// TODO - Refactor common data / logic of MultiPlayerServer and ChatServer into
	// abstract class

	private final int PORT = 9041;
	private Set<DataOutputStream> clientByteWriters = new HashSet<>();
	private ServerSocket listener;
	private MultiPlayerController multiPlayerController = new MultiPlayerController();

	public MultiPlayerServer() {
		try {
			listener = new ServerSocket(PORT);
		} catch (IOException e) {
			// todo - handle
		}
	}

	public static void main(String[] args) {
		MultiPlayerServer chatServer = new MultiPlayerServer();
		System.out.println("Server is running...");
		try {
			while (true) {
				ChatServerHandler chatServerHandler = chatServer.new ChatServerHandler(chatServer.listener.accept());
				chatServerHandler.start();
			}
		} catch (IOException e) {
			// do nothing
		} finally {
			try {
				chatServer.listener.close();
			} catch (IOException e) {
				// do nothing
			}
		}
	}

	private class ChatServerHandler extends Thread {

		private Socket socket;
		private DataInputStream input;
		private DataOutputStream byteWriter;

		public ChatServerHandler(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				input = new DataInputStream(socket.getInputStream());
				byteWriter = new DataOutputStream(socket.getOutputStream());
				clientByteWriters.add(byteWriter);
				processMessages();
				return;
			} catch (IOException e) {
				// todo - handle
			} finally {
				closeClient();
			}
		}

		private void processMessages() throws IOException {
			while (true) {
				int len = input.readInt();
				if (len > 0) {
					byte[] readBytes = new byte[len];
					System.out.println("Server waiting for input");
					input.readFully(readBytes);
					byte[] response = multiPlayerController
							.handleRequestAndSerializeResponse(socket.getRemoteSocketAddress().hashCode(), readBytes);
					for (DataOutputStream writer : clientByteWriters) {
						writer.writeInt(response.length);
						writer.write(response, 0, response.length);
					}
				}
			}
		}

		/**
		 * Close out a client's connection.
		 */
		private void closeClient() {
			if (byteWriter != null) {
				clientByteWriters.remove(byteWriter);
			}
			try {
				multiPlayerController.disconnectClient(socket.getRemoteSocketAddress().hashCode());
				socket.close();
			} catch (IOException e) {
				// do nothing
			}
		}

	}
}
