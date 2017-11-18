package authoring;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class LineDirection extends Polygon{
	private int rotation = 0;
	
	public LineDirection(PathPoint start, PathPoint end, PathLine line) {
		drawShape(start, end);
		this.setFill(Color.RED);
	}
	
	private void drawShape(PathPoint start, PathPoint end) {
		double angle = Math.atan2(end.getCenterY()-start.getCenterY(), end.getCenterX() - start.getCenterX());
		double midx = (start.getCenterX() + end.getCenterX())/2;
		double midy = (start.getCenterY() + end.getCenterY())/2;
		
		double tipx = midx + 5*Math.cos(angle);
		double tipy = midy + 5*Math.sin(angle);
		double vert1x = midx - 5*Math.cos(angle) + 5*Math.cos(angle + (Math.PI/2));
		double vert1y = midy - 5*Math.sin(angle) + 5*Math.sin(angle + (Math.PI/2));
		double vert2x = midx - 5*Math.cos(angle) - 5*Math.cos(angle + (Math.PI/2));
		double vert2y = midy - 5*Math.sin(angle) - 5*Math.sin(angle + (Math.PI/2));
		
		this.getPoints().addAll(new Double[] {
				tipx, tipy, vert1x, vert1y, vert2x, vert2y
		});
	}
	
	protected void changeDirection() {
		rotation += 180;
		if(rotation >= 360) rotation %= 360;
		this.setRotate(rotation);
	}
}
