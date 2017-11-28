package engine.behavior.firing;

import engine.behavior.ParameterName;

/**
 * Shoots periodically (once every x number of cycles, where x is any positive
 * integer) with limited range
 * 
 * @author radithya
 *
 */
public class PeriodicFiringStrategy extends GenericFiringStrategy {

	private double attackPeriod;
	private double attackCounter;
	
	public PeriodicFiringStrategy(@ParameterName("projectileTemplate") String projectileTemplate, @ParameterName("attackPeriod") double attackPeriod) {
		super(projectileTemplate);
		this.attackPeriod = attackPeriod;
		resetAttackTimer();
	}

	@Override
	public boolean shouldFire() {
		return updateAndCheckTimer();
	}
	
	private boolean updateAndCheckTimer() {
		if (attackCounter-- == 0) {
			resetAttackTimer();
			return true;
		}
		return false;
	}
	
	private void resetAttackTimer() {
		attackCounter = attackPeriod;
	}

}
