package sprites;

/**
 * Generic stationary Tower that fires periodically at a specified frequency
 * @author radithya
 *
 */
public class Tower extends PeriodicallyAttackingSprite {

	public Tower(String name, double hitPoints, double attackFrequency) {
		super(name, hitPoints, attackFrequency);
	}

	/**
	 * Towers don't move, by default
	 */
	@Override
	public void move() {
	}

}
