package engine.behavior.movement;

import engine.behavior.ParameterName;
import javafx.geometry.Point2D;

/**
 * Movement strategy for objects that move along a circular path
 * 
 * @author mscruggs
 *
 */
public abstract class CircularMovementStrategy extends StraightLineMovementStrategy{

	private double angle;
	private double radius;
	private double angularVelocity;
	
	public CircularMovementStrategy(@ParameterName("centerX") double centerX, @ParameterName("centerY") double centerY,
									@ParameterName("radius") double radius,
									@ParameterName("initialAngle") double initialAngle,
									@ParameterName("velocity") double velocity) {
		super(centerX, centerY,velocity/radius);
		this.radius = radius;
		this.angle = Math.toRadians(initialAngle);
		this.angularVelocity = velocity/radius;
		setInitialLocation();
	}
	
	public void move() {
		angle += angularVelocity;
		setEndCoordinates();
		super.move();
	}
	
	private void setEndCoordinates() {
		super.setEndCoord(radius*Math.cos(angle),radius*Math.sin(angle));
	}
	
	private void setInitialLocation() {
		this.setX(Math.cos(angle)*radius);
		this.setY(Math.sin(angle)*radius);
	}

}
