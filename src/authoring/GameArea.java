package authoring;

import java.util.LinkedList;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class GameArea extends Pane{
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
		this.setLayoutX(260);
		this.setLayoutY(50);
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
	
	protected void toggleGridVisibility(boolean visible) {
		grid.setVisible(visible);
	}

}
