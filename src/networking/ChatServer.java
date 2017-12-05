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
        Scanner scanner = new Scanner(System.in);
        String input;
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

        private String userName;
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
                while(userName == null) {
                    printWriter.println("Submit a user name");
                    userName = input.readLine();
                    synchronized (clientUserNames) {
                        if (userName != null && !clientUserNames.contains(userName)) {
                            clientUserNames.add(userName);
                            break;
                        }
                    }
                }
                clientPrintWriters.add(printWriter);
                while (true) {
                    String message = input.readLine();
                    if (message == null) {
                        return;
                    }
                    for (PrintWriter writer : clientPrintWriters) {
                        writer.println("MESSAGE " + userName + ": " + input);
                    }
                }
            } catch(IOException e) {
                // todo - handle
            } finally {
                // This client is going down!  Remove its name and its print
                // writer from the sets, and close its socket.
                if (userName != null) {
                    clientUserNames.remove(userName);
                }
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
