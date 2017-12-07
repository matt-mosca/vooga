package networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.google.protobuf.InvalidProtocolBufferException;

import networking.protocol.PlayerClient.ClientMessage;
import networking.protocol.PlayerClient.CreateGameRoom;
import networking.protocol.PlayerServer.GameRoomCreationStatus;
import networking.protocol.PlayerServer.ServerMessage;

public class MultiPlayerClient {

	// TODO - Refactor common data / set-up logic of ChatClient and MultiPlayerClient into
	// abstract class
	// get from some properties file
	private final String SERVER_ADDRESS = "127.0.0.1"; // Change to "152.3.53.39" once uploaded to VM
	private final int PORT = 9041;
	private Socket socket;
	private DataOutputStream outputWriter;
	private Thread ioThread;

	// Game client state (keeping track of which multi-player game it is in, etc)
	private String gameRoomId = "";

	public MultiPlayerClient() {
		setupChatSocket();
		ioThread = new PlayerIOThread();
		ioThread.start();
	}

	synchronized void createGameRoom(String gameName) {
		// Construct message
		ClientMessage.Builder clientMessageBuilder = ClientMessage.newBuilder();
		CreateGameRoom gameRoomCreationRequest = CreateGameRoom.newBuilder().setRoomName(gameName).build();
		ClientMessage clientMessage = clientMessageBuilder.setCreateGameRoom(gameRoomCreationRequest).build();
		// Serialize and write to output stream
		byte[] requestBytes = clientMessage.toByteArray();
		try {
			outputWriter.writeInt(requestBytes.length);
			outputWriter.write(requestBytes);
		} catch (IOException e) {
			e.printStackTrace();// TEMP
		}
	}

	// TODO - more methods
	private void handleGameRoomCreation(String gameRoomId) {
		System.out.println("Setting gameRoomId to " + gameRoomId);
		this.gameRoomId = gameRoomId;
	}

	private synchronized void handleServerMessage(byte[] inputBytes) {
		// Dispatch appropriate method - TODO : Reflection ?
		try {
			ServerMessage serverMessage = ServerMessage.parseFrom(inputBytes);
			if (serverMessage.hasGameRoomCreationStatus()) {
				GameRoomCreationStatus gameRoomCreationStatus = serverMessage.getGameRoomCreationStatus();
				if (!gameRoomCreationStatus.hasError()) {
					handleGameRoomCreation(gameRoomCreationStatus.getRoomId());
				} else {
					// TODO - throw exception to be handled by front end?
					System.out.println("Error creating game room: " + gameRoomCreationStatus.getError());
				}
			}
			// TODO - handle other message types
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace(); // TEMP
		}
	}

	private synchronized void setupChatSocket() {
		try {
			// Make connection and initialize streams
			socket = new Socket(SERVER_ADDRESS, PORT);
			outputWriter = new DataOutputStream(socket.getOutputStream());
		} catch (IOException socketException) {
			socketException.printStackTrace();
		}
	}

	class PlayerIOThread extends Thread {
		private DataInputStream input;

		@Override
		public void run() {
			try {
				input = new DataInputStream(socket.getInputStream());
				while (!socket.isClosed()) {
					int len = input.readInt();
					if (len > 0) {
						byte[] readBytes = new byte[len];
						input.readFully(readBytes);
						handleServerMessage(readBytes);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// Test client-server integration
	public static void main(String[] args) {
		MultiPlayerClient testClient = new MultiPlayerClient();
		testClient.createGameRoom("abc.voog");
	}

}
