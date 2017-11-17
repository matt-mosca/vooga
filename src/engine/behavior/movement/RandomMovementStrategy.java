package engine.behavior.movement;

import java.awt.Point;

/**
 * Move in one of a set of directions, chosen randomly based on an author-configured probability
 * @author radithya
 *
 */
public class RandomMovementStrategy extends MovementStrategy {

	private Point chosenDirection;
	
	public RandomMovementStrategy(double startX, double startY, RandomMovementAssigner randomMovementAssigner) {
		super(startX, startY);
		chosenDirection = randomMovementAssigner.assignMovementDirection();
	}
	
	@Override
	public void move() {
		setX(getX() + chosenDirection.getX());
		setY(getY() + chosenDirection.getY());
	}

}
