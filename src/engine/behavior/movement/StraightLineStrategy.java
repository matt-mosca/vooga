package engine.behavior.movement;

import engine.behavior.ParameterName;
import javafx.geometry.Point2D;

public class StraightLineStrategy extends AbstractMovementStrategy {

    private Point2D velocity;

    public StraightLineStrategy(@ParameterName("startX")  double startX, @ParameterName("startY") double startY,
                                @ParameterName("speed")   double speed,  @ParameterName("targetX") double targetX,
                                @ParameterName("targetY") double targetY) {
        super(startX, startY);
        velocity = new Point2D(targetX, targetY);
        velocity.normalize();
        velocity.multiply(Math.sqrt(speed));
    }

    @Override
    public void move() {
        setX(getX() + velocity.getX());
        setY(getX() + velocity.getY());
    }

    @Override
    public void handleBlock() {
        // TODO
    }
}
