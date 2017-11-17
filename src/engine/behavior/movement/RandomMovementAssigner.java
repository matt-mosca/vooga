package engine.behavior.movement;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomMovementAssigner {

	// TODO - make this configurable too, using properties file or constructor args?
	private final Point[] DIRECTIONS = {new Point(0, -1), new Point(-1, 0), new Point(0, 1), new Point(1, 0)};
	private Random random;
	// TODO - Consider making this a collection / variadic args?
	private List<Double> cumulativeMovementProbabilities = new ArrayList<>();
	
	public RandomMovementAssigner(double[] directionProbabilities) {
		double prevProbability = 0;
		for (double directionProbability : directionProbabilities) {
			prevProbability += directionProbability;
			cumulativeMovementProbabilities.add(prevProbability);
		}
		random = new Random();
	}
	
	Point assignMovementDirection() {
		double movementRand = random.nextDouble();
		int insertionPoint = -1 * Collections.binarySearch(cumulativeMovementProbabilities, movementRand) - 1;
		return DIRECTIONS[insertionPoint];
	}

}
