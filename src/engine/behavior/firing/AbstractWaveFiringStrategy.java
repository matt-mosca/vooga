package engine.behavior.firing;

import engine.behavior.ElementProperty;
import javafx.geometry.Point2D;

import java.util.Set;

/**
 * @author Ben Schwennesen
 * @author radithya
 *
 */
public abstract class AbstractWaveFiringStrategy extends AbstractPeriodicFiringStrategy {

	private Set<String> templatesToFire;
	private int wavesLeft;

	public AbstractWaveFiringStrategy(
			@ElementProperty(value = "templateToFire", isTemplateProperty = true) Set<String> templatesToFire,
			@ElementProperty(value = "spawnPeriod", isTemplateProperty = true) double spawnPeriod,
			@ElementProperty(value = "totalWaves", isTemplateProperty = true) int totalWaves) {
		super(spawnPeriod,Double.POSITIVE_INFINITY);
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
	public boolean shouldFire(double targetLocation) {
		return !isExpended() ;
//				&& super.shouldFire(targetLocation);
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
