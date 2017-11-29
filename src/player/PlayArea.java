package player;

import authoring.PlacementGrid;
import authoring.rightToolBar.SpriteImage;
import interfaces.Droppable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import sprites.BackgroundObject;
import sprites.InteractiveObject;
import sprites.StaticObject;

public class PlayArea extends Pane implements Droppable{
	
	public PlayArea(PlayerInterface player, int width, int height) {
		this.setLayoutX(350);
//		this.setLayoutY(50);
		this.setPrefHeight(width);
		this.setPrefWidth(height);
		this.setStyle("-fx-background-color:white");
	}
	
	protected void placeInGrid(InteractiveObject currObject) {
		this.getChildren().add(currObject);
	}

	@Override
	public void droppedInto(InteractiveObject interactive) {
		placeInGrid(interactive);
	}

	@Override
	public void objectRemoved(InteractiveObject interactive) {
		this.getChildren().remove(interactive);
	}

	@Override
	public void freeFromDroppable(InteractiveObject interactive) {
		// TODO Auto-generated method stub
		
	}

}
