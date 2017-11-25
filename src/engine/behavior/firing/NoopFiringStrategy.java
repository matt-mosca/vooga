package engine.behavior.firing;

public class NoopFiringStrategy implements FiringStrategy {

	public NoopFiringStrategy() {

	}

	// Just don't fire - applicable to sprites which don't / can't fire, like
	// projectiles, mines, obstacles, etc.
	@Override
	public void fire() {
	}

}
