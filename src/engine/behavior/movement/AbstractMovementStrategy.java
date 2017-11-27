package engine.behavior.movement;

import java.awt.geom.Point2D;

import engine.behavior.ParameterName;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * Use abstract class instead of interface to encapsulate x and y coordinate
 * data
 *
 * @author radithya
 *
 */
public abstract class AbstractMovementStrategy implements MovementStrategy {

	// Alternative to using properties - can simply update x, y values of
	// trackingPoint in setX and setY respectively ... preferred approach?
	private DoubleProperty xCoordinate;
	private DoubleProperty yCoordinate;
	private Point2D.Double trackingPoint;

	public AbstractMovementStrategy(@ParameterName("startX") double startX, @ParameterName("startY") double startY) {
		xCoordinate = new SimpleDoubleProperty(startX);
		yCoordinate = new SimpleDoubleProperty(startY);
		Point2D.Double trackingPoint = new Point2D.Double(getX(), getY());
		xCoordinate.addListener(
				(observableValue, oldValue, newValue) -> trackingPoint.setLocation(newValue.doubleValue(), getY()));
		yCoordinate.addListener(
				(observableValue, oldValue, newValue) -> trackingPoint.setLocation(getX(), newValue.doubleValue()));
	}

	@Override
	public abstract void move();

	@Override
	public double getX() {
		return xCoordinate.get();
	}

	@Override
	public double getY() {
		return yCoordinate.get();
	}

	@Override
	public Point2D.Double getCurrentPosition() {
		return new Point2D.Double(getX(), getY());
	}

	@Override
	public Point2D.Double getPositionForTracking() {
		return trackingPoint;
	}

	@Override
	public void setX(double newXCoord) {
		xCoordinate.set(newXCoord);
	}

	@Override
	public void setY(double newYCoord) {
		yCoordinate.set(newYCoord);
	}
}
