package engine.behavior.movement;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomMovementAssigner {

	// TODO - make this configurable too, using properties file or constructor args?
	private final Point2D.Double[] DIRECTIONS = { new Point2D.Double(0, -1), new Point2D.Double(-1, 0),
			new Point2D.Double(0, 1), new Point2D.Double(1, 0) };
	private Random random;
	private List<Double> cumulativeMovementProbabilities = new ArrayList<>();

	public RandomMovementAssigner(double[] directionProbabilities) {
		double cumulativeProbability = 0;
		for (double directionProbability : directionProbabilities) {
			cumulativeProbability += directionProbability;
			cumulativeMovementProbabilities.add(cumulativeProbability);
		}
		random = new Random();
	}

	Point2D.Double assignMovementDirection() {
		double movementRand = random.nextDouble();
		int insertionPoint = -1 * Collections.binarySearch(cumulativeMovementProbabilities, movementRand) - 1;
		return DIRECTIONS[insertionPoint];
	}

}
