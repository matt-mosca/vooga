package factory;

import java.io.File;

import javafx.scene.media.AudioClip;

public class AudioClipFactory {
	
	private final String DEFAULT_AUDIO_NAME = "src/MediaTesting/Bomb_Exploding-Sound_Explorer-68256487.mp3";
	private final double DEFAULT_VOLUME = 40;
	private AudioClip audioClip;
	//TO DO make default_volume to universal volume
	
	public AudioClipFactory() {
		audioClip = new AudioClip(composeResourceStringUrl(DEFAULT_AUDIO_NAME));
		audioClip.setVolume(DEFAULT_VOLUME);
	}
	
	public AudioClipFactory(String name) {
		audioClip = new AudioClip(composeResourceStringUrl(name));
		audioClip.setVolume(DEFAULT_VOLUME);
	}
	
	public AudioClip getAudioClip() {
		return audioClip;
	}
	
	private String composeResourceStringUrl(String url) {
		return new File(url).toURI().toString();
	}
}
