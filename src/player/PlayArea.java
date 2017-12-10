package player;

import util.path.Path;
import util.protocol.ClientMessageUtils;
import engine.PlayModelController;

import java.util.Map;

import display.interfaces.Droppable;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import display.sprites.InteractiveObject;

public class PlayArea extends Pane implements Droppable {
	private PlayModelController myController;
	private double lastX;
	private double lastY;
	private ClientMessageUtils clientMessageUtils;

	public PlayArea(PlayModelController controller, ClientMessageUtils clientMessageUtils, int width, int height) {
		myController = controller;
		this.clientMessageUtils = clientMessageUtils;
		this.setLayoutX(310);
		this.setLayoutY(10);
		// this.setLayoutY(50);
		this.setPrefHeight(width);
		this.setPrefWidth(height);
		this.getStylesheets().add("player/resources/playerPanes.css");
		this.getStyleClass().add("play-area");
		// this.setStyle("-fx-background-color:white");
	}

	protected void placeInGrid(InteractiveObject currObject) {
		lastX = currObject.getX();
		lastY = currObject.getY();
		clientMessageUtils.addNewSpriteToDisplay((myController.placeElement(currObject.getElementName(),
				new Point2D(currObject.getX(), currObject.getY()))));
	}

	@Override
	public void droppedInto(InteractiveObject interactive) {
		if (!interactive.intersects(this.getLayoutBounds())) {
			interactive.setX(lastX);
			interactive.setY(lastY);
		} else {
			placeInGrid(interactive);
		}
	}

	@Override
	public void objectRemoved(InteractiveObject interactive) {
		this.getChildren().remove(interactive);
	}

	@Override
	public void freeFromDroppable(InteractiveObject interactive) {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<Path, Color> getPaths() {
		return null;
	}

}
