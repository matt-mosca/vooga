package engine.behavior.firing;

import engine.behavior.ElementProperty;

/**
 * Captures whatever is common across all implementations of FiringStrategy
 * 
 * @author tyler
 * @author radithya
 *
 */
public abstract class GenericFiringStrategy implements FiringStrategy {

	private String projectileTemplate;
	private String audioURI;

	public GenericFiringStrategy(
			@ElementProperty(value = "projectileTemplate", isTemplateProperty = true) String projectileTemplate,
			@ElementProperty(value = "audioURI", isTemplateProperty = true) String audioURI) {
		this.projectileTemplate = projectileTemplate;
		this.audioURI = audioURI;
	}

	@Override
	public String fire() {
		return projectileTemplate;
	}
	
	@Override
	public boolean isExpended() {
		return false;
	}
	

	public String getAudioURI() {
		return audioURI;
	}
}
