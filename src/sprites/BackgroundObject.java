package sprites;

import interfaces.ClickableInterface;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;

public class BackgroundObject extends StaticObject {
	
	public BackgroundObject(Point2D size, ClickableInterface clickable, String imageString) {
		super(size, clickable, imageString);
		this.toBack();
		this.addEventHandler(MouseEvent.MOUSE_RELEASED, e->clickable.dropped(this, e));
	}
}
