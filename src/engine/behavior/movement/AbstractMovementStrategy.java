package engine.behavior.movement;

/**
 * Use abstract class instead of interface to encapsulate x and y coordinate
 * data
 * 
 * @author radithya
 *
 */
public abstract class MovementStrategy implements IMovementStrategy {

	private double xCoordinate;
	private double yCoordinate;
	private double speed;

	public MovementStrategy(double startX, double startY, double speed) {
		xCoordinate = startX;
		yCoordinate = startY;
		this.speed = speed;
	}

	/**
	 * Move based on the specific movement strategy
	 */
	public abstract void move();

	/**
	 * What to do when blocked
	 */
	public abstract void handleBlock();

	/**
	 * The current xCoordinate
	 * 
	 * @return current xCoordinate
	 */
	public double getX() {
		return xCoordinate;
	}

	@Override
	public double getY() {
		return yCoordinate;
	}

	/**
	 * Set x-coordinate
	 * 
	 * @param newXCoord
	 *            x-coordinate to set to
	 */
	protected void setX(double newXCoord) {
		xCoordinate = newXCoord;
	}

	/**
	 * Set y-coordinate
	 * 
	 * @param newYCoord
	 *            y-coordinate to set to
	 */
	protected void setY(double newYCoord) {
		yCoordinate = newYCoord;
	}

}
