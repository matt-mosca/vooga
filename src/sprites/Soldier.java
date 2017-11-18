package sprites;

// TODO - Just for testing / exploration, can be removed or converted to abstract class with concrete sub-classes that have specific movement and attack strategies

import java.util.Map;

/**
 * Abstraction of a moving friendly unit that attacks periodically at a
 * specified frequency
 * 
 * @author radithya
 * @deprecated
 *
 */
public class Soldier extends PeriodicallyAttackingSprite {

	public Soldier(Map<String, ?> properties, String templateName) {
		super(properties, templateName);
	}

}
