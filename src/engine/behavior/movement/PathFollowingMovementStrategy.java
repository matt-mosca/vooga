package engine.behavior.movement;

import engine.behavior.ParameterName;
import javafx.geometry.Point2D;

import java.util.ArrayList;

/**
 * Movement strategy for objects that move along a defined path
 * 
 * @author mscruggs
 *
 */
public abstract class PathFollowingMovementStrategy extends TargetedMovementStrategy {
	
	private ArrayList<Point2D> coordinates;
	private int currentCoordinateIndex = 0;
	
	public PathFollowingMovementStrategy(@ParameterName("velocity") double velocity,
										 @ParameterName("coordinates") ArrayList<Point2D> coordinates) {

		super(coordinates.get(0).getX(), coordinates.get(0).getY(), velocity);
		this.coordinates = coordinates;
	}

	public Point2D move() {
		super.move();
		checkIfLocationReached();
		return getCurrentCoordinates();
	}
	/**
	 * TODO: add check to see if location reached 
	 */
	private void checkIfLocationReached() {
		if(true) {
			currentCoordinateIndex++;
			if(currentCoordinateIndex>=coordinates.size()) {
				//currentCoordinateIndex = 0;
				stop();
			}
			Point2D currentTarget = coordinates.get(currentCoordinateIndex);
			this.setTargetCoordinates(currentTarget.getX(), currentTarget.getY());
		}
	}
}

