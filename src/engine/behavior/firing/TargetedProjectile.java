package engine.behavior.firing;

import engine.behavior.movement.MovementStrategy;
import engine.behavior.movement.TargetedMovementStrategy;

public class TargetedProjectile {
	
	private TargetedMovementStrategy movementStrategy;
	private Object target;
	
	public TargetedProjectile(TargetedMovementStrategy movementStrategy, Object target) {
		this.movementStrategy = movementStrategy;
		
		//Target most likely other sprite?
		this.target = target;
		
	}
}
