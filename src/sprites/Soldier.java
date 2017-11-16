package sprites;

// TODO - Just for testing / exploration, can be removed or converted to abstract class with concrete sub-classes that have specific movement and attack strategies

import java.util.Map;

/**
 * Abstraction of a moving friendly unit that attacks periodically at a
 * specified frequency
 * 
 * @author radithya
 *
 */
public class Soldier extends PeriodicallyAttackingSprite {

	public Soldier(Map<String, ?> properties, String templateName) {
		super(properties, templateName);
	}

	@Override
	public void update() {
		move();
	}

	/**
	 * Generic movement along velocity vector, can be overridden by sub-classes
	 * which have specific path-finding logic
	 */
	@Override
	public void move() {
		setX(getX() + getXVelocity());
		setY(getY() + getYVelocity());
	}

	// Sub-classes could override attack strategy as necessary

}
