package player;

import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class NotificationBar {
	private String message;
	private Label messageView;
	private ImageView graphic;
	private HBox bar;
	private HBox attachBox;
	private Timer watch;
	private NotificationTimerTask notificationRemover;
	
	public NotificationBar(HBox box) {
		attachBox = box;
		message = new String();
		messageView = new Label(message);
		graphic = new ImageView();
		bar = new HBox();
		notificationRemover = new NotificationTimerTask(attachBox, bar);
		addItems();
		setStyle();
	}
	
	public NotificationBar(HBox box, String words) {
		attachBox = box;
		message = new String(words);
		messageView = new Label(message);
		graphic = new ImageView();
		bar = new HBox();
		addItems();
		setStyle();
	}
	
	public NotificationBar(HBox box, String imageName, String words) {
		attachBox = box;
		message = new String(words);
		messageView = new Label(message);
		Image personImage = new Image(getClass().getClassLoader().getResourceAsStream(imageName));
		graphic = new ImageView(personImage);
		bar = new HBox();
		addItems();
		setStyle();
	}
	
	private void addItems() {
		bar.getChildren().add(graphic);
		bar.getChildren().add(messageView);
	}
	
	private void setStyle() {
		bar.getStylesheets().add("player/resources/multiplayer.css");
		bar.getStyleClass().add("borders");
	}
	
	public void setText(String newMessage) {
		message = newMessage;
		messageView.setText(newMessage);
	}
	
	public void show() {
		attachBox.getChildren().add(bar);
		watch.schedule(notificationRemover, 3000L);
	}
}
