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

	private final int DEFAULT_START_COORDINATE = 0;
	// Alternative to using properties - can simply update x, y values of
	// trackingPoint in setX and setY respectively ... preferred approach?
	private TrackingPoint trackingPoint;
	private DoubleProperty xCoordinate;
	private DoubleProperty yCoordinate;

	public AbstractMovementStrategy() {
		xCoordinate = new SimpleDoubleProperty(DEFAULT_START_COORDINATE);
		yCoordinate = new SimpleDoubleProperty(DEFAULT_START_COORDINATE);
		trackingPoint = new TrackingPoint(xCoordinate, yCoordinate);
	}

	@Override
	public abstract Point2D move();

	public void handleBlock(String blockMethodName) {
		move();
	}
	
	@Override
	public double getCurrentX() {
		return xCoordinate.get();
	}

	@Override
	public double getCurrentY() {
		return yCoordinate.get();
	}

	@Override
	public TrackingPoint getPositionForTracking() {
		return trackingPoint;
	}

	@Override
	public void setX(double newX) {
		xCoordinate.set(newX);
	}

	@Override
	public void setY(double newY) {
		yCoordinate.set(newY);
	}

	protected Point2D getCurrentCoordinates() {
		return new Point2D(getCurrentX(), getCurrentY());
	}
}
