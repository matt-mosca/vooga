package authoring;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Shape;

public class PlacementGrid extends GridPane {
	private final int GRID_ROW_PERCENTAGE = 5;
	private final int GRID_COLUMN_PERCENTAGE = 5;
	
	private Path path;
	private Cell[][] cells;
	private int width;
	private int height;
	
	public PlacementGrid(AuthorInterface author, int width, int height, Path path) {
		this.width = width;
		this.height = height;
		this.path = path;
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, e->activatePath(e));
		
		initializeLayout();
		initializeCells();
	}

	private void initializeLayout() {
		this.setPrefSize(width, height);
		
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
	
	private void initializeCells() {
		cells = new Cell[(100/GRID_COLUMN_PERCENTAGE)][(100/GRID_ROW_PERCENTAGE)];
		for(int i = 0; i<(100/GRID_ROW_PERCENTAGE); i++) {
			for(int j = 0; j<(100/GRID_COLUMN_PERCENTAGE);j++) {
				Cell cell = new Cell();
				this.add(cell, j, i); //column then row for this javafx command
				cells[i][j] = cell;
			}
		}
	}

	private void activatePath(MouseEvent e) {
		e.consume();
		int row = (int) e.getY()/(height/(100/GRID_ROW_PERCENTAGE));
		int col = (int) e.getX()/(width/(100/GRID_COLUMN_PERCENTAGE));
		Cell cell = cells[row][col];
		
		double x = col*(width/(100/GRID_COLUMN_PERCENTAGE)) + cell.getWidth()/2;
		double y = row*(height/(100/GRID_ROW_PERCENTAGE)) + cell.getHeight()/2;
		
		if(cell.pathActive()) {
			cell.deactivate();
		}else {
			path.addWaypoint(e,x,y);
			cell.activate();
		}
	}
	
	//Currently unused, might eventually change state when objects are placed in cells
	protected void updateCells(double x, double y) {
		Cell cell = calculateCell(x,y);
		if(cell.pathActive()) {
			return;
		}else {
			cell.activate();
		}
	}
	
	private Cell calculateCell(double x, double y) {
		int row = (int) y/(height/(100/GRID_ROW_PERCENTAGE));
		int col = (int) x/(width/(100/GRID_COLUMN_PERCENTAGE));
		return cells[row][col];
	}

	protected void resizeGrid(int width, int height) {
		this.width = width;
		this.height = height;
		this.setPrefWidth(width);
		this.setPrefHeight(height);
	}
	
	protected void snapToGrid(Shape shape) {
		
	}
}
