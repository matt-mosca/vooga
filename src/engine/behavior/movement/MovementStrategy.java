package engine.behavior.movement;

import java.awt.geom.Point2D;

/**
 * Used by game elements to handle movement according to various strategies which are selected during game authoring.
 *
 * @author Ben Schwennesen
 */
public interface MovementStrategy {

    /**
     * Move based on the specific movement strategy for the game element
     */
    void move();

    /**
     * Retrieve the current horizontal coordinate for the game element
     *
     * @return current x-coordinate
     */
    double getX();

    /**
     * Retrieve the current vertical coordinate for the game element
     *
     * @return current y-coordinate
     */
    double getY();

    /**
     * Set horizontal coordinate for the game element using this strategy object.
     *
     * @param newXCoord new x-coordinate to use
     */
    void setX(double newXCoord);

    /**
     * Set vertical coordinate for the game element using this strategy object.
     *
     * @param newYCoord new y-coordinate to use
     */
    void setY(double newYCoord);

    /**
     * Get the current coordinates of the game element using this strategy.
     *
     * @return current position as a point object
     */
    Point2D.Double getCurrentPosition();

    /**
     * Get auto-updating position of the game element using this strategy for tracking
     *
     * @return point object that changes with movement
     */
    Point2D.Double getPositionForTracking();
}
