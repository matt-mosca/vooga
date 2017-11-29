package engine.behavior.movement;

import javafx.geometry.Point2D;

/**
 * Movement strategy for stationary object
 * 
 * @author mscruggs
 *
 */
public class StationaryMovementStrategy extends AbstractMovementStrategy {

	public StationaryMovementStrategy() {
		super();
		
	}

	/**
	 * Object does not move
	 **/
	public Point2D move() { return getCurrentCoordinates(); }

	@Override
	public void handleBlock(String blockMethodName) {
		// TODO Auto-generated method stub
		
	}


}
