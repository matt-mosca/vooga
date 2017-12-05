package networking;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
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
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.Socket;
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

    // get from some prop file
    private final String SERVER_ADDRESS = "152.3.53.39";

    // the name will be replaced with user's chosen name
    private final String PLAYER_NAME = "Player" + String.valueOf(Math.random());
    // port is arbitrary
    private final int PORT = 1234;

    private ObservableList<Node> chatItems;
    private Socket socket;
    private InetAddress group;
    private TextArea inputArea;
    private OutputStream outputStream;

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

    public void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {

        }
    }

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
        outputStream.write(message.getBytes());
    }

    private void setupChatSocket() {
        try {
            // Make connection and initialize streams
            socket = new Socket(SERVER_ADDRESS, 9001);
            outputStream = socket.getOutputStream();
            Thread t = new Thread(new ChatThread(socket, chatItems));
            t.start();
        } catch (SocketException se) {
            System.out.println("Error creating socket");
        } catch (Exception ie) {
            System.out.println("Error reading/writing from/to socket");
        }
    }

    // necessary on Mac OS to make sure Multicast sockets work
    /*private void configureNetworkInterface() throws SocketException {
        Enumeration e = NetworkInterface.getNetworkInterfaces();
        while (e.hasMoreElements()) {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements()) {
                InetAddress i = (InetAddress) ee.nextElement();
                if (i.isSiteLocalAddress() && !i.isAnyLocalAddress() && !i.isLinkLocalAddress()
                        && !i.isLoopbackAddress() && !i.isMulticastAddress()) {
                    socket.setNetworkInterface(NetworkInterface.getByName(n.getName()));
                }
            }
        }
    }*/
}