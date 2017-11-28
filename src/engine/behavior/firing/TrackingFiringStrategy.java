package engine.behavior.firing;

public class TrackingFiringStrategy implements FiringStrategy {

	TrackingProjectile projectile;
	@Override
	public void fire() {
		projectile = new TrackingProjectile(null, null);
		
	}
	
	public TrackingProjectile getProjectile() {
		return projectile;
	}

}
