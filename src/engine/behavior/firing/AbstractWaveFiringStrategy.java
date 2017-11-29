package engine.behavior.firing;

import javafx.geometry.Point2D;

import java.util.List;
import java.util.Set;

public abstract class AbstractWaveFiringStrategy extends AbstractPeriodicFiringStrategy {

    private Set<String> templatesToFire;
    private int wavesLeft;

    public AbstractWaveFiringStrategy(Set<String> templatesToFire, double period, int totalWaves) {
        super(period);
        this.templatesToFire = templatesToFire;
        wavesLeft = totalWaves;
    }

    protected Set<String> getTemplatesToFire() {
        return templatesToFire;
    }
    
    @Override
    public boolean shouldFire() {
    		return wavesLeft >= 0 && super.shouldFire();
    }
    
    protected int getWavesLeft() {
    		return wavesLeft;
    }
    
    protected void decrementWavesLeft() {
    		wavesLeft --;
    }
}
