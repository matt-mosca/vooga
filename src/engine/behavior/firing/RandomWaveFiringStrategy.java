package engine.behavior.firing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Randomly fires various types of sprites into the map based on assigned probabilities.
 *
 * @author Ben Schwennesen
 * @author radithya
 */
public class RandomWaveFiringStrategy extends AbstractWaveFiringStrategy {

    private List<String> templates;
    private List<Double> probabilities;

    public RandomWaveFiringStrategy(Map<String, Double> fireProbabilities, double attackPeriod) {
        super(fireProbabilities.keySet(), attackPeriod);
        templates = new ArrayList<>(fireProbabilities.keySet());
        double cumulativeProbability = 0;
        probabilities = new ArrayList<>();
        for (String templateName : fireProbabilities.keySet()) {
            double directionProbability = fireProbabilities.get(templateName);
            cumulativeProbability += directionProbability;
            probabilities.add(cumulativeProbability);
        }
    }

    @Override
    public String fire() {
        // Todo - comment / refactor
        double movementRand = Math.random();
        int insertionPoint = -1 * Collections.binarySearch(probabilities, movementRand) - 1;
        return templates.get(insertionPoint);
    }
}
