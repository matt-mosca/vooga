package authoring;

import interfaces.ClickableInterface;
import interfaces.CreationInterface;
import interfaces.CustomizeInterface;
import javafx.scene.image.ImageView;

public interface AuthorInterface extends CreationInterface, CustomizeInterface {
	
	public void newTowerSelected(ImageView myImageView);
	
}
