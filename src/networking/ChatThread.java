package networking;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.text.Text;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Used to poll for other participants' messages.
 *
 * Based on http://www.geeksforgeeks.org/a-group-chat-application-in-java/.
 *
 * @author Ben Schwennesen
 */
public class ChatThread implements Runnable {

    private final int MAX_LENGTH = 1000;
    private final String CHARACTER_SET = "UTF-8";
    private final String ERROR = "Chat encountered an error. Try again later.";

    private final int PORT;
    private MulticastSocket socket;
    private InetAddress group;
    private ObservableList<Node> chatItems;

    public ChatThread(MulticastSocket socket, InetAddress group, int port, ObservableList<Node> chatItems) {
        this.socket = socket;
        this.group = group;
        this.PORT = port;
        this.chatItems = chatItems;
    }

    @Override
    public void run() {
        while(!socket.isClosed()) {
            byte[] buffer = new byte[MAX_LENGTH];
            DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, PORT);
            String message;
            try {
                socket.receive(datagram);
                message = new String(buffer, 0, datagram.getLength(), CHARACTER_SET);
                Platform.runLater(() -> chatItems.add(chatItems.size(), new Text(message)));
            } catch (Exception e) {
                Platform.runLater(() -> chatItems.add(chatItems.size(), new Text(ERROR)));
            }
        }
    }
}