package engine.behavior.movement;

import javafx.geometry.Point2D;

/**
 * @author mscruggs
 */
public abstract class TargetedMovementStrategy extends AbstractMovementStrategy {

    private double targetX;
    private double targetY;

    private double xVelocity;
    private double yVelocity;

    private double velocityMagnitude;

    protected TargetedMovementStrategy(double targetX, double targetY, double velocityMagnitude) {
        super();
        setTargetCoordinates(targetX, targetY);
        this.velocityMagnitude = velocityMagnitude;
        calculateVelocityComponents();
    }

    public Point2D move() {
    	calculateVelocityComponents();
        setX(this.getCurrentX()+ getXVelocity());
        setY(this.getCurrentY()+ getYVelocity());
        return getCurrentCoordinates();
    }

    protected void setTargetCoordinates(double targetX, double targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
    }

    protected double getTargetX() {
        return targetX;
    }

    protected double getTargetY() {
        return targetY;
    }


    protected double getXVelocity() {
        return xVelocity;
    }

    protected double getYVelocity() {
        return yVelocity;
    }

    protected void setXVelocity(double newXVelocity) {
        xVelocity = newXVelocity;
    }


    protected void setYVelocity(double newYVelocity) {
        yVelocity = newYVelocity;
    }

    protected void calculateVelocityComponents() {
        double angle = Math.toRadians(new Point2D(this.getCurrentX(),this.getCurrentY()).angle(targetX, targetY));
        this.xVelocity = velocityMagnitude * Math.cos(angle);
        this.yVelocity = velocityMagnitude * Math.sin(angle);
    }

    public void stop() {
        this.xVelocity = 0;
        this.yVelocity = 0;
        this.velocityMagnitude = 0;
    }
}
