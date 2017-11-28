package engine.behavior.movement;

import engine.behavior.ParameterName;
import javafx.geometry.Point2D;

/**
 * Movement strategy for objects that movements depends on straight line movement
 * 
 * @author mscruggs
 *
 */

public class StraightLineMovementStrategy extends TargetedMovementStrategy {

	protected double velocityMagnitude;

	public StraightLineMovementStrategy(@ParameterName("velocity") double velocity) {
		this(0, 0, velocity);
	}
	
	public StraightLineMovementStrategy(@ParameterName("targetX") double targetX,
										@ParameterName("targetX") double targetY,
										@ParameterName("velocity") double velocity) {
		super(targetX, targetY, velocity);
		this.velocityMagnitude = velocity;
		calculateVelocityComponents();
	}
	
	public void move() {
		setX(this.getCurrentX()+ getXVelocity());
		setY(this.getCurrentY()+ getYVelocity());
	}

	public void stop() {
		super.stop();
		this.velocityMagnitude = 0;
	}

	public void bounce() {
		
	}
	
	private void calculateVelocityComponents() {
		double angle = Math.toRadians(new Point2D(this.getCurrentX(),this.getCurrentY()).angle(getTargetX(), getTargetY()));
		setXVelocity(velocityMagnitude * Math.cos(angle));
		setYVelocity(velocityMagnitude * Math.sin(angle));
	}

}
