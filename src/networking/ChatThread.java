package networking;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;

/**
 * Used to poll for other participants' messages.
 *
 * Based on http://www.geeksforgeeks.org/a-group-chat-application-in-java/.
 *
 * @author Ben Schwennesen
 */
public class ChatThread implements Runnable {

    private final String ERROR = "Chat encountered an error. Try again later.";

    private Socket socket;
    private ObservableList<Node> chatItems;
    private BufferedReader in;

    public ChatThread(Socket socket, ObservableList<Node> chatItems) {
        this.socket = socket;
        this.chatItems = chatItems;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException e) {

        }

    }

    @Override
    public void run() {
        while(!socket.isClosed()) {
            String message;
            try {
                if ((message = in.readLine()) != null) {
                    Platform.runLater(() -> chatItems.add(chatItems.size(), new Text(message)));
                }
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> chatItems.add(chatItems.size(), new Text(ERROR)));
            }
        }
    }
}