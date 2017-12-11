package engine.behavior.firing;

import engine.behavior.ElementProperty;
import javafx.geometry.Point2D;

/**
 * Periodically fires projectiles.
 *
 * @author Ben Schwennesen
 * @author radithya
 */
public abstract class AbstractPeriodicFiringStrategy implements FiringStrategy {

    private double attackPeriod;
    private double attackCounter;
    private double range;

    public AbstractPeriodicFiringStrategy(
            @ElementProperty(value = "attackPeriod", isTemplateProperty = true) double attackPeriod,
            @ElementProperty(value = "firingRange", isTemplateProperty = true) double range) {
        this.attackPeriod = attackPeriod;
        this.range = range;
        resetAttackTimer();
    }

    @Override
    public boolean shouldFire(double distanceToTarget) {
    	System.out.println("TOWER_RANGE = "+range);
    	System.out.println("Timer="+updateAndCheckTimer());
    	System.out.println(distanceToTarget);
    	System.out.println("Range="+checkIfWithinRange(distanceToTarget));
    	//System.out.println(updateAndCheckTimer() && checkIfWithinRange(distanceToTarget));
        return updateAndCheckTimer() && checkIfWithinRange(distanceToTarget);
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
    
    private boolean checkIfWithinRange(double distanceToTarget) {
    	return (distanceToTarget<=range);
    }
}
