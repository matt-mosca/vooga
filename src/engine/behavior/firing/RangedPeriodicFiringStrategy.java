package engine.behavior.firing;

import engine.behavior.ParameterName;
import javafx.geometry.Point2D;

/**
 * Shoots periodically (once every x number of cycles, where x is any positive
 * integer) with limited range
 * 
 * @author tyler
 * @author radithya
 *
 */
public class RangedPeriodicFiringStrategy extends PeriodicFiringStrategy {

	private double range;

	public RangedPeriodicFiringStrategy(@ParameterName("projectileTemplate") String projectileTemplate,
			@ParameterName("attackPeriod") double attackPeriod, @ParameterName("range") double range) {
		super(projectileTemplate, attackPeriod);
		this.range = range;
	}

	@Override
	public boolean shouldFire() {
		return super.shouldFire() && targetIsWithinRange(getTargetPoint());
	}

	// check if distance to point is <= range
	private boolean targetIsWithinRange(Point2D target) {
		return target.distance(getPosition()) <= range;
	}

}
