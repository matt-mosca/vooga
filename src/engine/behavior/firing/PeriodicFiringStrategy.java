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

	public PeriodicFiringStrategy(@ParameterName("projectileTemplate") String projectileTemplate,
			@ParameterName("attackPeriod") double attackPeriod) {
		super(attackPeriod);
		this.projectileTemplate = projectileTemplate;
	}

	@Override
	public String fire() {
		return projectileTemplate;
	}
}
