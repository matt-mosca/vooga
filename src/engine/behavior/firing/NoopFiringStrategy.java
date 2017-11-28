package engine.behavior.firing;

import engine.behavior.ParameterName;

public class NoopFiringStrategy extends GenericFiringStrategy {

	public NoopFiringStrategy(@ParameterName("projectileTemplate") String projectileTemplate) {
		super(projectileTemplate);
	}

	@Override
	public boolean shouldFire() {
		return false;
	}

}
