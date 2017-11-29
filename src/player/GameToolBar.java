package player;

import shared.GenericToolBar;
import splashScreen.ScreenDisplay;
import sprites.StaticObject;

public class GameToolBar extends GenericToolBar {
	public GameToolBar(ScreenDisplay display) {
		super(display, StaticObject.class);
	}

}
