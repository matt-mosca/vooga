package authoring;

import java.util.LinkedList;

import javafx.geometry.Point2D;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import sprites.StaticObject;

public class GameArea extends Pane{
	private static final int Y_LOCATION = 50;
	private static final int X_LOCATION = 260;
	private final int GRID_WIDTH = 400;
	private final int GRID_HEIGHT = 400;
	private PlacementGrid grid;
	private LinkedList<PathPoint> points;
	private PathPoint activePoint;
	
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
		this.setLayoutX(X_LOCATION);
		this.setLayoutY(Y_LOCATION);
	}
	
	private void initializeHandlers() {
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, e->addWaypoint(e, e.getX(), e.getY()));
	}
	
	protected void addWaypoint(MouseEvent e, double x, double y) {
		PathPoint point = new PathPoint(x, y);
		if(activePoint != null) this.getChildren().add(activePoint.setConnectingLine(point));
		activePoint = point;
		points.add(point);
		this.getChildren().add(point);
		e.consume();
	}
	
	
	
	protected void placeInGrid(StaticObject currObject, MouseEvent e) {
		double xLocation = e.getX() - X_LOCATION;
		double yLocation = e.getY() - Y_LOCATION;
		Point2D location = new Point2D(xLocation, yLocation);
		Point2D newLocation = grid.findClosest(location, currObject);
		currObject.setX(newLocation.getX() + X_LOCATION);
		currObject.setY(newLocation.getY() + Y_LOCATION);
		
	}
	
	protected void toggleGridVisibility(boolean visible) {
		grid.setVisible(visible);
	}

}
