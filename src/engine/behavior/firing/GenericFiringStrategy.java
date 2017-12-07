package engine.behavior.firing;

import engine.behavior.ParameterName;

/**
 * Captures whatever is common across all implementations of FiringStrategy
 * 
 * @author tyler
 * @author radithya
 *
 */
public abstract class GenericFiringStrategy implements FiringStrategy {

	private String projectileTemplate;

	public GenericFiringStrategy(@ParameterName("projectileTemplate") String projectileTemplate) {
		this.projectileTemplate = projectileTemplate;
	}

	@Override
	public String fire() {
		return projectileTemplate;
	}
	
	@Override
	public boolean isExpended() {
		return false;
	}
}
