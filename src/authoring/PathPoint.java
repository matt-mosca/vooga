package authoring;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class PathPoint extends Circle{
	private Map<PathPoint,PathLine> linesToPrev;
	private Map<PathPoint,PathLine> linesToNext;
	private boolean active = false;
	private boolean wasDragged = false;
	
	protected PathPoint(double x, double y) {
		this.setCenterX(x);
		this.setCenterY(y);
		this.setRadius(5);
		this.setFill(Color.RED);
		linesToPrev = new HashMap<>();
		linesToNext = new HashMap<>();
		
		initializeHandlers();
	}
	
	private void initializeHandlers() {
		this.addEventHandler(MouseEvent.MOUSE_DRAGGED, e->dragPoint(e));
	}

	protected PathLine setConnectingLine(PathPoint next) {
		PathLine line = new PathLine(this, next);
		linesToNext.put(next, line);
		next.addToPrevious(this, line);
		return line;
	}
	
	protected void addToPrevious(PathPoint prev, PathLine line) {
		linesToPrev.put(prev, line);
	}
	
	protected void lockPosition() {
		wasDragged = false;
	}
	
	protected boolean wasMoved() {
		return wasDragged;
	}
	
	private void dragPoint(MouseEvent e) {
		this.setCenterX(e.getX());
		this.setCenterY(e.getY());
		wasDragged = true;
		e.consume();
	}
	
	protected void toggleActive() {
		if(!active) {
			this.setFill(Color.AQUAMARINE);
		}else {
			this.setFill(Color.RED);
		}
		active = !active;
	}
	
	protected boolean isActive() {
		return active;
	}
	
	protected Map<PathPoint, PathLine> getPrevLines(){
		return linesToPrev;
	}
	
	protected Map<PathPoint, PathLine> getNextLines(){
		return linesToNext;
	}
}
