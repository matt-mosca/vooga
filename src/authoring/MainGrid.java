package authoring;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;


public class MainGrid extends GridPane {
	private final int GRID_ROW_PERCENTAGE = 5;
	private final int GRID_COLUMN_PERCENTAGE = 5;
	
	public MainGrid(AuthorInterface author) {
		initializeLayout();
		initializeCells();
	}

	private void initializeLayout() {
		this.setMinSize(400, 400);
		this.setStyle("-fx-background-color: #3E3F4B;");
//		this.setStyle("-fx-grid-lines-visible: true");
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
	
	private void initializeCells() {
		for(int i = 0; i<(100/GRID_ROW_PERCENTAGE); i++) {
			for(int j = 0; j<(100/GRID_COLUMN_PERCENTAGE);j++) {
				Cell cell = new Cell();
				this.add(cell, i, j);
			}
		}
	}

}
