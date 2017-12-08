package MediaTesting;

import factory.AudioClipFactory;
import factory.MediaPlayerFactory;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class audioClipTest extends Application {

	public static final String TITLE = "Duvall In This Together";
	public static final int HEIGHT = 600;
	public static final int WIDTH = 800;
	public static final Paint BACKGROUND = Color.WHITE;
	public static final int FRAMES_PER_SECOND = 60;
	public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	private Group root;

	/**
	 * Initialize what will be displayed and how it will be updated.
	 */
	@Override
	public void start(Stage s) {
		root = new Group();
		Button play = new Button();
		play.setText("play");
		play.setOnAction(e->player());
		root.getChildren().add(play);
		Scene myScene = new Scene(root, WIDTH, HEIGHT, Color.WHITE);
		s.setScene(myScene);
		s.setTitle(TITLE);
		s.show();
	}
	
	private void player() {
		AudioClipFactory audio = new AudioClipFactory();
		//Slider slider = new Slider(0,100,5);
		MediaPlayerFactory mediaPlayer = new  MediaPlayerFactory();
		mediaPlayer.getMediaPlayer().play();
		MediaView mediaViewer = new MediaView(mediaPlayer.getMediaPlayer());
		//audio.getAudioClip().play();
		root.getChildren().add(mediaViewer);
		
	}

	/**
	 * Start the program.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}

