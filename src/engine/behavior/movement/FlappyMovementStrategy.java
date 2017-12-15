package engine.behavior.movement;

import engine.game_elements.ElementProperty;
import javafx.geometry.Point2D;

public class FlappyMovementStrategy extends FallingMovementStrategy{
	private double verticalThrust;
	public FlappyMovementStrategy(@ElementProperty(value = "startPoint", isTemplateProperty = false) Point2D startPoint,
								  @ElementProperty(value = "initialYVelocity", isTemplateProperty = true) double initialYVelocity, 
								  @ElementProperty(value = "xVelocity", isTemplateProperty = true) double xVelocity,
								  @ElementProperty(value = "gravity", isTemplateProperty = true) double gravity,
								  @ElementProperty(value = "verticalThrust", isTemplateProperty = true) double verticalThrust) {
		super(startPoint, initialYVelocity, xVelocity, gravity);
	}
	
	public void jump() {
		super.setYVelocity(verticalThrust);
	}
	
	public static void main(String[] args) throws InterruptedException {
		FlappyMovementStrategy flap = new FlappyMovementStrategy(new Point2D(0,0),0,0,10,10);
		for(int i=0;i<600;i++) {
			flap.move();
			if(i%60==0)
				flap.jump();
			System.out.println(flap.yVelocity);
		}
	}
}
