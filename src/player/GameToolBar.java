package player;

import javafx.scene.image.ImageView;
import shared.GenericToolBar;
import splashScreen.ScreenDisplay;
import sprites.InteractiveObject;
import sprites.StaticObject;

public class GameToolBar extends GenericToolBar {
	public GameToolBar(ScreenDisplay display) {
		super(display, StaticObject.class);
		this.getStylesheets().add("player/resources/playerPanes.css");
		this.getStyleClass().add("toolbar");
	}
	
	protected void addImage(InteractiveObject object) {
		items.add(object);
	}

}
