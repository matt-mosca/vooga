package engine.behavior.firing;

public class TargetedFiringStrategy implements FiringStrategy {

	HomingProjectile projectile;
	@Override
	public void fire() {
		projectile = new HomingProjectile(); 
		
	}
	
	public HomingProjectile getProjectile() {
		return projectile;
	}

}
