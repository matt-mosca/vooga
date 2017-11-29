package authoring;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import sprites.BackgroundObject;
import sprites.InteractiveObject;
import authoring.path.Path;
import javafx.geometry.Point2D;

/**
 * 
 * @author PLEDGE
 *
 */
public class PlacementGrid extends GridPane {
	private Path path;
	private Cell[][] cells;
	private int width;
	private int height;
	private int rowPercentage;
	private int colPercentage;
	private int cellSize;
	
	public PlacementGrid(AuthorInterface author, int width, int height, int rowPercent, int colPercent, Path path) {
		this.width = width;
		this.height = height;
		this.path = path;
		this.rowPercentage = rowPercent;
		this.colPercentage = colPercent;
		this.cellSize = width/(100/rowPercentage);
		this.addEventHandler(MouseEvent.MOUSE_PRESSED, e->activatePath(e));
		initializeLayout();
		initializeCells();
	}

	private void initializeLayout() {
		this.setPrefSize(width, height);
		for(int i = 0; i<100; i+=rowPercentage) {
			RowConstraints row = new RowConstraints();
			row.setPercentHeight(100/rowPercentage);
			this.getRowConstraints().add(row);
		}
		
		for(int i = 0; i<100; i+=colPercentage) {
			ColumnConstraints col = new ColumnConstraints();
			col.setPercentWidth(100/colPercentage);
			this.getColumnConstraints().add(col);
		}
	}
	
	private void initializeCells() {
		cells = new Cell[(100/colPercentage)][(100/rowPercentage)];
		for(int i = 0; i<(100/rowPercentage); i++) {
			for(int j = 0; j<(100/colPercentage);j++) {
				Cell cell = new Cell();
				this.add(cell, j, i); //column then row for this javafx command
				cells[i][j] = cell;
			}
		}
	}

	private void activatePath(MouseEvent e) {
		e.consume();
		int row = (int) e.getY()/(height/(100/rowPercentage));
		int col = (int) e.getX()/(width/(100/colPercentage));
		Cell cell = cells[row][col];
		
		double x = col*(width/(100/colPercentage)) + cell.getWidth()/2;
		double y = row*(height/(100/rowPercentage)) + cell.getHeight()/2;
		
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
	
	/**
	 * Need to update it to account for different sized objects 
	 * Change the assignToCell method and do different checking for odd/set
	 */
	public Point2D place(InteractiveObject interactive) {
		double minDistance = Double.MAX_VALUE;
		Point2D finalLocation = null;
		int finalRow = 0;
		int finalColumn = 0;
		for (int r = 0; r < cells.length - interactive.getSize() + 1; r++) {
			for (int c = 0; c < cells[r].length - interactive.getSize() + 1; c++) {
				Cell currCell = cells[r][c];
				Point2D cellLocation = new Point2D(currCell.getLayoutX(), currCell.getLayoutY());
				double totalDistance = Math.abs(cellLocation.distance(interactive.center()));
				if ((totalDistance <= minDistance) && (currCell.isEmpty() &&
						(!neighborsFull(r, c, interactive.getSize()))) | 
						(interactive instanceof BackgroundObject)) {
					minDistance = totalDistance;
					finalLocation = cellLocation;
					finalRow = r;
					finalColumn = c;
				}
				
			}
		}
		assignToCells(finalRow, finalColumn, interactive);
		return finalLocation;
	}
	
	private boolean neighborsFull(int row, int col, int size) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (!cells[i+row][j+col].isEmpty()) return true;
			}
		}
		return false;
	}
	
	private void assignToCells(int finalRow, int finalCol, InteractiveObject currObject) {
		for (int i = 0; i < currObject.getSize(); i++) {
			for (int j = 0; j < currObject.getSize(); j++) {
				cells[i+finalRow][j+finalCol].assignToCell(currObject);
			}
		}
	}
	
	private void removeAssignments(int finalRow, int finalCol, InteractiveObject interactive) {
		for (int i = 0; i < interactive.getSize(); i++) {
			for (int j = 0; j < interactive.getSize(); j++) {
				cells[i + finalRow][j + finalCol].removeAssignment(interactive);
			}
		}
	}

	public void removeFromGrid(InteractiveObject interactive) {
		int col = (int) ((interactive.getX()) / cellSize);
		if (col >= 0) {
			int row = (int) ((interactive.getY()) / cellSize);
			removeAssignments(row, col, interactive);
		}

	}

	private Cell calculateCell(double x, double y) {
		int row = (int) y/(height/(100/rowPercentage));
		int col = (int) x/(width/(100/colPercentage));
		return cells[row][col];
	}

	protected void resizeGrid(int width, int height) {
		this.width = width;
		this.height = height;
		this.setPrefWidth(width);
		this.setPrefHeight(height);
	}
}
