package engine.behavior.firing;

import engine.behavior.ElementProperty;

/**
 * Shoots periodically (once every x number of cycles, where x is any positive
 * integer).
 * 
 * @author tyler
 * @author radithya
 *
 */
public class PeriodicFiringStrategy extends AbstractPeriodicFiringStrategy {

	private String projectileTemplate;
	private String audioUrl;

	public PeriodicFiringStrategy(
			@ElementProperty(value = "projectileTemplate", isTemplateProperty = true) String projectileTemplate,
			@ElementProperty(value = "attackPeriod", isTemplateProperty = true) Double attackPeriod,
			@ElementProperty(value = "firingAudioUrl", isTemplateProperty = true) String audioUrl) {
		super(attackPeriod);
		this.projectileTemplate = projectileTemplate;
		this.audioUrl = audioUrl;
		
	}

	@Override
	public String fire() {
		return projectileTemplate;
	}

	@Override
	public String getAudioUrl() {
		return audioUrl;
	}
}
