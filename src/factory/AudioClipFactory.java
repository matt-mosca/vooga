package factory;

import javafx.scene.media.AudioClip;

public class AudioClipFactory {
	
	private final String DEFAULT_AUDIO_NAME = "MediaTesting/Bomb_Exploding-Sound_Explorer-68256487.mp3";
	private final double DEFAULT_VOLUME = 3;
	private AudioClip audioClip;
	//TO DO make default_volume to universal volume
	
	public AudioClipFactory() {
		audioClip = new AudioClip(composeResourceString(DEFAULT_AUDIO_NAME));
		audioClip.setVolume(DEFAULT_VOLUME);
	}
	
	public AudioClipFactory(String name) {
		audioClip = new AudioClip(composeResourceString(name));
		audioClip.setVolume(DEFAULT_VOLUME);
	}
	
	public AudioClip getAudioClip() {
		return audioClip;
	}
	
	private String composeResourceString(String name) {
		return getClass().getResource(name).toString();
	}

}
