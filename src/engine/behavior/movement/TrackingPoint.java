package engine.behavior.movement;

import javafx.beans.property.DoubleProperty;

/**
 * Responsible for tracking the current position of a game element.
 *
 * @author Ben Schwennesen
 */
public class TrackingPoint {

    private DoubleProperty xCoordinate;
    private DoubleProperty yCoordinate;

    /**
     * Constructs a point object for tracking the location of a game element.
     *
     * @param xCoordinate the double property responsible for tracking the element's horizontal position
     * @param yCoordinate the double property responsible for tracking the element's vertical position
     */
    public TrackingPoint(DoubleProperty xCoordinate, DoubleProperty yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    /**
     * Get the current x-coordinate of the point.
     *
     * @return the current horizontal coordinate
     */
    public double getCurrentX() {
        return xCoordinate.get();
    }

    /**
     * Get the current y-coordinate of the point.
     *
     * @return the current vertical coordinate
     */
    public double getCurrentY() {
        return yCoordinate.get();
    }
}
