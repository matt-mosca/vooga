package authoring;

import authoring.rightToolBar.SpriteImage;
import interfaces.ClickableInterface;
import interfaces.CreationInterface;
import interfaces.CustomizeInterface;
import interfaces.TestingInterface;
import javafx.scene.image.ImageView;

public interface AuthorInterface extends CreationInterface, CustomizeInterface, TestingInterface {
	
	public void newTowerSelected(ImageView myImageView);

	public void imageSelected(SpriteImage imageView);

	public void addToMap(String baseProperty, String value);
	
}
