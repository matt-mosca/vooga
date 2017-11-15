package authoring;

import java.util.LinkedList;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Polyline;


public class GridGameArea extends GridPane implements GameArea{
	private final int GRID_ROW_PERCENTAGE = 5;
	private final int GRID_COLUMN_PERCENTAGE = 5;
	private final int GRID_WIDTH = 400;
	private final int GRID_HEIGHT = 400;
	
	private Polyline path;
	private LinkedList<Point2D> points;
	private Cell[][] cells;
	
	public GridGameArea(AuthorInterface author) {
		initializeVariables();
		initializeLayout();
		initializeCells();
		initializeEventHandlers();
	}
	
	private void initializeVariables() {
		path = new Polyline();
		points = new LinkedList<>();
		cells = new Cell[(100/GRID_COLUMN_PERCENTAGE)][(100/GRID_ROW_PERCENTAGE)];
	}

	private void initializeEventHandlers() {
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, e->adjustWaypoint(e));
	}

	public void initializeLayout() {
		this.setMinSize(GRID_WIDTH, GRID_HEIGHT);
		this.setStyle("-fx-grid-lines-visible: true");
		this.setStyle("-fx-background-color: #3E3F4B;");
		this.setLayoutX(260);
		this.setLayoutY(50);
		
		for(int i = 0; i<100; i+=GRID_ROW_PERCENTAGE) {
			RowConstraints row = new RowConstraints();
			row.setPercentHeight(100/GRID_ROW_PERCENTAGE);
			this.getRowConstraints().add(row);
		}
		
		for(int i = 0; i<100; i+=GRID_COLUMN_PERCENTAGE) {
			ColumnConstraints col = new ColumnConstraints();
			col.setPercentWidth(100/GRID_COLUMN_PERCENTAGE);
			this.getColumnConstraints().add(col);
		}
	}
	
	public void initializeCells() {
		for(int i = 0; i<(100/GRID_ROW_PERCENTAGE); i++) {
			for(int j = 0; j<(100/GRID_COLUMN_PERCENTAGE);j++) {
				Cell cell = new Cell();
				this.add(cell, i, j);
				cells[i][j] = cell;
			}
		}
	}

	private void adjustWaypoint(MouseEvent e) {
		int row = (int) e.getY()/(GRID_HEIGHT/(100/GRID_ROW_PERCENTAGE));
		int col = (int) e.getX()/(GRID_WIDTH/(100/GRID_COLUMN_PERCENTAGE));
		Cell cell = cells[col][row];
		
		double x = col*(GRID_WIDTH/(100/GRID_COLUMN_PERCENTAGE)) + cell.getWidth()/2;
		double y = row*(GRID_HEIGHT/(100/GRID_ROW_PERCENTAGE)) + cell.getHeight()/2;
		Point2D waypoint = new Point2D(x, y);
		
		if(cell.pathActive()) {
			cell.deactivate();
			points.remove(waypoint);
		}else {
			cell.activate();
			points.add(waypoint);
		}
	}
}
