package engine.behavior.movement;

import javafx.geometry.Point2D;

public abstract class StraightLineMovementStrategy extends MovementStrategy{
	protected double endX;
	protected double endY;
	protected double xVelo;
	protected double yVelo;
	protected double velocityMagnitude;
	
	
	public StraightLineMovementStrategy(double startX, double startY) {
		this(startX, startY, 0, 0);
	}
	
	public StraightLineMovementStrategy(double startX, double startY,double endX,double endY) {
		super(startX, startY);
		this.endX = endX;
		this.endY = endY;
		calculateVelocityComponents();
	}
	
	public void move() {
		setX(this.getX()+xVelo);
		setY(this.getY()+yVelo);
	}

	public abstract void handleBlock();
	
	protected void setEndCoord(Point2D updatedEndCoordinates) {
		this.endX = updatedEndCoordinates.getX();
		this.endY = updatedEndCoordinates.getY();
		calculateVelocityComponents();
	}
	
	protected void stop() {
		this.xVelo = 0;
		this.yVelo = 0;
		this.velocityMagnitude = 0;
	}
	
	protected void bounce() {
		
	}
	
	private void calculateVelocityComponents() {
		double angle = new Point2D(this.getX(),this.getY()).angle(endX, endY);
		this.xVelo = velocityMagnitude * Math.cos(angle);
		this.yVelo = velocityMagnitude * Math.sin(angle);
	}


}
