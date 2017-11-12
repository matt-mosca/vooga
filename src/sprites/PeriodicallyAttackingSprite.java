package sprites;

/**
 * Abstraction of a Sprite that attacks periodically at a certain frequency
 * @author radithya
 *
 */
public abstract class PeriodicallyAttackingSprite extends MortalSprite {

	private double attackFrequency;
	private long attackTimer;

	public PeriodicallyAttackingSprite(String name, double hitPoints, double attackFreqeuncy) {
		super(name, hitPoints);
		this.attackFrequency = attackFreqeuncy;
		resetAttackTimer();
	}

	@Override
	public void update() {
		if (shouldAttack()) {
			attack();
		}
	}

	// TODO - launch projectile, needs Projectile class
	@Override
	public void attack() {
	}
	
	protected boolean shouldAttack() {
		if (attackTimer == 0) {
			resetAttackTimer();
			return true;
		}
		attackTimer --;	
		return false;
	}
	
	private void resetAttackTimer() {
		attackTimer = Math.round(1.0 / attackFrequency);
	}

}
