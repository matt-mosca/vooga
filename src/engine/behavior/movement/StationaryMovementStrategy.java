package engine.behavior.movement;

import engine.behavior.ParameterName;

/**
 * Movement strategy for stationary object
 * 
 * @author mscruggs
 *
 */
public class StationaryMovementStrategy extends AbstractMovementStrategy {

	public StationaryMovementStrategy(@ParameterName("startX") double startX, @ParameterName("startY") double startY) {
		super(startX, startY);
		
	}

	/**
	 * Left empty because object does not move
	 * */
	public void move() {}


}
