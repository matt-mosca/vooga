package engine.behavior.firing;

import java.awt.geom.Point2D;

import engine.behavior.ParameterName;

public class PeriodicFiringStrategy extends GenericFiringStrategy {

	private double attackPeriod;
	private double attackCounter;
	private double range;
	
	
	
	public PeriodicFiringStrategy(@ParameterName("projectileTemplate") String projectileTemplate, @ParameterName("attackRate") double attackPeriod, @ParameterName("range") double range) {
		super(projectileTemplate);
		this.attackPeriod = attackPeriod;
		this.range = range;
		resetAttackTimer();
	}

	@Override
	public boolean shouldFire() {
		if (updateAndCheckTimer()) {
			return true;
		}
		return false;
	}
	
	private boolean updateAndCheckTimer() {
		if (attackCounter-- == 0) {
			resetAttackTimer();
			// check if distance to point is <= range
			if (targetIsWithinRange(getTargetPoint())) {
				return true;				
			}
		}
		return false;
	}
	
	private void resetAttackTimer() {
		attackCounter = attackPeriod;
	}

	
	private boolean targetIsWithinRange(Point2D.Double target) {
		return target.distance(getPosition()) <= range;
	}

}
