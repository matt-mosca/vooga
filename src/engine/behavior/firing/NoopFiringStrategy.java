package engine.behavior.firing;

/**
 * Represents strategy of not firing at all, applies to sprites whose firing is
 * to be disabled
 * 
 * @author tyler
 * @author radithya
 *
 */
public class NoopFiringStrategy implements FiringStrategy {

	@Override
	public String fire() {
		// won't get called because of shouldFire() returning false
		return null;
	}

	@Override
	public boolean shouldFire() {
		return false;
	}
	
	@Override
	public boolean isExpended() {
		return false;
	}
	
	@Override
	public String getAudioUrl() {
		return null;
	}
}
