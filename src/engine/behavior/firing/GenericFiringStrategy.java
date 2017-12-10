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
	private String audioUrl;

	public GenericFiringStrategy(
			@ElementProperty(value = "projectileTemplate", isTemplateProperty = true) String projectileTemplate,
			@ElementProperty(value = "firingAudioUrl", isTemplateProperty = true) String audioUrl) {
		this.projectileTemplate = projectileTemplate;
		this.audioUrl = audioUrl;
	}

	@Override
	public String fire() {
		return projectileTemplate;
	}
	
	@Override
	public boolean isExpended() {
		return false;
	}
	
	@Override
	public String getAudioUrl() {
		return audioUrl;
	}
}
