package authoring;

import java.util.HashSet;
import java.util.Set;

import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;

public class Path extends Group{
	private Set<PathPoint> points;
	private PathPoint activePoint;
	private PathPoint headPoint;
	
	public Path() {
		points = new HashSet<>();
		activePoint = null;
	}
	
	protected void addWaypoint(MouseEvent e, double x, double y) {
		e.consume();
		if(activePoint == null && points.size() != 0) return;
		PathPoint point = new PathPoint(x, y);
		point.addEventHandler(MouseEvent.MOUSE_CLICKED, event->handlePointClick(event, point));
		
		if(activePoint != null) {
			drawLineBetween(activePoint, point);
		}else {
			headPoint = point;
		}
		
		setActiveWaypoint(e, point);
		activePoint = point;
		points.add(point);
		this.getChildren().add(point);
	}
	
	private void handlePointClick(MouseEvent e, PathPoint point) {
		e.consume();
		if(point.wasMoved()) {
			point.lockPosition();
		}else if(e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
			connectPath(e, point);
		}else if(e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 1) {
			setActiveWaypoint(e, point);
		}else if(e.getButton() == MouseButton.SECONDARY) {
			removeWaypoint(e, point);
		}
	}
	
	private void removeWaypoint(MouseEvent e, PathPoint point) {
		points.remove(point);
		removeWaypointLines(point);
		modifyLineOrder(point);
		if(point.equals(activePoint)) activePoint = null;
		this.getChildren().remove(point);
	}
	
	private void setActiveWaypoint(MouseEvent e, PathPoint point) {
		point.toggleActive();
		if(point.equals(activePoint)) {
			activePoint = null;
		}else {
			if(activePoint != null) activePoint.toggleActive();
			activePoint = point;
		}
	}
	
	private void connectPath(MouseEvent e, PathPoint point) {
		if(!point.equals(activePoint) && activePoint != null) {
			drawLineBetween(activePoint, point);
			setActiveWaypoint(e, point);
		}
	}
	
	private void removeWaypointLines(PathPoint point) {
		for(Line line:point.getNextLines().values()) {
			this.getChildren().remove(line);
		}

		for(PathPoint prev:point.getPrevious()) {
			this.getChildren().remove(prev.getNextLines().get(point));
			prev.getNext().remove(point);
			prev.getNextLines().remove(point);
		}
		
		for(PathPoint next:point.getNext()) {
			next.getPrevious().remove(point);
		}
	}
	
	private void modifyLineOrder(PathPoint point) {
		for(PathPoint prevPoint:point.getPrevious()) {
			for(PathPoint nextPoint:point.getNext()) {
				drawLineBetween(prevPoint, nextPoint);
			}
		}
	}
	
	private void handleLineClick(MouseEvent e, PathLine line) {
		e.consume();
		if(e.getButton() == MouseButton.PRIMARY) {
			setActiveLine(line);
		}else if(e.getButton() == MouseButton.SECONDARY) {
			removeLine(line);
		}
	}

	private void setActiveLine(PathLine line) {
		line.toggleActive();
	}
	
	private void removeLine(PathLine line) {
		if(!line.isActive()) return;
		line.removeLineFromPoints();
		this.getChildren().remove(line);
	}

	private void drawLineBetween(PathPoint start, PathPoint end) {
		PathLine line = start.setConnectingLine(end);
		this.getChildren().add(line);
		line.toBack();
		line.addEventHandler(MouseEvent.MOUSE_CLICKED, e->handleLineClick(e, line));
	}
	
}
