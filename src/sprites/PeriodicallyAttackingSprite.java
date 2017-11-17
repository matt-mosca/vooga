package sprites;

import java.util.Map;

//TODO - DEPRECATE IN FAVOR OF FIRINGSTRATEGY
/**
 * Abstraction of a Sprite that attacks periodically at a certain frequency
 * @author radithya
 *
 */
public abstract class PeriodicallyAttackingSprite extends MortalSprite {

	private double attackPeriod;
	private double attackTimer;

	public PeriodicallyAttackingSprite(Map<String, ?> properties, String templateName) {
		super(properties, templateName);
		resetAttackTimer();
	}

	// TODO - DEPRECATE AND MOVE THE FOLLOWING INTO A FIRING STRATEGY
	@Override
	public void attack() { }
	
	protected boolean shouldAttack() {
		if (attackTimer == 0) {
			resetAttackTimer();
			return true;
		}
		attackTimer --;	
		return false;
	}
	
	private void resetAttackTimer() {
		attackTimer = attackPeriod;
	}

}
