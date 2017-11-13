package networking;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Used for chat between authors and players over a network socket.
 *
 * Only works if machines are on same network (and sometimes not even).
 *
 * We're probably going to want to setup a full server on AWS or something to get this and online
 * multi-player working.
 *
 * Based on http://www.geeksforgeeks.org/a-group-chat-application-in-java/.
 *
 * @author Ben Schwennesen
 */
public class ChatClient {

    // the name will be replaced with user's chosen name
    private final String PLAYER_NAME = "Player" + String.valueOf(Math.random());
    // port is arbitrary
    private final int PORT = 1234;

    private ObservableList<Node> chatItems;
    private MulticastSocket socket;
    private InetAddress group;
    private TextArea inputArea;

    public ChatClient(Stage primaryStage) {
        chatItems = FXCollections.observableArrayList();
        ListView<Node> chatList = new ListView<>(chatItems);
        Scene chat = new Scene(chatList);
        primaryStage.setScene(chat);

        setupChatSocket();

        inputArea = new TextArea();
        inputArea.setOnKeyPressed(e -> processText(e));
        chatItems.add(chatItems.size(), inputArea);
    }

    public void closeSocket() { socket.close(); }

    private void processText(KeyEvent key){
        if (key.isShiftDown() && key.getCode().equals(KeyCode.ENTER)) {
            String input = inputArea.getText();
            inputArea.clear();
            if (input.trim().length() > 0) {
                try {
                    sendMessage(input);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendMessage(String message) throws IOException {
        message = PLAYER_NAME + ": " + message;
        byte[] buffer = message.getBytes();
        DatagramPacket datagram = new DatagramPacket(buffer,buffer.length, group, PORT);
        socket.send(datagram);
    }

    private void setupChatSocket() {
        try {
            socket = new MulticastSocket(PORT);
            configureNetworkInterface();
            group = InetAddress.getByName("228.5.6.7");

            socket.setTimeToLive(0);
            socket.joinGroup(group);

            Thread t = new Thread(new ChatThread(socket, group, PORT, chatItems));
            t.start();
        } catch (SocketException se) {
            System.out.println("Error creating socket");
        } catch (Exception ie) {
            System.out.println("Error reading/writing from/to socket");
        }
    }

    // necessary on Mac OS to make sure Multicast sockets work
    private void configureNetworkInterface() throws SocketException {
        Enumeration e = NetworkInterface.getNetworkInterfaces();
        while (e.hasMoreElements()) {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements()) {
                InetAddress i = (InetAddress) ee.nextElement();
                if (i.isSiteLocalAddress() && !i.isAnyLocalAddress() && !i.isLinkLocalAddress()
                        && !i.isLoopbackAddress() && !i.isMulticastAddress()) {
                    System.out.println(n.getName());
                    socket.setNetworkInterface(NetworkInterface.getByName(n.getName()));
                }
            }
        }
    }
}