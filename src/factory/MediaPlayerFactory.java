package factory;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MediaPlayerFactory {
	
	private final String DEFAULT_MEDIA_NAME = "src/MediaTesting/497632384.mp4";
	private Media media;
	private MediaPlayer mediaPlayer;
	
	public MediaPlayerFactory() {
		media = new Media(composeResourceStringUrl(DEFAULT_MEDIA_NAME));
		mediaPlayer = new MediaPlayer(media);
	}
	
	public MediaPlayerFactory(String mediaName) {
		media = new Media(composeResourceStringUrl(mediaName));
		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setCycleCount(mediaPlayer.INDEFINITE);
		mediaPlayer.setMute(true);
	}
	
	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}
	
	private String composeResourceStringUrl(String url) {
		return new File(url).toURI().toString();
	}
}
