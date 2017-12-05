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
 * TODO finish
 *
 * Reference: http://cs.lmu.edu/~ray/notes/javanetexamples/
 */
public class ChatServer {

    private final int PORT = 9042;
    private Set<String> clientUserNames = new HashSet<>();
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
                while (true) {
                    String message = input.readLine();
                    if (message == null) {
                        return;
                    }
                    System.out.println(message);
                    for (PrintWriter writer : clientPrintWriters) {
                        writer.println(message);
                    }
                }
            } catch(IOException e) {
                // todo - handle
                e.printStackTrace();
            } finally {
                // This client is going down!  Remove its name and its print
                // writer from the sets, and close its socket.
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
}
