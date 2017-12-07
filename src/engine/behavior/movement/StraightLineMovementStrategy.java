package engine.behavior.movement;

import engine.behavior.ParameterName;
import javafx.geometry.Point2D;

/**
 * Movement strategy for objects that movements depends on straight line
 * movement
 * 
 * @author mscruggs
 *
 */

public class StraightLineMovementStrategy extends TargetedMovementStrategy {

	public StraightLineMovementStrategy(@ParameterName("targetX") double targetX,
			@ParameterName("targetY") double targetY, @ParameterName("velocity") double velocity) {
		super(new Point2D(targetX, targetY), velocity);
		setVelocityComponents();
	}

	public void bounce() {

	}

}
