package engine.behavior.firing;

import engine.behavior.ParameterName;

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
	private String audioURI;

	public PeriodicFiringStrategy(@ParameterName("projectileTemplate") String projectileTemplate, String audioURI,
			@ParameterName("attackPeriod") double attackPeriod) {
		super(attackPeriod);
		this.projectileTemplate = projectileTemplate;
		this.audioURI = audioURI;
		
	}

	@Override
	public String fire() {
		return projectileTemplate;
	}

	@Override
	public String getAudioURI() {
		return audioURI;
	}
}
