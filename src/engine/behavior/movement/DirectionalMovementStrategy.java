package engine.behavior.movement;

import javafx.geometry.Point2D;

public class DirectionalMovementStrategy extends TargetedMovementStrategy{

	private double angle;
	private final double DEFAULT_STARTING_POSITION = -1;
	
	public DirectionalMovementStrategy(Point2D startingLocation,double velocityMagnitude, double angle) {
		super(startingLocation,new Point2D(-1,-1), velocityMagnitude);
		this.angle = Math.toRadians(angle);
		setVelocityComponents(this.angle);
	}
	
	public Point2D move() {
		setVelocityComponents(angle);
		setX(getCurrentX() + getXVelocity());
		setY(getCurrentY() + getYVelocity());
		return getCurrentCoordinates();
	}
	
	public boolean targetReached() {
		return false;
	}
	
	public boolean removeUponCompletion() {
		return false;
	}
	
	public void setAngle(double angle) {
		this.angle = Math.toRadians(angle);
	}
	
	public double getAngle() {
		return angle;
	}
	
	public void incrementAngle(double angleIncrement) {
		this.angle += Math.toRadians(angleIncrement);
	}
	
}
