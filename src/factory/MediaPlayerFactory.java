package factory;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MediaPlayerFactory {
	
	private final String DEFAULT_MEDIA_NAME = "src/MediaTesting/497632384.mp4";
	private MediaPlayer mediaPlayer;
	
	public MediaPlayerFactory() {
		mediaPlayer = new MediaPlayer(new Media(composeResourceStringUrl(DEFAULT_MEDIA_NAME)));
	}
	
	public MediaPlayerFactory(String mediaName) {
		mediaPlayer = new MediaPlayer(new Media(composeResourceStringUrl(mediaName)));
		mediaPlayer.setCycleCount(mediaPlayer.INDEFINITE);
		mediaPlayer.setMute(true);
	}
	
	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}
	
	public void changeMediaPlayer(String newMediaName) {
		mediaPlayer = new MediaPlayer(new Media(composeResourceStringUrl(newMediaName)));
	}
	
	private String composeResourceStringUrl(String url) {
		return new File(url).toURI().toString();
	}
}
