package engine.behavior.collision;

/**
 * Represents mortal collider behavior - takes damage or explodes upon collision
 * with damage-dealing objects
 * Can be used by towers, soldiers, etc
 * @author radithya
 *
 */
public class MortalCollider extends GenericCollider {

	private double healthPoints;

	public MortalCollider(double totalHitPoints) {
		healthPoints = totalHitPoints;
	}

	@Override
	public void visit(DamageDealingCollisionVisitable visitable) {
		setHealthPoints(getHealthPoints() - visitable.getDamageToDeal());
	}

	@Override
	public boolean isAlive() {
		return healthPoints > 0;
	}

	protected double getHealthPoints() {
		return healthPoints;
	}

	private void setHealthPoints(double newHitPoints) {
		healthPoints = newHitPoints;
	}
}
