package networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Manages chat connections and distributes messages.
 *
 * Reference: http://cs.lmu.edu/~ray/notes/javanetexamples/
 *
 * @author Ben Schwennesen
 */
public class ChatServer {

    private final int PORT = 9042;
    private Set<PrintWriter> clientPrintWriters = new HashSet<>();
    private ServerSocket listener;

    public ChatServer() {
        try {
            listener = new ServerSocket(PORT);
        } catch (IOException e) {
            // todo - handle
        }
    }

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
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
        private BufferedReader input;
        private PrintWriter printWriter;

        public ChatServerHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                printWriter = new PrintWriter(socket.getOutputStream(), true);
                clientPrintWriters.add(printWriter);
                processMessages();
                return;
            } catch(IOException e) {
                // todo - handle
            } finally {
                closeClient();
            }
        }

        private void processMessages() throws IOException {
            while (true) {
                String message = input.readLine();
                if (message == null) {
                    return;
                }
                for (PrintWriter writer : clientPrintWriters) {
                    writer.println(message);
                }
            }
        }

        /**
         * Close out a client's connection.
         */
        private void closeClient() {
            if (printWriter != null) {
                clientPrintWriters.remove(printWriter);
            }
            try {
                socket.close();
            } catch (IOException e) {
                // do nothing
            }
        }
    }
}