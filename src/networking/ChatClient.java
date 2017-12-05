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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
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

    private final String MESSAGE_DELIMITER = ": ";
    // get from some prop file
    private final String SERVER_ADDRESS = "152.3.53.39";

    // port is arbitrary
    private final int PORT = 1234;

    private final String USER_NAME_PROMPT = "Set user name above";
    private final String USER_NAME_ACCEPTED = "Username %s accepted!";
    private String userName;

    private ObservableList<Node> chatItems;
    private Socket socket;
    private TextArea inputArea;
    private PrintWriter outputWriter;

    public ChatClient(Stage primaryStage) {
        chatItems = FXCollections.observableArrayList();
        ListView<Node> chatList = new ListView<>(chatItems);
        Scene chat = new Scene(chatList);
        primaryStage.setScene(chat);

        setupChatSocket();
        inputArea = new TextArea();
        inputArea.setOnKeyPressed(e -> processText(e));
        chatItems.add(chatItems.size(), inputArea);
        Text prompt = new Text(USER_NAME_PROMPT);
        prompt.setFill(Color.RED);
        chatItems.add(prompt);
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

    private void sendMessage(String input) throws IOException {
        if (userName == null) {
            userName = input;
            Text acceptanceMessage = new Text(String.format(USER_NAME_ACCEPTED, input));
            acceptanceMessage.setFill(Color.RED);
            chatItems.add(acceptanceMessage);
        } else {
            System.out.println(input);
            String message = userName + MESSAGE_DELIMITER + input;
            outputWriter.println(message);
        }
    }

    private void setupChatSocket() {
        try {
            // Make connection and initialize streams
            socket = new Socket(SERVER_ADDRESS, 9042);
            outputWriter = new PrintWriter(socket.getOutputStream(), true);
            Thread t = new ChatThread(socket, chatItems);
            t.start();
        } catch (SocketException se) {
            System.out.println("Error creating socket");
        } catch (Exception ie) {
            System.out.println("Error reading/writing from/to socket");
        }
    }
}