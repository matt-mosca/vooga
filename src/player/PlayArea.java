package player;

import authoring.PlacementGrid;
import authoring.rightToolBar.SpriteImage;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import sprites.BackgroundObject;
import sprites.StaticObject;

public class PlayArea extends Pane {
	
	public PlayArea(PlayerInterface player) {
		this.setLayoutX(300);
		this.setLayoutY(50);
		this.setPrefHeight(400);
		this.setPrefWidth(400);
		this.setStyle("-fx-background-color:white");
	}
	
	protected void placeInGrid(SpriteImage currObject) {
		this.getChildren().add(currObject);
	}

}
