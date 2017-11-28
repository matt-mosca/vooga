package player;

import authoring.PlacementGrid;
import javafx.scene.layout.Pane;

public class PlayArea extends Pane {
	
	public PlayArea(PlayerInterface player) {
		this.setLayoutX(300);
		this.setLayoutY(50);
		this.setPrefHeight(400);
		this.setPrefWidth(400);
		this.setStyle("-fx-background-color:white");
	}

}
