package engine.behavior.movement;

import engine.behavior.ElementProperty;
import javafx.geometry.Point2D;

/**
 * Movement strategy for stationary object
 * 
 * @author mscruggs
 *
 */
public class StationaryMovementStrategy extends AbstractMovementStrategy {

	public StationaryMovementStrategy(
			@ElementProperty(value = "startPoint", isTemplateProperty = false) Point2D startPoint) {
		super(startPoint);

	}

	/**
	 * Object does not move
	 **/
	public Point2D move() { return getCurrentCoordinates(); }

	@Override
	public void handleBlock(String blockMethodName) {
		// TODO Auto-generated method stub
		
	}
}
