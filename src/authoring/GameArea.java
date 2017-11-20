package authoring;


import java.util.ResourceBundle;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import sprites.StaticObject;

public class GameArea extends Pane{
	private final String WIDTH = "Game_Area_Width";
	private final String HEIGHT = "Game_Area_Height";
	private final String COLOR = "Game_Area_Color";
	private final int X_OFFSET = 260;
	private final int Y_OFFSET = 50;
	
	private int width;
	private int height;
	private String backgroundColor;
	
	private ResourceBundle gameProperties;
	private PlacementGrid grid;
	private Path path;
	private boolean gridEnabled = true;
	
	public GameArea(AuthorInterface author) {
		initializeProperties();
		initializeLayout();
		initializeHandlers();
		path = new Path();
		grid = new PlacementGrid(author, width, height, path);

		this.getChildren().add(path);
		this.getChildren().add(grid);
		grid.toBack();
	}
	
	private void initializeProperties() {
		gameProperties = ResourceBundle.getBundle("authoring/resources/GameArea");
		width = Integer.parseInt(gameProperties.getString(WIDTH));
		height = Integer.parseInt(gameProperties.getString(HEIGHT));
		backgroundColor = gameProperties.getString(COLOR);
	}
	
	private void initializeLayout() {
		this.setPrefSize(width, height);
		this.setStyle("-fx-background-color: " + backgroundColor + ";");
		this.setLayoutX(X_OFFSET);
		this.setLayoutY(Y_OFFSET);
	}
	
	private void initializeHandlers() {
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, e->gameAreaClicked(e));
	}
	
	private void gameAreaClicked(MouseEvent e) {
		path.addWaypoint(e, e.getX(), e.getY());
	}
	
	protected void placeInGrid(StaticObject currObject, MouseEvent e) {
		if(gridEnabled) {
			Point2D newLocation = grid.place(currObject, X_OFFSET, Y_OFFSET);
			currObject.setX(newLocation.getX());
			currObject.setY(newLocation.getY());
		}
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
		backgroundColor = hexcode;
	}

	public void removeFromGrid(StaticObject currObject, MouseEvent e) {
		grid.removeFromGrid(currObject, X_OFFSET, Y_OFFSET);
	}
}
