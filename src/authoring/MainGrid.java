package authoring;

import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MainGrid extends Rectangle {
	/**
	 * Hey Ben,
	 * Do some stuff here. Make a pretty grid or some cute paths idk it's up to you.
	 * Idk if you wanna use something else but I just made it an HBox
	 * -Matt O <3
	 */
	
	
	public MainGrid(AuthorInterface author) {
		this.setY(50);
		this.setX(260);
		this.setWidth(400);
		this.setHeight(400);
		this.setFill(Color.AQUAMARINE);
	}

}
