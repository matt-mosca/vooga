package engine.behavior.collision;

/**
 * Represents mortal collider behavior - takes damage or explodes upon collision
 * with damage-dealing objects
 * Can be used by towers, soldiers, etc
 * @author radithya
 *
 */
public class MortalCollider extends GenericCollider {

	private double hitPoints;

	public MortalCollider(int playerId, double totalHitPoints) {
		super(playerId);
		hitPoints = totalHitPoints;
	}

	@Override
	public void visit(DamageDealingCollisionVisitable visitable) {
		setHitPoints(getHitPoints() - visitable.getDamageToDeal());
	}

	@Override
	public boolean isAlive() {
		return hitPoints > 0;
	}

	protected double getHitPoints() {
		return hitPoints;
	}

	private void setHitPoints(double newHitPoints) {
		hitPoints = newHitPoints;
	}
}
