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
	public static final int SCENE_WIDTH = 500;
	public static final int SCENE_HEIGHT = 200;
	
	private MediaPlayer mediaPlayer;
	private MediaView mediaViewer;
	private Stage mediaStage;
	private Group root = new Group();
	private Button skipButton;
	
	public MediaWindow(MediaPlayer mediaPlayer) {
		mediaStage = new Stage();
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		mediaStage.setX(primaryScreenBounds.getMinX());
		mediaStage.setY(primaryScreenBounds.getMinY());
		mediaStage.setWidth(primaryScreenBounds.getWidth());
		mediaStage.setHeight(primaryScreenBounds.getHeight());
		this.mediaPlayer = mediaPlayer;
		mediaViewer = new MediaView(this.mediaPlayer);
		skipButton = new Button("skip");
		skipButton.setOnAction(e->skip());
	}
	
	public void play() {
		root.getChildren().add(mediaViewer);
		root.getChildren().add(skipButton);
		Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT, Color.WHITE);
		mediaStage.setTitle("Media");
		mediaStage.setScene(scene);
		mediaStage.show();
		mediaPlayer.play();
	}
	
	public void skip() {
		mediaPlayer.stop();
		mediaStage.close();
	}
}
