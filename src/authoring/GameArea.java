package authoring;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class GameArea extends Pane{
	private final int GRID_WIDTH = 400;
	private final int GRID_HEIGHT = 400;
	
	private PlacementGrid grid;
	private Path path;
	
	public GameArea(AuthorInterface author) {
		path = new Path();
		grid = new PlacementGrid(author, GRID_WIDTH, GRID_HEIGHT, path);
		this.getChildren().add(path);
		this.getChildren().add(grid);
		grid.toBack();
		
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
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, e->path.addWaypoint(e, e.getX(), e.getY()));
	}
	
	protected void toggleGridVisibility(boolean visible) {
		grid.setVisible(visible);
	}

}
