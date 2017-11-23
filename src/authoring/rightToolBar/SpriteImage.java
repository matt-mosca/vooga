package authoring.rightToolBar;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SpriteImage extends ImageView {
	
	
	public SpriteImage() {
	}
	
	public void addImage(String imageName) {
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(imageName));
		this.setImage(image);
		
		
	}

}
