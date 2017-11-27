package engine.behavior.collision;

import engine.behavior.ParameterName;

/**
 * Deals damage when collided with
 * Could be used for mines, projectiles, etc.
 * @author radithya
 *
 */
public class DamageDealingCollisionVisitable implements CollisionVisitable {

	private double damageToDeal;
	
	public DamageDealingCollisionVisitable(@ParameterName("damageToDeal") double damageToDeal) {
		this.damageToDeal = damageToDeal;
	}

	@Override
	public void accept(CollisionVisitor v) {
		v.visit(this);
	}

	double getDamageToDeal() {
		return damageToDeal;
	}
	
}
