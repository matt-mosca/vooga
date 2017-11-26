package engine.behavior.movement;

import engine.behavior.ParameterName;

import java.awt.geom.Point2D;

/**
 * Move in one of a set of directions, chosen randomly based on an
 * author-configured probability
 * 
 * @author radithya
 *
 */
public final class RandomMovementStrategy extends AbstractMovementStrategy {

	private Point2D.Double chosenDirection;

	public RandomMovementStrategy(@ParameterName("startX") double startX, @ParameterName("startY") double startY,
								  @ParameterName("directionProbabilities") double[] directionProbabilities) {
		super(startX, startY);
		RandomMovementAssigner randomMovementAssigner = new RandomMovementAssigner(directionProbabilities);
		chosenDirection = randomMovementAssigner.assignMovementDirection();
		// TODO - set velocity vector to have norm == speed
	}

	@Override
	public void move() {
		setX(getX() + chosenDirection.getX());
		setY(getY() + chosenDirection.getY());
	}

	// TODO - more effective / realistic unblocking logic?
	@Override
	public void handleBlock() {
		// Naive reversing
		chosenDirection = new Point2D.Double(chosenDirection.getX() * -1, chosenDirection.getY() * -1);// TEMP
	}

}
