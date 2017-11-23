package player;

import interfaces.ClickableInterface;
import javafx.scene.shape.Rectangle;

public interface PlayerInterface extends ClickableInterface {

	public void clicked(Rectangle rec);
	
	public void decreaseHealth();
}
