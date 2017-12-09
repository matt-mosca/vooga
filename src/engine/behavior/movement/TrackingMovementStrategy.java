package engine.behavior.movement;

import engine.behavior.ParameterName;
import javafx.geometry.Point2D;

/**
 * Movement strategy for objects that track another sprite.
 *
 * TODO - change to use target's Point2D.Double (prob)
 * 
 * @author mscruggs
 *
 */
public class TrackingMovementStrategy extends TargetedMovementStrategy {

	// can't be set in constructor in order for reflexive creation of sprites to work
	private TrackingPoint targetLocation;
	
	public TrackingMovementStrategy(Point2D startingPoint,TrackingPoint targetLocation, @ParameterName("velocity") double velocity) {
		super(startingPoint,new Point2D(targetLocation.getCurrentX(),targetLocation.getCurrentY()), velocity);
		this.targetLocation = targetLocation;
	}

	public void setTargetLocation(TrackingPoint targetLocation) {
		this.targetLocation = targetLocation;
	}
	
	public Point2D move() {
		this.setTargetCoordinates(targetLocation.getCurrentX(), targetLocation.getCurrentY());
		return super.move();
	}
}
