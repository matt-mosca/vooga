package sprites;

// TODO - Just for testing / exploration, can be removed or converted to abstract class with concrete sub-classes that have specific movement and attack strategies
/**
 * Abstraction of a moving friendly unit that attacks periodically at a
 * specified frequency
 * 
 * @author radithya
 *
 */
public class Soldier extends PeriodicallyAttackingSprite {

	public Soldier(String name, double hitPoints, double attackFrequency) {
		super(name, hitPoints, attackFrequency);
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
