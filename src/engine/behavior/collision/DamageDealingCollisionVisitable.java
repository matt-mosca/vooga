package engine.behavior.collision;

import engine.behavior.ElementProperty;

/**
 * Deals damage when collided with
 * Could be used for mines, projectiles, etc.
 *
 * @author radithya
 */
public class DamageDealingCollisionVisitable implements CollisionVisitable {

    private double damageToDeal;

    public DamageDealingCollisionVisitable(
            @ElementProperty(value = "damageToDeal", isTemplateProperty = true) double damageToDeal) {
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
