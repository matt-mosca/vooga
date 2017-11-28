package engine.behavior.firing;

public class TargetedFiringStrategy implements FiringStrategy {

	TargetedProjectile projectile;
	@Override
	public void fire() {
		projectile = new TargetedProjectile(null, null);
		
	}
	
	public TargetedProjectile getProjectile() {
		return projectile;
	}

}
