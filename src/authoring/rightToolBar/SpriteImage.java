package authoring.rightToolBar;

import java.util.Map;
import java.util.TreeMap;

import engine.authoring_engine.AuthoringController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class SpriteImage extends ImageView {
	
	private String myImageName;
	private AuthoringController controller;
	private Map<String, String> myProperties;
	
	
	public SpriteImage() {
	}
	
	public void addImage(String imageName) {
		myImageName = imageName;
		Image image;
		try {
			image = new Image(getClass().getClassLoader().getResourceAsStream(imageName));
		}catch (NullPointerException e) {
			image = new Image(imageName);
		}
		this.setImage(image);
		setDefaultProperties();
	}
	
	private void setDefaultProperties() {
		myProperties = new TreeMap<String, String>();
		myProperties.put("Cost", "10");
		myProperties.put("Strength", "20");
		myProperties.put("Health", "100");
	}
	
	public void update(String newProperty, String newValue) {
		myProperties.put(newProperty, newValue);
	}
	
	public Map<String, String> getMyProperties() {
		return myProperties;
	}
	
	public void resize(double displaySize) {
		double spriteWidth = this.getBoundsInLocal().getWidth();
		double spriteHeight = this.getBoundsInLocal().getHeight();
		double maxDimension = Math.max(spriteWidth, spriteHeight);
		double scaleValue = maxDimension / displaySize;
		this.setFitWidth(spriteWidth / scaleValue);
		this.setFitHeight(spriteHeight / scaleValue);
	}
	
	public void createElement() {
	}
	
	public abstract SpriteImage clone();

}
