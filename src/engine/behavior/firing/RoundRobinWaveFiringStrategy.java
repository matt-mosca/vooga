package engine.behavior.firing;

import java.util.Iterator;
import java.util.Set;

public class RoundRobinWaveFiringStrategy extends AbstractWaveFiringStrategy {

	Iterator<String> elementChooser;

	public RoundRobinWaveFiringStrategy(Set<String> templatesToFire, double period, int totalWaves) {
		super(templatesToFire, period, totalWaves);
		elementChooser = templatesToFire.iterator();
	}

	@Override
	protected String chooseElementToSpawn() {
		if (!elementChooser.hasNext()) {
			elementChooser = getTemplatesToFire().iterator();
		}
		return elementChooser.next();
	}

}
