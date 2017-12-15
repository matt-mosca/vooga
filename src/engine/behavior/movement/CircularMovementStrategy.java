package engine.behavior.movement;

import engine.game_elements.ElementProperty;
import javafx.geometry.Point2D;

/**
 * Movement strategy for objects that move along a circular path
 * 
 * @author mscruggs
 *
 */
public class CircularMovementStrategy extends TargetedMovementStrategy {

	private double radius;
	private double angularVelocity;
	
	public CircularMovementStrategy(
			@ElementProperty(value = "startPoint", isTemplateProperty = false) Point2D startPoint,
			@ElementProperty(value = "radius", isTemplateProperty = true) double radius,
			@ElementProperty(value = "initialAngle", isTemplateProperty = true) double initialAngle,
			@ElementProperty(value = "velocity", isTemplateProperty = true) double velocity) {
		super(startPoint, radius * Math.cos(Math.toRadians(initialAngle)),
				radius * Math.sin(Math.toRadians(initialAngle)), radius);
		this.radius = radius;
		this.setAngle(initialAngle);
		this.angularVelocity = velocity/radius;
		//setInitialLocation();
	}
	
	/**
	 * Moves the object in a circular path 
	 * 
	 * @return The coordinates of the object in Point2D format
	 * */
	public Point2D move() {
		double angle = Math.toRadians(this.getAngle());
		angle += angularVelocity;
		setTargetCoordinates(radius * Math.cos(angle),radius * Math.sin(angle));
		setVelocityComponents(angle);
		setX(this.getCurrentX()+ getXVelocity());
		setY(this.getCurrentY()+ getYVelocity());
		setAngle(Math.toDegrees(angle));
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
