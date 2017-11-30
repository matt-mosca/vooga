package player;

import authoring.PlacementGrid;
import authoring.rightToolBar.SpriteImage;
import engine.play_engine.PlayController;
import interfaces.Droppable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import sprites.BackgroundObject;
import sprites.InteractiveObject;
import sprites.StaticObject;

public class PlayArea extends Pane implements Droppable{
	private PlayController myController;
	
	public PlayArea(PlayController controller, int width, int height) {
		myController = controller;
		this.setLayoutX(310);
		this.setLayoutY(10);
//		this.setLayoutY(50);
		this.setPrefHeight(width);
		this.setPrefWidth(height);
		this.getStylesheets().add("player/resources/playerPanes.css");
		this.getStyleClass().add("play-area");
//		this.setStyle("-fx-background-color:white");
	}
	
	protected void placeInGrid(InteractiveObject currObject) {
		this.getChildren().add(currObject);
//		myController.placeElement(currObject.getElementName(), new Point2D(currObject.getX(), currObject.getY()));
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
