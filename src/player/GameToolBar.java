package player;

import javafx.scene.image.ImageView;
import splashScreen.ScreenDisplay;
import sprites.InteractiveObject;
import sprites.StaticObject;

/**
 * @deprecated
 * @author bwelton
 *
 */
public class GameToolBar extends GenericToolBar {
	public GameToolBar(ScreenDisplay display) {
		super(display, StaticObject.class);
	}
	
	protected void addImage(InteractiveObject object) {
		items.add(object);
	}

}
