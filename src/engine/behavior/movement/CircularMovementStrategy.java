package engine.behavior.movement;

import javafx.geometry.Point2D;

public abstract class CircularMovementStrategy extends StraightLineMovementStrategy{

	private double angle;
	private double radius;
	private double angularVelocity;
	
	public CircularMovementStrategy(double centerX, double centerY, double radius,double initialAngle,
			double velocity) {
		super(centerX, centerY,velocity/radius);
		this.radius = radius;
		this.angle = Math.toRadians(initialAngle);
		this.angularVelocity = velocity/radius;
		setInitialLocation();
	}
	
	public void move() {
		angle += angularVelocity;
		setEndCoordinates();
		super.move();
	}
	
	private void setEndCoordinates() {
		super.setEndCoord(new Point2D(radius*Math.cos(angle),radius*Math.sin(angle)));
	}
	
	private void setInitialLocation() {
		this.setX(Math.cos(angle)*radius);
		this.setY(Math.sin(angle)*radius);
	}

}
