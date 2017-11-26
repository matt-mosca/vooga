package engine.behavior.movement;

import engine.behavior.ParameterName;

/**
 * Use abstract class instead of interface to encapsulate x and y coordinate data
 * 
 * @author radithya
 */
public abstract class AbstractMovementStrategy implements MovementStrategy {

	private double xCoordinate;
	private double yCoordinate;

	public AbstractMovementStrategy(@ParameterName("startX") double startX, @ParameterName("startY") double startY) {
		xCoordinate = startX;
		yCoordinate = startY;
	}

	@Override
	public abstract void move();

	@Override
	public abstract void handleBlock();

	@Override
	public double getX() {
		return xCoordinate;
	}

	@Override
	public double getY() {
		return yCoordinate;
	}

	@Override
	public void setX(double newXCoord) {
		xCoordinate = newXCoord;
	}

	@Override
	public void setY(double newYCoord) {
		yCoordinate = newYCoord;
	}

}
