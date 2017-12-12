package engine.behavior.firing;

import engine.behavior.ElementProperty;

import java.util.Collection;
import java.util.Set;

/**
 * @author Ben Schwennesen
 * @author radithya
 *
 */
public abstract class AbstractWaveFiringStrategy extends AbstractPeriodicFiringStrategy {

	private Collection<String> templatesToFire;
	private int elementsRemaining;

	public AbstractWaveFiringStrategy(
			@ElementProperty(value = "templateToFire", isTemplateProperty = true) Collection<String> templatesToFire,
			@ElementProperty(value = "spawnPeriod", isTemplateProperty = true) double spawnPeriod,
			@ElementProperty(value = "numberToSpawn", isTemplateProperty = true) int numberToSpawn) {
		super(spawnPeriod,Double.POSITIVE_INFINITY);
		if (templatesToFire.isEmpty()) {
			throw new IllegalArgumentException();
		}
		this.templatesToFire = templatesToFire;
		elementsRemaining = numberToSpawn;
	}

	protected Collection<String> getTemplatesToFire() {
		return templatesToFire;
	}

	@Override
	public String fire() {
		decrementWavesLeft();
		return chooseElementToSpawn();
	}
	
	@Override
	public boolean shouldFire(double targetLocation) {
		return !isExpended() ;
//				&& super.shouldFire(targetLocation);
	}
	
	@Override
	public boolean isExpended() {
		return elementsRemaining <= 0;
	}

	protected abstract String chooseElementToSpawn();
	
	protected int getElementsRemaining() {
		return elementsRemaining;
	}

	protected void decrementWavesLeft() {
		elementsRemaining--;
	}
}
