package player;

import authoring.leftToolBar.LeftToolBar;
import interfaces.ClickableInterface;
import shared.GenericToolBar;
import sprites.BackgroundObject;
import sprites.StaticObject;

public class GameToolBar extends GenericToolBar {
	public GameToolBar(ClickableInterface clickable) {
		super(clickable, StaticObject.class);
	}

}
