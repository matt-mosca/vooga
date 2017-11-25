package engine.behavior.movement;

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
     * Handle situations where the game element is blocked by an obstacle
     */
    void handleBlock();

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
     * Set veritcal coordinate for the game element using this strategy object.
     *
     * @param newYCoord new y-coordinate to use
     */
    void setY(double newYCoord);
}
