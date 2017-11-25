package authoring;

import java.util.ResourceBundle;

import authoring.path.Path;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import sprites.BackgroundObject;
import sprites.StaticObject;

public class GameArea extends Pane{
	private final String WIDTH = "Game_Area_Width";
	private final String HEIGHT = "Game_Area_Height";
	private final String COLOR = "Game_Area_Color";
	private final String ROW_PERCENTAGE = "Grid_Row_Percentage";
	private final String COL_PERCENTAGE = "Grid_Column_Percentage";
	
	private int width;
	private int height;
	private int rowPercentage;
	private int colPercentage;
	private String backgroundColor;
	
	private ResourceBundle gameProperties;
	private PlacementGrid grid;
	private Path path;
	private boolean gridEnabled = true;
	
	private Group backObjects;
	
	public GameArea(AuthorInterface author) {
		initializeProperties();
		initializeLayout();
		initializeHandlers();
		backObjects = new Group();
		path = new Path();
		grid = new PlacementGrid(author, width, height, rowPercentage, colPercentage, path);

		this.getChildren().add(grid);
		this.getChildren().add(backObjects);
		this.getChildren().add(path);
	}
	
	private void initializeProperties() {
		gameProperties = ResourceBundle.getBundle("authoring/resources/GameArea");
		width = Integer.parseInt(gameProperties.getString(WIDTH));
		height = Integer.parseInt(gameProperties.getString(HEIGHT));
		backgroundColor = gameProperties.getString(COLOR);
		rowPercentage = Integer.parseInt(gameProperties.getString(ROW_PERCENTAGE));
		colPercentage = Integer.parseInt(gameProperties.getString(COL_PERCENTAGE));
	}
	
	private void initializeLayout() {
		this.setPrefSize(width, height);
		this.setStyle("-fx-background-color: " + backgroundColor + ";");
	}
	
	private void initializeHandlers() {
		this.addEventHandler(MouseEvent.MOUSE_PRESSED, e->gameAreaClicked(e));
	}
	
	private void gameAreaClicked(MouseEvent e) {
		path.addWaypoint(e, e.getX(), e.getY());
	}
	
	protected void placeInGrid(StaticObject currObject) {
		if(gridEnabled) {
			Point2D newLocation = grid.place(currObject);
			currObject.setX(newLocation.getX());
			currObject.setY(newLocation.getY());
			for (Node node: backObjects.getChildren()) {
				if(!(node instanceof BackgroundObject)) node.toFront();
			}
		}
	}
	
	protected void addBackObject(StaticObject object) {
		backObjects.getChildren().add(object);
	}
	
	protected void removeBackObject(StaticObject object) {
		backObjects.getChildren().remove(object);
	}
	
	protected void toggleGridVisibility(boolean visible) {
		grid.setVisible(visible);
		gridEnabled = visible;
	}
	
	protected void toggleMovement(boolean moveable) {
		if(moveable) {
			grid.toBack();
		}else {
			backObjects.toBack();
		}
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

	public void removeFromGrid(StaticObject currObject) {
		grid.removeFromGrid(currObject);
	}
}
