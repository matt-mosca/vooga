package engine.behavior.firing;

public class TargetedFiringStrategy implements FiringStrategy {

	HomingProjectile projectile;
	@Override
	public void fire() {
		projectile = new HomingProjectile(null, projectile); 
		
	}
	
	public HomingProjectile getProjectile() {
		return projectile;
	}

}
