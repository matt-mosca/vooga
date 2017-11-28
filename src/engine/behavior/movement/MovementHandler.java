package engine.behavior.movement;

import engine.behavior.blocking.BlockingStrategy;

import java.awt.geom.Point2D;

/**
 * Encapsulates movement behavior of game elements. Responsible for coordinating the tracking and updating the elements'
 * locations, including the handling of situations where the element is blocked by an obstacle.
 *
 * @author Ben Schwennesen
 */
public class MovementHandler {

    private MovementStrategy movementStrategy;
    //private BlockingStrategy blockingStrategy;

    public MovementHandler(MovementStrategy movementStrategy){//}, BlockingStrategy blockingStrategy) {
        this.movementStrategy = movementStrategy;
        //this.blockingStrategy = blockingStrategy;
    }

    public void move() {
        movementStrategy.move();
    }

    public void handleBlock() {
        //blockingStrategy.handleBlock();
    }

    public TrackingPoint getPositionForTracking() {
        return movementStrategy.getPositionForTracking();
    }

    public double getCurrentX() {
        return movementStrategy.getCurrentX();
    }

    public double getCurrentY() {
        return movementStrategy.getCurrentY();
    }

    public void setX(double newX) {
        movementStrategy.setX(newX);
    }

    public void setY(double newY) {
        movementStrategy.setY(newY);
    }
}
