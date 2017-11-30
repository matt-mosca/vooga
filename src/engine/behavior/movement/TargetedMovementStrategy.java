package engine.behavior.movement;

import engine.behavior.ParameterName;
import javafx.geometry.Point2D;

/**
 * @author mscruggs
 */
public class TargetedMovementStrategy extends AbstractMovementStrategy {

    private double targetX;
    private double targetY;

    private double xVelocity;
    private double yVelocity;
    private double velocityMagnitude;

    protected TargetedMovementStrategy(Point2D targetPoint,
                                       @ParameterName("velocityMagnitude") double velocityMagnitude) {
        super();
        setTargetCoordinates(targetPoint.getX(), targetPoint.getY());
        this.velocityMagnitude = velocityMagnitude;
        setVelocityComponents();
    }

    public Point2D move() {
    	setVelocityComponents();
    	if(targetReached() && removeUponCompletion()) {
    		setX(this.getTargetX());
    		setY(this.getTargetY());
    	}
    	else{
    		setX(this.getCurrentX()+ getXVelocity());
        	setY(this.getCurrentY()+ getYVelocity());
    	}
    	return getCurrentCoordinates();
    }
    
    public void stop() {
        this.xVelocity = 0;
        this.yVelocity = 0;
        this.velocityMagnitude = 0;
    }
    
    public boolean targetReached() {
    	return (Math.hypot(targetX-this.getCurrentX(), targetY-this.getCurrentY())<velocityMagnitude);
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

    protected void setVelocityComponents() {
        setVelocityComponents(Math.toRadians(new Point2D(this.getCurrentX(),
        									this.getCurrentY()).angle(targetX, targetY)));
        
    }
    
    protected void setVelocityComponents(double angle) {
    	this.xVelocity = velocityMagnitude * Math.cos(angle);
        this.yVelocity = velocityMagnitude * Math.sin(angle);
    }

    
}
