package player;

import authoring.AuthorInterface;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ToolBar extends HBox {
	
	private Rectangle healthBackground;
	private HealthBar myHealthBar;
	
	public ToolBar(PlayerInterface player) {
		this.setSpacing(100);
		myHealthBar = new HealthBar();
		this.getChildren().add(myHealthBar);
		this.getChildren().add(new DecreaseHealthButton(player));
	}
	
	public HealthBar getHealthBar() {
		return myHealthBar;
	}
}
