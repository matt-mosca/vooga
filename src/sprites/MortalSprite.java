package sprites;

import java.util.Map;

// TODO - DEPRECATE IN FAVOR OF MORTALCOLLIDER COLLISIONVISITOR
/**
 * Abstraction of a Sprite that can take damage and die
 * @author radithya
 * @deprecated
 *
 */
public abstract class MortalSprite extends Sprite {

	private double hitPoints;
	
	public MortalSprite(Map<String, ?> properties, String templateName) {
		super(properties);
		// Safe to assume MortalSprite is alive and active on screen upon creation?
	}

	public double getHitPoints() {
		return hitPoints;
	}
	
	public void incrementHitPoints(double hitPointsToAdd) {
		hitPoints += hitPointsToAdd;
	}
	
	public void decrementHitPoints(double hitPointsToRemove) {
		hitPoints -= hitPointsToRemove;
		if (hitPoints <= 0) {
			//die();
		}
	}
	
}
