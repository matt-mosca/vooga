package engine.behavior.movement;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;

/**
 * Use abstract class instead of interface to encapsulate x and y coordinate
 * data
 *
 * @author radithya
 *
 */
public abstract class AbstractMovementStrategy implements MovementStrategy {

	private final int DEFAULT_START_COORDINATE = -1;
	// Alternative to using properties - can simply update x, y values of
	// trackingPoint in setX and setY respectively ... preferred approach?
	private TrackingPoint trackingPoint;

	public AbstractMovementStrategy() {
		DoubleProperty xCoordinate = new SimpleDoubleProperty(DEFAULT_START_COORDINATE);
		DoubleProperty yCoordinate = new SimpleDoubleProperty(DEFAULT_START_COORDINATE);
		trackingPoint = new TrackingPoint(xCoordinate, yCoordinate);
	}

	/**
	 * Moves an object and returns its new location
	 * 
	 * @return objects new location
	 * */
	public abstract Point2D move();

	/**
	 * How the movement handler will handle being blocked by another object 
	 * 
	 * @param New desired x coordinate
	 * */
	public void handleBlock(String blockMethodName) {
		move();
	}
	
	/**
	 * Get the current X coordinate
	 * 
	 * @return Current X coordinate
	 * */
	public double getCurrentX() {
		return trackingPoint.getCurrentX();
	}

	/**
	 * Get the current Y coordinate
	 * 
	 * @return Current Y coordinate
	 * */
	public double getCurrentY() {
		return trackingPoint.getCurrentY();
	}

	/**
	 * Get the X and Y values for tracking
	 * 
	 * @return Tracking point of this object
	 * */
	public TrackingPoint getPositionForTracking() {
		return trackingPoint;
	}

	/**
	 * Set the X coordinate 
	 * 
	 * @param New desired x coordinate
	 * */
	public void setX(double newX) {
		trackingPoint.setX(newX);
	}

	/**
	 * Set the Y coordinate 
	 * 
	 * @param New desired y coordinate
	 * */
	public void setY(double newY) {
		trackingPoint.setY(newY);
	}
	
	/**
	 * Returns if the target was reached.
	 * By default, will return false.
	 * 
	 * @return false since target is not reached
	 * */
	public boolean targetReached() {
		return false;
	}

	/**
	 * Returns if the object should be removed when it reached its target
	 * 
	 * @return True if target should be removed, false if it should not
	 * */
	public boolean removeUponCompletion() {
		return true;
	}
	
	/**
	 * Get the current coordinates of the object
	 * 
	 * @return Current coordinates of object
	 * */
	protected Point2D getCurrentCoordinates() {
		return new Point2D(getCurrentX(), getCurrentY());
	}
}
