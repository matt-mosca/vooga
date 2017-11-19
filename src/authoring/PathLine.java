package authoring;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class PathLine extends Line{
	private PathPoint start;
	private PathPoint end;
	private LineDirection direction;
	private boolean active = false;
	
	public PathLine(PathPoint start, PathPoint end) {
		this.start = start;
		this.end = end;
		this.startXProperty().bind(start.centerXProperty());
		this.startYProperty().bind(start.centerYProperty());
		this.endXProperty().bind(end.centerXProperty());
		this.endYProperty().bind(end.centerYProperty());
		this.setStroke(Color.RED);
		this.setStrokeWidth(4);
		
		direction = new LineDirection(start, end, this);
	}
	
	protected void toggleActive() {
		if(!active) {
			this.setStroke(Color.AQUAMARINE);
		}else {
			this.setStroke(Color.RED);
		}
		active = !active;
	}
	
	protected void removeLineFromPoints() {
		start.getNextLines().remove(end);
		end.getPrevLines().remove(start);
	}
	
	protected void changeDirection() {
		removeLineFromPoints();
		
		PathPoint temp = start;
		start = end;
		end = temp;
		
		direction.drawShape();
		start.getNextLines().put(end, this);
		end.getPrevLines().put(start, this);
	}
	
	protected boolean isActive() {
		return active;
	}
	
	protected LineDirection getDirectionComponent() {
		return direction;
	}
	
	protected PathPoint getStartPoint() {
		return start;
	}
	
	protected PathPoint getEndPoint() {
		return end;
	}
}
