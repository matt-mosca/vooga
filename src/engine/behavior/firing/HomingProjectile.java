package engine.behavior.firing;

import engine.behavior.movement.MovementStrategy;

public class HomingProjectile {
	
	private MovementStrategy movementStrategy;
	private Object target;
	
	public HomingProjectile(MovementStrategy movementStrategy, Object target) {
		this.movementStrategy = movementStrategy;
		
		//Target most likely other sprite?
		this.target = target;
		
	}
}
