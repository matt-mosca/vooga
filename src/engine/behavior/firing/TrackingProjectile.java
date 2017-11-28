package engine.behavior.firing;

import engine.behavior.movement.MovementStrategy;
import engine.behavior.movement.TrackingMovementStrategy;

public class TrackingProjectile {
	
	private TrackingMovementStrategy movementStrategy;
	private Object target;
	
	public TrackingProjectile(TrackingMovementStrategy movementStrategy, Object target) {
		this.movementStrategy = movementStrategy;
		
		//Target most likely other sprite?
		this.target = target;
		
	}
}
