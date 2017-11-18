package authoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class PathPoint extends Circle{
	private List<PathPoint> nextPoints;
	private List<PathPoint> prevPoints;
	private Map<PathPoint,Line> linesToNext;
	private boolean active = false;
	private boolean wasDragged = false;
	
	protected PathPoint(double x, double y) {
		this.setCenterX(x);
		this.setCenterY(y);
		this.setRadius(5);
		this.setFill(Color.RED);
		nextPoints = new ArrayList<>();
		prevPoints = new ArrayList<>();
		linesToNext = new HashMap<>();
		
		initializeHandlers();
	}
	
	private void initializeHandlers() {
		this.addEventHandler(MouseEvent.MOUSE_DRAGGED, e->dragPoint(e));
	}

	protected PathLine setConnectingLine(PathPoint next) {
		nextPoints.add(next);
		PathLine line = new PathLine(this, next);
		linesToNext.put(next, line);
		next.addToPrevious(this);
		return line;
	}
	
	protected void addToPrevious(PathPoint prev) {
		prevPoints.add(prev);
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
	
	protected List<PathPoint> getPrevious(){
		return prevPoints;
	}
	
	protected List<PathPoint> getNext(){
		return nextPoints;
	}
	
	protected Map<PathPoint, Line> getNextLines(){
		return linesToNext;
	}
}
