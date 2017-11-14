package player;

import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class HealthBackground extends Rectangle {
	
	private Rectangle healthBackground;
	private Rectangle healthBar;
	
	
	public HealthBackground() {
		this.setHeight(40);
		this.setWidth(200);
		this.setFill(Color.RED);
	}

}
