package engine.behavior.movement;

/**
 * Use abstract class instead of interface to encapsulate x and y coordinate
 * data
 * 
 * @author radithya
 *
 */
public abstract class MovementStrategy {

	private double xCoordinate;
	private double yCoordinate;

	public MovementStrategy(double startX, double startY) {
		xCoordinate = startX;
		yCoordinate = startY;
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
		return xCoordinate;
	}

	/**
	 * The current yCoordinate
	 * 
	 * @return current yCoordinate
	 */
	public double getY() {
		return yCoordinate;
	}

	/**
	 * Set x-coordinate
	 * 
	 * @param newXCoord
	 *            x-coordinate to set to
	 */
	public void setX(double newXCoord) {
		xCoordinate = newXCoord;
	}

	/**
	 * Set y-coordinate
	 * 
	 * @param newYCoord
	 *            y-coordinate to set to
	 */
	public void setY(double newYCoord) {
		yCoordinate = newYCoord;
	}

}
