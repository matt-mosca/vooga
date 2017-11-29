package engine.behavior.firing;

import javafx.geometry.Point2D;

import java.util.List;
import java.util.Set;

public abstract class AbstractWaveFiringStrategy extends AbstractPeriodicFiringStrategy {

    private Set<String> templatesToFire;

    public AbstractWaveFiringStrategy(Set<String> templatesToFire, double period) {
        super(period);
        this.templatesToFire = templatesToFire;
    }

    protected Set<String> getTemplatesToFire() {
        return templatesToFire;
    }
}
