package engine.behavior.blocking;

import engine.behavior.movement.StraightLineMovementStrategy;
/**
 * Blocking strategy for object that bounces off of objects that block it
 * 
 * @author mscruggs
 *
 */
public class BouncingBlockingStrategy extends BlockingStrategy {

	public BouncingBlockingStrategy(StraightLineMovementStrategy movementStrat) {
		super(movementStrat);
	}

	public void handleBlock() {
		movementStrat.bounce();	
	}
	
}
