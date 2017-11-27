package engine.behavior.movement;

import javafx.geometry.Point2D;
import sprites.Sprite;

public abstract class TrackingMovementStrategy extends StraightLineMovementStrategy{

	private Sprite target;
	
	public TrackingMovementStrategy(double startX, double startY,Sprite target) {
		super(startX, startY, target.getX(), target.getY());
		this.target = target;
		// TODO Auto-generated constructor stub
	}
	
	public void move() {
		this.setEndCoord(new Point2D(target.getX(),target.getY()));
		super.move();
	}
	
	public abstract void handleBlock();
}
