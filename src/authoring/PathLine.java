package authoring;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class PathLine extends Line{
	private PathPoint start;
	private PathPoint end;
	private boolean active = false;
	
	public PathLine(PathPoint start, PathPoint end) {
		this.start = start;
		this.end = end;
		this.startXProperty().bind(start.centerXProperty());
		this.startYProperty().bind(start.centerYProperty());
		this.endXProperty().bind(end.centerXProperty());
		this.endYProperty().bind(end.centerYProperty());
		this.setStroke(Color.RED);
		this.setStrokeWidth(2);
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
		start.getNext().remove(end);
		end.getPrevious().remove(start);
	}
	
	protected boolean isActive() {
		return active;
	}
}
