package authoring;

import java.util.HashSet;
import java.util.Set;

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
		point.addEventHandler(MouseEvent.MOUSE_CLICKED, event->handleClick(event, point));
		
		if(activePoint != null) {
			this.getChildren().add(activePoint.setConnectingLine(point));
		}else {
			headPoint = point;
		}
		
		setActiveWaypoint(e, point);
		activePoint = point;
		points.add(point);
		this.getChildren().add(point);
	}
	
	private void handleClick(MouseEvent e, PathPoint point) {
		e.consume();
		if(e.getButton() == MouseButton.PRIMARY) {
			setActiveWaypoint(e, point);
		}else if(e.getButton() == MouseButton.SECONDARY) {
			removeWaypoint(e, point);
		}
	}
	
	private void removeWaypoint(MouseEvent e, PathPoint point) {
		e.consume();
		points.remove(point);
		removeWaypointLines(point);
		addNewOrder(point);
		if(point.equals(activePoint)) activePoint = null;
		this.getChildren().remove(point);
	}
	
	private void setActiveWaypoint(MouseEvent e, PathPoint point) {
		e.consume();
		point.toggleActive();
		if(point.equals(activePoint)) {
			activePoint = null;
		}else {
			if(activePoint != null) activePoint.toggleActive();
			activePoint = point;
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
	
	private void addNewOrder(PathPoint point) {
		for(PathPoint prevPoint:point.getPrevious()) {
			for(PathPoint nextPoint:point.getNext()) {
				this.getChildren().add(prevPoint.setConnectingLine(nextPoint));
			}
		}
	}
	
}
