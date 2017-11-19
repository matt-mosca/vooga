package authoring;

import java.util.LinkedList;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Rectangle;
import sprites.StaticObject;
import javafx.geometry.Point2D;

/**
 * 
 * @author PLEDGE
 *
 */
public class PlacementGrid extends GridPane {
	private final int GRID_ROW_PERCENTAGE = 5;
	private final int GRID_COLUMN_PERCENTAGE = 5;
	
	private GameArea gameArea;
	private Cell[][] cells;
	private int width;
	private int height;
	private int pathNumber = 0;
	
	public PlacementGrid(AuthorInterface author, int width, int height, GameArea area) {
		this.width = width;
		this.height = height;
		this.gameArea = area;
		cells = new Cell[(100/GRID_COLUMN_PERCENTAGE)][(100/GRID_ROW_PERCENTAGE)];
		
		initializeLayout();
		initializeCells();
		initializeEventHandlers();
	}

	private void initializeLayout() {
		this.setMinSize(width, height);
		this.setStyle("-fx-background-color: #3E3F4B;");
		
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
		for(int i = 0; i<(100/GRID_ROW_PERCENTAGE); i++) {
			for(int j = 0; j<(100/GRID_COLUMN_PERCENTAGE);j++) {
				Cell cell = new Cell();
				this.add(cell, j, i); //column then row for this javafx command
				cells[i][j] = cell;
			}
		}
	}
	
	private void initializeEventHandlers() {
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, e->activatePath(e));
	}

	private void activatePath(MouseEvent e) {
		int row = (int) e.getY()/(height/(100/GRID_ROW_PERCENTAGE));
		int col = (int) e.getX()/(width/(100/GRID_COLUMN_PERCENTAGE));
		Cell cell = cells[row][col];
		
		double x = col*(width/(100/GRID_COLUMN_PERCENTAGE)) + cell.getWidth()/2;
		double y = row*(height/(100/GRID_ROW_PERCENTAGE)) + cell.getHeight()/2;
		gameArea.addWaypoint(e,x,y);
		
		if(cell.pathActive()) {
			cell.deactivate();
			updateNeighbors(row, col, false);
			pathNumber--;
		}else {
			if(pathNumber>0 && !cell.activeNeighbors()) return;
			cell.activate();
			updateNeighbors(row, col,true);
			pathNumber++;
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
	
	/**
	 * Need to update it to account for different sized objects 
	 * Change the assignToCell method and do different checking for odd/set
	 */
	public Point2D findClosest(StaticObject currObject, double xLocation, double yLocation) {
		double minDistance = Double.MAX_VALUE;
		Point2D finalLocation = null;
		int finalRow = 0;
		int finalColumn = 0;
		for (int r = 0; r < cells.length - currObject.getSize() + 1; r++) {
			for (int c = 0; c < cells[r].length - currObject.getSize() + 1; c++) {
				Cell currCell = cells[r][c];
				Point2D cellLocation = new Point2D(currCell.getLayoutX() + xLocation, 
						currCell.getLayoutY() + yLocation);
				double totalDistance = Math.abs(cellLocation.distance(currObject.center()));
				if (totalDistance <= minDistance && currCell.isEmpty()) {
					minDistance = totalDistance;
					finalLocation = cellLocation;
					finalRow = r;
					finalColumn = c;
				}
				
			}
		}
		assignToCells(finalRow, finalColumn, currObject);
		return finalLocation;
	}
	
	private void assignToCells(int finalRow, int finalColumn, StaticObject currObject) {

		for (int i = 0; i < currObject.getSize(); i++) {
			for (int j = 0; j < currObject.getSize(); j++) {
				System.out.println(finalRow + i);
				System.out.println(finalColumn + j);
				cells[finalRow + i][finalColumn + j].assignToCell(currObject);
			}
			
		}
	}
	
//	
//	public List<Point2D> getPath() {
//		return points;
//	}
}
