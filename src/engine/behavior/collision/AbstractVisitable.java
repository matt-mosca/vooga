package engine.behavior.collision;

/**
 * Receives collisions from various game elements.
 *
 * WIP Template
 *
 * @author Ben Schwennesen
 */
public abstract class AbstractVisitable implements CollisionVisitable {

    private double healthPoints;

    public AbstractVisitable(double healthPoints) {
        this.healthPoints = healthPoints;
    }

    protected double getHealthPoints() {
        return healthPoints;
    }

    @Override
    public void accept(CollisionVisitor v) {

    }
}
