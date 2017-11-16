package behavior;

/**
 * Use abstract class instead of interface to encapsulate x and y coordinate data
 * @author radithya
 *
 */
public abstract class MovementStrategy {
	
	private double xCoord;
	private double yCoord;
	
	public MovementStrategy(double startX, double startY) {
		xCoord = startX;
		yCoord = startY;
	}
	
	public abstract void move();

	
	public double getX() {
		return xCoord;
	}
	
	public double getY() {
		return yCoord;
	}
	
	protected void setX(double newXCoord) {
		xCoord = newXCoord;
	}
	
	protected void setY(double newYCoord) {
		yCoord = newYCoord;
	}
	
}
