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
	
	public void resize(double displaySize) {
		double spriteWidth = this.getBoundsInLocal().getWidth();
		double spriteHeight = this.getBoundsInLocal().getHeight();
		double maxDimension = Math.max(spriteWidth, spriteHeight);
		double scaleValue = maxDimension / displaySize;
		this.setFitWidth(spriteWidth / scaleValue);
		this.setFitHeight(spriteHeight / scaleValue);
	}

}
