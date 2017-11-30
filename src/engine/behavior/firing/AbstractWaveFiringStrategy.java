package engine.behavior.firing;

import java.util.Set;

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
		return wavesLeft >= 0 && super.shouldFire();
	}

	protected abstract String chooseElementToSpawn();
	
	protected int getWavesLeft() {
		return wavesLeft;
	}

	protected void decrementWavesLeft() {
		wavesLeft--;
	}
}
