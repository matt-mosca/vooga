package factory;

import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MediaWindow {

	public static final int STANDARD_TEXT_SIZE = 12;
	public static final int LARGE_TEXT_SIZE = 20;
	public static final int WRAPPING_WIDTH = 450;
	private double width;
	private double height;
	private double centeredX;
	
	private MediaPlayer mediaPlayer;
	private MediaView mediaViewer;
	private Stage mediaStage;
	private Group root = new Group();
	private Button skipButton;
	
	public MediaWindow(MediaPlayer mediaPlayer) {
		mediaStage = new Stage();
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		width = primaryScreenBounds.getWidth();
		height = primaryScreenBounds.getHeight();
		mediaStage.setX(primaryScreenBounds.getMinX());
		mediaStage.setY(primaryScreenBounds.getMinY());
		mediaStage.setWidth(width);
		mediaStage.setHeight(height);
		this.mediaPlayer = mediaPlayer;
		mediaViewer = new MediaView(this.mediaPlayer);
		mediaViewer.setFitWidth(width);
		mediaViewer.setFitHeight(height);
		centeredX= width/7;
		mediaViewer.setX(centeredX);
		skipButton = new Button("skip");
		skipButton.setOnAction(e->skip());
	}
	
	public void play() {
		root.getChildren().add(mediaViewer);
		root.getChildren().add(skipButton);
		Scene scene = new Scene(root, width, height, Color.BLACK);
		mediaStage.setTitle("Media");
		mediaStage.setScene(scene);
		mediaStage.show();
		mediaPlayer.play();
		mediaStage.setOnCloseRequest(e->mediaPlayer.stop());
	}
	
	public void skip() {
		mediaPlayer.stop();
		mediaStage.close();
	}
}
