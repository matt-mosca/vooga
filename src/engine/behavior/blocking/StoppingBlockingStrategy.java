package engine.behavior.blocking;

import engine.behavior.movement.StraightLineMovementStrategy;

/**
 * Blocking strategy for object that stops when blocked
 * 
 * @author mscruggs
 *
 */
public class StoppingBlockingStrategy extends BlockingStrategy {
	
	public StoppingBlockingStrategy(StraightLineMovementStrategy movementStrat) {
		super(movementStrat);
		// TODO Auto-generated constructor stub
	}

	public void handleBlock() {
		movementStrat.stop();
	}

}
