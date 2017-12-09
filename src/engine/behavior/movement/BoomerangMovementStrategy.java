package engine.behavior.movement;

import javafx.geometry.Point2D;

public class BoomerangMovementStrategy extends CircularMovementStrategy{

	private Point2D startingLocation;
	public BoomerangMovementStrategy(Point2D startingPoint,double targetX, double targetY,double velocity) {
		super(startingPoint,
			  startingPoint.getX()+((targetX-startingPoint.getX())/2),
			  startingPoint.getY()+((targetY-startingPoint.getY())/2),
			  startingPoint.distance(targetX, targetY)/2,
			  startingPoint.angle(targetX,targetY),
			  velocity);
		startingLocation = startingPoint;
	}
	
	public boolean targetReached() {
		return ((startingLocation.getX() == this.getTargetX()) &&
				(startingLocation.getY() == this.getTargetY()));
	}
	
}
