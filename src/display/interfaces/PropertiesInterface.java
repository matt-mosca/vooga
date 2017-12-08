package display.interfaces;

import authoring.rightToolBar.SpriteImage;
import javafx.scene.image.ImageView;

public interface PropertiesInterface {
	
	public void clicked(ImageView imageView);

	public void imageSelected(SpriteImage imageView);

	public void addToWave();

	public void addToLevel();

}
