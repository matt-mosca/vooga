package engine.behavior.movement;

import engine.behavior.ParameterName;
import javafx.geometry.Point2D;
import sprites.Sprite;

/**
 * Movement strategy for objects that track another sprite.
 *
 * TODO - change to use target's Point2D.Double (prob)
 * 
 * @author mscruggs
 *
 */
public class TrackingMovementStrategy extends StraightLineMovementStrategy{

	private Sprite target;
	
	public TrackingMovementStrategy(@ParameterName("startX") double startX,
									@ParameterName("startX") double startY,
									@ParameterName("startX") double velocity,
									@ParameterName("target") Sprite target) {
		super(startX, startY, target.getX(), target.getY(),velocity);
		this.target = target;
		// TODO Auto-generated constructor stub
	}
	
	public void move() {
		this.setEndCoord(target.getX(),target.getY());
		super.move();
	}
}
