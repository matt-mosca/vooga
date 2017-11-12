package sprites;

public abstract class MortalSprite extends Sprite {

	private double hitPoints;
	
	public MortalSprite(String name, double hitPoints) {
		super(name);
		this.hitPoints = hitPoints;
		// Safe to assume MortalSprite is alive and active on screen upon creation?
		setActive();
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
			die();
		}
	}
	
	protected void die() {
		deactivate();
	}
	
}
