package engine.behavior.movement;

import engine.behavior.ParameterName;
import javafx.geometry.Point2D;

/**
 * Movement strategy for objects that move along a circular path
 * 
 * @author mscruggs
 *
 */
public class CircularMovementStrategy extends TargetedMovementStrategy {

	private double angle;
	private double radius;
	private double angularVelocity;
	
	public CircularMovementStrategy(@ParameterName("centerX") double centerX, @ParameterName("centerY") double centerY,
									@ParameterName("radius") double radius,
									@ParameterName("initialAngle") double initialAngle,
									@ParameterName("velocity") double velocity) {
		super(new Point2D(radius * Math.cos(Math.toRadians(initialAngle)),radius * Math.sin(Math.toRadians(initialAngle))), radius);
		this.radius = radius;
		this.angle = Math.toRadians(initialAngle);
		this.angularVelocity = velocity/radius;
		setInitialLocation();
	}
	
	/**
	 * Moves the object in a circular path 
	 * 
	 * @return The current coordinates of the object in Point2D format
	 * */
	public Point2D move() {
		angle += angularVelocity;
		setTargetCoordinates(radius * Math.cos(angle),radius * Math.sin(angle));
		setX(this.getCurrentX()+ getXVelocity());
		setY(this.getCurrentY()+ getYVelocity());
		return getCurrentCoordinates();
	}
	
	/**
	 * Tells if the target location was reached. 
	 * Will always return false since the circle is continuous.
	 * 
	 * @return false since a circle is continuous
	 * 
	 * */
	public boolean targetReached() {
		return false;
	}
	
	/**
	 * Sets the initial starting location of the 
	 * Will always return false since the circle is continuous.
	 * 
	 * */
	private void setInitialLocation() {
		this.setX(Math.cos(angle)*radius);
		this.setY(Math.sin(angle)*radius);
	}

}
