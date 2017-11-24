package engine.behavior.collision;

/**
 * Produces collisions with various game elements.
 *
 * WIP Template
 *
 * @author Ben Schwennesen
 */
public abstract class AbstractVisitor implements CollisionVisitor {

    private double hitPoints;

    public AbstractVisitor(double hitPoints) {
        this.hitPoints = hitPoints;
    }

    protected double getHitPoints() {
        return hitPoints;
    }

}
