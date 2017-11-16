package authoring;

import java.util.LinkedList;
import java.util.List;

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
				this.add(cell, j, i); //column then row for this javafx command
				cells[i][j] = cell;
			}
		}
	}

	private void adjustWaypoint(MouseEvent e) {
		int row = (int) e.getY()/(GRID_HEIGHT/(100/GRID_ROW_PERCENTAGE));
		int col = (int) e.getX()/(GRID_WIDTH/(100/GRID_COLUMN_PERCENTAGE));
		Cell cell = cells[row][col];
		
		double x = col*(GRID_WIDTH/(100/GRID_COLUMN_PERCENTAGE)) + cell.getWidth()/2;
		double y = row*(GRID_HEIGHT/(100/GRID_ROW_PERCENTAGE)) + cell.getHeight()/2;
		Point2D waypoint = new Point2D(x, y);
		
		if(cell.pathActive()) {
			cell.deactivate();
			points.remove(waypoint);
			updateNeighbors(row, col, false);
		}else {
			if(points.size()>0 && !cell.activeNeighbors()) return;
			cell.activate();
			points.add(waypoint);
			updateNeighbors(row, col,true);
		}
	}
	
	private void updateNeighbors(int row, int col, boolean addActive) {
		int[] rows = {row, row+1, row-1};
		int[] cols = {col, col+1, col-1};
		for(int r: rows) {
			for(int c:cols) {
				if(r>-1 && c>-1 && r<(cells.length) && c<(cells[0].length) && !(r==row && c==col)) {
					if(addActive) {
						cells[r][c].addActive();
					}else{
						cells[r][c].removeActive();
					}
				}
			}
		}
	}
	
	public List<Point2D> getPath() {
		return points;
	}
}
