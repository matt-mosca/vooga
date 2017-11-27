package engine.behavior.movement;

import javafx.geometry.Point2D;

/**
 * Movement strategy for objects that movements depends on straight line movement
 * 
 * @author mscruggs
 *
 */

public class StraightLineMovementStrategy extends MovementStrategy{
	protected double endX;
	protected double endY;
	protected double xVelo;
	protected double yVelo;
	protected double velocityMagnitude;
	
	
	public StraightLineMovementStrategy(double startX, double startY,double velocity) {
		this(startX, startY, 0, 0,velocity);
	}
	
	public StraightLineMovementStrategy(double startX, double startY,double endX,double endY, double velocity) {
		super(startX, startY);
		this.endX = endX;
		this.endY = endY;
		this.velocityMagnitude = velocity;
		calculateVelocityComponents();
	}
	
	public void move() {
		setX(this.getX()+xVelo);
		setY(this.getY()+yVelo);
	}

	protected void setEndCoord(double endX, double endY) {
		this.endX = endX;
		this.endY = endY;
		calculateVelocityComponents();
	}
	
	public void stop() {
		this.xVelo = 0;
		this.yVelo = 0;
		this.velocityMagnitude = 0;
	}
	
	public void bounce() {
		
	}
	
	private void calculateVelocityComponents() {
		double angle = Math.toRadians(new Point2D(this.getX(),this.getY()).angle(endX, endY));
		this.xVelo = velocityMagnitude * Math.cos(angle);
		this.yVelo = velocityMagnitude * Math.sin(angle);
	}


}
