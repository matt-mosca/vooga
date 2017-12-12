package engine.behavior.firing;

import engine.behavior.ElementProperty;
import util.Exclude;

import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author radithya
 *
 */
public class RoundRobinWaveFiringStrategy extends AbstractWaveFiringStrategy {

	@Exclude private Iterator<String> elementChooser;
	private List<String> elementsToFire;

	public RoundRobinWaveFiringStrategy(
			@ElementProperty(value = "templatesToFire", isTemplateProperty = true) List<String> templatesToFire,
			@ElementProperty(value = "period", isTemplateProperty = true) double period,
			@ElementProperty(value = "numberToSpawn", isTemplateProperty = true) int numberToSpawn) {
		super(templatesToFire, period, numberToSpawn);
		elementChooser = templatesToFire.iterator();
		elementsToFire = templatesToFire;
	}

	@Override
	protected String chooseElementToSpawn() {
		if (elementChooser == null) {
			elementChooser = elementsToFire.iterator();
		}
		if (!elementChooser.hasNext()) {
			elementChooser = getTemplatesToFire().iterator();
		}
		return elementChooser.next();
	}

	@Override
	public String getAudioUrl() {
		return null;
	}

}
