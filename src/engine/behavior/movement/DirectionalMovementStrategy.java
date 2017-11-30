package engine.behavior.movement;

import javafx.geometry.Point2D;

public class DirectionalMovementStrategy extends TargetedMovementStrategy{

	public DirectionalMovementStrategy(Point2D targetPoint, double velocityMagnitude, double angle) {
		super(targetPoint, velocityMagnitude);
		setVelocityComponents(angle);
	}
	
	public Point2D move() {
		return new Point2D(0,0);
	}
	
}
