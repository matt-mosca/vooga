package engine.behavior.blocking;

import engine.behavior.movement.StraightLineMovementStrategy;
/**
 * Super class for all blocking strategies
 * 
 * @author mscruggs
 *
 */
public abstract class BlockingStrategy implements HandleBlocking{
	
	public BlockingStrategy() {
	}
	
	public abstract void handleBlock();
}
