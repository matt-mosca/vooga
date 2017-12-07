package authoring.bottomToolBar;

import java.awt.Color;
import java.util.List;

import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class SpriteDisplayer extends ScrollPane {
	
	private HBox myBox;
	
	public SpriteDisplayer() {
		myBox = new HBox();
		myBox.setPrefWidth(400);
		myBox.setPrefHeight(80);
		myBox.setStyle("-fx-background-color: #336699;");
		this.setContent(myBox);
	}
	
	public void addToScroll(List<ImageView> newSprites) {
		myBox.getChildren().removeAll(myBox.getChildren());
		myBox.getChildren().addAll(newSprites);
//		this.setContent(myBox);
	}
}
