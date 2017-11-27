package engine.behavior.firing;

public class TargetedFiringStrategy implements FiringStrategy {

	HomingProjectile projectile;
	@Override
	public void fire() {
		projectile = new HomingProjectile(null, null);
		
	}
	
	public HomingProjectile getProjectile() {
		return projectile;
	}

}
