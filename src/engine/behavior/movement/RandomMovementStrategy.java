package engine.behavior.movement;

import engine.behavior.ParameterName;

import javafx.geometry.Point2D;

/**
 * Move in one of a set of directions, chosen randomly based on an
 * author-configured probability
 * 
 * @author radithya
 *
 */
public final class RandomMovementStrategy extends AbstractMovementStrategy {

	private Point2D chosenDirection;

	public RandomMovementStrategy(@ParameterName("northProbability") double northProbability,
								  @ParameterName("southProbability") double southProbability,
								  @ParameterName("eastProbability") double eastProbability,
								  @ParameterName("westProbability") double westProbability) {
		super();
		RandomMovementAssigner randomMovementAssigner = new RandomMovementAssigner(new double[]{northProbability,
				southProbability, eastProbability, westProbability});
		chosenDirection = randomMovementAssigner.assignMovementDirection();
		// TODO - set velocity vector to have norm == speed
	}

	@Override
	public Point2D move() {
		setX(getCurrentX() + chosenDirection.getX());
		setY(getCurrentY() + chosenDirection.getY());
		return getCurrentCoordinates();
	}
/*
	// TODO - more effective / realistic unblocking logic?
	@Override
	public void handleBlock() {
		// Naive reversing
		chosenDirection = new Point2D.Double(chosenDirection.getX() * -1, chosenDirection.getCurrentY() * -1);// TEMP
	}
*/
}
