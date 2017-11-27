package engine.behavior.blocking;

import engine.behavior.movement.StraightLineMovementStrategy;
 
/**
 * Blocking strategy for objects that moves through other objects
 * 
 * @author mscruggs
 *
 */
public class UnstoppableBlockingStrategy extends BlockingStrategy{

	public UnstoppableBlockingStrategy(StraightLineMovementStrategy movementStrat) {
		super(movementStrat);
	}

	public void handleBlock() {
		movementStrat.move();
	}

}
