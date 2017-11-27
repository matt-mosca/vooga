package engine.behavior.movement;

import javafx.geometry.Point2D;
import sprites.Sprite;

public class TrackingMovementStrategy extends StraightLineMovementStrategy{

	private Sprite target;
	
	public TrackingMovementStrategy(double startX, double startY,double velocity,Sprite target) {
		super(startX, startY, target.getX(), target.getY(),velocity);
		this.target = target;
		// TODO Auto-generated constructor stub
	}
	
	public void move() {
		this.setEndCoord(new Point2D(target.getX(),target.getY()));
		super.move();
	}
}
