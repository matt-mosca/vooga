package engine.behavior.blocking;

import engine.behavior.movement.StraightLineMovementStrategy;
/**
 * Super class for all blocking strategies
 * 
 * @author mscruggs
 *
 */
public abstract class BlockingStrategy implements HandleBlocking{
	protected StraightLineMovementStrategy movementStrat;
	
	public BlockingStrategy(StraightLineMovementStrategy movementStrat) {
		this.movementStrat = movementStrat;
	}
	
	public abstract void handleBlock();
}
