package engine.behavior.movement;

/**
 * Movement strategy for stationary object
 * 
 * @author mscruggs
 *
 */
public class StationaryMovementStrategy extends MovementStrategy {

	public StationaryMovementStrategy(double startX, double startY) {
		super(startX, startY);
		
	}

	/**
	 * Left empty because object does not move
	 * */
	public void move() {}


}
