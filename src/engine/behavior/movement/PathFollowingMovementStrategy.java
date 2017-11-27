package engine.behavior.movement;

import java.util.ArrayList;

import javafx.geometry.Point2D;

public abstract class PathFollowingMovementStrategy extends StraightLineMovementStrategy{
	
	private ArrayList<Point2D> coordinates;
	private int currentCoordinateIndex = 0;
	
	public PathFollowingMovementStrategy(double startX, double startY, double velocity,ArrayList<Point2D> coordinates) {
		super(startX, startY,velocity);
		this.coordinates = coordinates;
	}

	public void move() {
		this.setEndCoord(coordinates.get(currentCoordinateIndex));
		super.move();
		checkIfLocationReached();
	}
	
	private void checkIfLocationReached() {
		if(true) {
			currentCoordinateIndex++;
			if(currentCoordinateIndex>coordinates.size()) {
				currentCoordinateIndex =0;
			}
		}
	}
}

