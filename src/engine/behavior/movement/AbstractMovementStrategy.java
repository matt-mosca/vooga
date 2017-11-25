package engine.behavior.movement;

/**
 * Use abstract class instead of interface to encapsulate x and y coordinate data
 * 
 * @author radithya
 */
public abstract class AbstractMovementStrategy implements MovementStrategy {

	private double xCoordinate;
	private double yCoordinate;
	private double speed;

	public AbstractMovementStrategy(double startX, double startY, double speed) {
		xCoordinate = startX;
		yCoordinate = startY;
		this.speed = speed;
	}

	@Override
	public abstract void move();

	@Override
	public abstract void handleBlock();

	/**
	 * Retrieve speed.
	 *
	 * @return the speed at which the object using this strategy should use.
	 */
	protected double getSpeed() { return speed; }

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
