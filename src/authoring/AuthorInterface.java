package authoring;

import interfaces.ClickableInterface;
import interfaces.CreationInterface;
import javafx.scene.image.ImageView;

public interface AuthorInterface  extends ClickableInterface, CreationInterface {
	
	public void newTowerSelected(ImageView myImageView);
	
}
