package engine.behavior.movement;

import java.awt.geom.Point2D;

/**
 * Move in one of a set of directions, chosen randomly based on an
 * author-configured probability
 * 
 * @author radithya
 *
 */
public final class RandomMovementStrategy extends MovementStrategy {

	private Point2D.Double chosenDirection;

	public RandomMovementStrategy(double startX, double startY, RandomMovementAssigner randomMovementAssigner) {
		super(startX, startY);
		chosenDirection = randomMovementAssigner.assignMovementDirection();
	}

	@Override
	public void move() {
		setX(getX() + chosenDirection.getX());
		setY(getY() + chosenDirection.getY());
	}
/*
	// TODO - more effective / realistic unblocking logic?
	@Override
	public void handleBlock() {
		// Naive reversing
		chosenDirection = new Point2D.Double(chosenDirection.getX() * -1, chosenDirection.getY() * -1);// TEMP
	}
*/
}
