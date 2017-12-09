package engine.behavior.firing;

import engine.behavior.ElementProperty;

/**
 * Periodically fires projectiles.
 *
 * @author Ben Schwennesen
 * @author radithya
 */
public abstract class AbstractPeriodicFiringStrategy implements FiringStrategy {

    private double attackPeriod;
    private double attackCounter;

    public AbstractPeriodicFiringStrategy(
            @ElementProperty(value = "attackPeriod", isTemplateProperty = true) double attackPeriod) {
        this.attackPeriod = attackPeriod;
        resetAttackTimer();
    }

    @Override
    public boolean shouldFire() {
        return updateAndCheckTimer();
    }
    
    @Override
    public boolean isExpended() {
    		return false;
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

    protected double getAttackPeriod() {
        return attackPeriod;
    }
}
