package player;

import authoring.rightToolBar.SpriteImage;
import interfaces.ClickableInterface;
import javafx.scene.shape.Rectangle;

public interface PlayerInterface extends ClickableInterface {

	public void clicked(SpriteImage sprite);
	
	public void decreaseHealth();
}
