package engine.behavior.movement;

import java.awt.geom.Point2D;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * Use abstract class instead of interface to encapsulate x and y coordinate
 * data
 * 
 * @author radithya
 *
 */
public abstract class MovementStrategy {

	// Alternative to using properties - can simply update x, y values of
	// trackingPoint in setX and setY respectively ... preferred approach?
	private DoubleProperty xCoordinate;
	private DoubleProperty yCoordinate;
	private Point2D.Double trackingPoint;

	public MovementStrategy(double startX, double startY) {
		xCoordinate = new SimpleDoubleProperty(startX);
		yCoordinate = new SimpleDoubleProperty(startY);
		Point2D.Double trackingPoint = new Point2D.Double(getX(), getY());
		xCoordinate.addListener(
				(observableValue, oldValue, newValue) -> trackingPoint.setLocation(newValue.doubleValue(), getY()));
		yCoordinate.addListener(
				(observableValue, oldValue, newValue) -> trackingPoint.setLocation(getX(), newValue.doubleValue()));
	}

	/**
	 * Move based on the specific movement strategy
	 */
	public abstract void move();

	/**
	 * The current xCoordinate
	 * 
	 * @return current xCoordinate
	 */
	public double getX() {
		return xCoordinate.get();
	}

	/**
	 * The current yCoordinate
	 * 
	 * @return current yCoordinate
	 */
	public double getY() {
		return yCoordinate.get();
	}

	/**
	 * The current (x, y) position as a Point2D.Double
	 * 
	 * @return current position
	 */
	public Point2D.Double getCurrentPosition() {
		return new Point2D.Double(getX(), getY());
	}

	/**
	 * Auto-updating (NOT snapshot) position of this MovementStrategy for tracking
	 * 
	 * @return auto-updating position that changes with movement
	 */
	public Point2D.Double getPositionForTracking() {
		return trackingPoint;
	}

	/**
	 * Set x-coordinate
	 * 
	 * @param newXCoord
	 *            x-coordinate to set to
	 */
	public void setX(double newXCoord) {
		xCoordinate.set(newXCoord);
	}

	/**
	 * Set y-coordinate
	 * 
	 * @param newYCoord
	 *            y-coordinate to set to
	 */
	public void setY(double newYCoord) {
		yCoordinate.set(newYCoord);
	}

}
