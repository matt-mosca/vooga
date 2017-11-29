package engine.behavior.firing;

import engine.behavior.ParameterName;

/**
 * Represents strategy of not firing at all, applies to sprites whose firing is
 * to be disabled
 * 
 * @author tyler
 * @author radithya
 *
 */
public class NoopFiringStrategy extends GenericFiringStrategy {

	public NoopFiringStrategy(@ParameterName("projectileTemplate") String projectileTemplate) {
		super(projectileTemplate);
	}

	@Override
	public boolean shouldFire() {
		return false;
	}

}
