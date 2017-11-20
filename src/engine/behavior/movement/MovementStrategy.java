package engine.behavior.movement;

/**
 * Use abstract class instead of interface to encapsulate x and y coordinate
 * data
 * 
 * @author radithya
 *
 */
public abstract class MovementStrategy {

	private double xCoord;
	private double yCoord;

	/*
	 * Deprecate in favor of exposing setX() and setY() as public methods for use
	 * from Sprite (different package)? public MovementStrategy(double startX,
	 * double startY) { xCoord = startX; yCoord = startY; }
	 */

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
		return xCoord;
	}

	/**
	 * The current yCoordinate
	 * 
	 * @return current yCoordinate
	 */
	public double getY() {
		return yCoord;
	}

	/**
	 * Set x-coordinate
	 * 
	 * @param newXCoord
	 *            x-coordinate to set to
	 */
	public void setX(double newXCoord) {
		xCoord = newXCoord;
	}

	/**
	 * Set y-coordinate
	 * 
	 * @param newYCoord
	 *            y-coordinate to set to
	 */
	public void setY(double newYCoord) {
		yCoord = newYCoord;
	}

}
