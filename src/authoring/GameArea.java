package authoring;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

public class GameArea extends Pane{
	private int width = 400;
	private int height = 400;
	
	private PlacementGrid grid;
	private Path path;
	private boolean gridEnabled;
	
	public GameArea(AuthorInterface author) {
		path = new Path();
		grid = new PlacementGrid(author, width, height, path);
		this.getChildren().add(path);
		this.getChildren().add(grid);
		grid.toBack();
		
		initializeLayout();
		initializeHandlers();
	}
	
	private void initializeLayout() {
		this.setPrefSize(width, height);
		this.setStyle("-fx-background-color: #3E3F4B;");
		this.setLayoutX(260);
		this.setLayoutY(50);
	}
	
	private void initializeHandlers() {
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, e->gameAreaClicked(e));
	}
	
	private void gameAreaClicked(MouseEvent e) {
		path.addWaypoint(e, e.getX(), e.getY());
	}
	
	protected void toggleGridVisibility(boolean visible) {
		grid.setVisible(visible);
		gridEnabled = visible;
	}
	
	protected void resizeGameArea(int width, int height) {
		this.width = width;
		this.height= height;
		grid.resizeGrid(width, height);
		this.setPrefSize(width, height);
	}
	
	protected void changeBackground(String hexcode) {
		this.setStyle("-fx-background-color: " + hexcode + ";");
	}
	
	protected void placeObject(Shape shape) {
		if(gridEnabled) {
			grid.snapToGrid(shape);
		}
	}

}
