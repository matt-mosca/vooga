package player;

import authoring.rightToolBar.SpriteImage;
import interfaces.ClickableInterface;
import javafx.scene.shape.Rectangle;

public interface PlayerInterface{

	public void clicked(SpriteImage sprite);
	
	public void decreaseHealth();
}
