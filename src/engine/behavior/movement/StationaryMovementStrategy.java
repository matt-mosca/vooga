package engine.behavior.movement;

import engine.behavior.ParameterName;

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
	 * Left empty because object does not move
	 * */
	public void move() {}

	@Override
	public void handleBlock(String blockMethodName) {
		// TODO Auto-generated method stub
		
	}


}
