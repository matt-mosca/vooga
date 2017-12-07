package engine.behavior.firing;

import java.util.Set;

/**
 * @author Ben Schwennesen
 * @author radithya
 *
 */
public abstract class AbstractWaveFiringStrategy extends AbstractPeriodicFiringStrategy {

	private Set<String> templatesToFire;
	private int wavesLeft;

	public AbstractWaveFiringStrategy(Set<String> templatesToFire, double period, int totalWaves) {
		super(period);
		if (templatesToFire.isEmpty()) {
			throw new IllegalArgumentException();
		}
		this.templatesToFire = templatesToFire;
		wavesLeft = totalWaves;
	}

	protected Set<String> getTemplatesToFire() {
		return templatesToFire;
	}

	@Override
	public String fire() {
		decrementWavesLeft();
		return chooseElementToSpawn();
	}
	
	@Override
	public boolean shouldFire() {
		return !isExpended() && super.shouldFire();
	}
	
	@Override
	public boolean isExpended() {
		return wavesLeft <= 0;
	}

	protected abstract String chooseElementToSpawn();
	
	protected int getWavesLeft() {
		return wavesLeft;
	}

	protected void decrementWavesLeft() {
		wavesLeft--;
	}
}
