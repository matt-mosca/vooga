package engine.behavior.movement;

import java.util.ArrayList;

import engine.behavior.ParameterName;
import javafx.geometry.Point2D;

/**
 * Movement strategy for objects that move along a defined path
 * 
 * @author mscruggs
 *
 */
public abstract class PathFollowingMovementStrategy extends StraightLineMovementStrategy{
	
	private ArrayList<Point2D> coordinates;
	private int currentCoordinateIndex = 0;
	
	public PathFollowingMovementStrategy(@ParameterName("startX") double startX,
										 @ParameterName("startY") double startY,
										 @ParameterName("velocity") double velocity,
										 @ParameterName("coordinates") ArrayList<Point2D> coordinates) {
		super(startX, startY,velocity);
		this.coordinates = coordinates;
	}

	public void move() {
		super.move();
		checkIfLocationReached();
	}
	/**
	 * TODO: add check to see if location reached 
	 */
	private void checkIfLocationReached() {
		if(true) {
			currentCoordinateIndex++;
			if(currentCoordinateIndex>=coordinates.size()) {
				currentCoordinateIndex = 0;
			}
			Point2D currentTarget = coordinates.get(currentCoordinateIndex);
			this.setEndCoord(currentTarget.getX(), currentTarget.getY());
		}
	}
}

