package sprites;

import java.util.Map;

/**
 * Generic stationary Tower that fires periodically at a specified frequency
 * @author radithya
 *
 */
public class Tower extends PeriodicallyAttackingSprite {

	public Tower(Map<String, ?> properties, String templateName) {
		super(properties, templateName);
	}

	/**
	 * Towers don't move, by default
	 */
	@Override
	public void move() {
	}

}
