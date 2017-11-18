package sprites;

import java.util.Map;

/**
 * Generic stationary Tower that fires periodically at a specified frequency
 * 
 * @author radithya
 * @deprecated
 *
 */
public class Tower extends PeriodicallyAttackingSprite {

	public Tower(Map<String, ?> properties, String templateName) {
		super(properties, templateName);
	}

	// TODO - Need this? Or remove for greater flexibility? Want a nice way of
	// enforcing default stationary MovementStrategy
	// for towers
	/**
	 * Towers don't move, by default
	 */
	@Override
	public void move() {
	}

}
