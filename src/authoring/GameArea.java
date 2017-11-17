package authoring;

import java.util.LinkedList;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

public class GameArea extends Pane{
	private final int GRID_WIDTH = 400;
	private final int GRID_HEIGHT = 400;
	
	private PlacementGrid grid;
	private LinkedList<PathPoint> points;
	private PathPoint activePoint;
	private PathPoint headPoint;
	
	public GameArea(AuthorInterface author) {
		grid = new PlacementGrid(author, GRID_WIDTH, GRID_HEIGHT, this);
		points = new LinkedList<>();
		activePoint = null;
		
		this.getChildren().add(grid);
		toggleGridVisibility(false);
		initializeLayout();
		initializeHandlers();
	}
	
	private void initializeLayout() {
		this.setMinSize(GRID_WIDTH, GRID_HEIGHT);
		this.setStyle("-fx-background-color: #3E3F4B;");
		this.setLayoutX(260);
		this.setLayoutY(50);
		this.setFocusTraversable(true);
	}
	
	private void initializeHandlers() {
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, e->addWaypoint(e, e.getX(), e.getY()));
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
	
	protected void toggleGridVisibility(boolean visible) {
		grid.setVisible(visible);
	}

}
