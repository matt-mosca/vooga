package authoring.rightToolBar;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import engine.authoring_engine.AuthoringController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import splashScreen.ScreenDisplay;
import sprites.InteractiveObject;

public abstract class SpriteImage extends InteractiveObject {
	private String myImageName;
	private AuthoringController controller;
	private Map<String, String> myProperties;
	private Map<String, String> myPossibleProperties;
	private Map<String, String> myBaseProperties;
	private String myName;
	
	public SpriteImage(ScreenDisplay display) {
		super(display,null);
		myBaseProperties = new HashMap<String, String>();
		myPossibleProperties = new HashMap<String, String>();
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
	
	public void setName(String name) {
		myName = name;
		myProperties.put("Name", name);
	}
	
	public String getName() {
		return myName;
	}
	
	private void setDefaultProperties() {
		myProperties = new HashMap<String, String>();
		myProperties.put("Cost", "10");
		myProperties.put("Strength", "20");
		myProperties.put("Name", myName);
		myProperties.put("Health", "100");
	}
	
	public void createInitialProperties(Map<String, Class> newMap) {
		if (myPossibleProperties.isEmpty()) {
			for (String s : newMap.keySet()) {
				myPossibleProperties.put(s, newMap.get(s).toString());
			}
		} 
	}
	
	public void update(String newProperty, String newValue) {
		myPossibleProperties.put(newProperty, newValue);
	}
	
	public Map<String, String> getMyProperties() {
		return myPossibleProperties;
	}
	
	public void setMyProperties(Map<String, String> newMap) {
		myPossibleProperties = newMap;
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
	
	public void addBasePropertyMap(Map<String, String> newMap) {
		myBaseProperties = newMap;
	}
	
	public Map<String, String> getPropertiesMap() {
		return myBaseProperties;
	}
	
	@Override
	public int getSize() {
		//TODO modify to let spriteimages occupy cells as well
		return 0;
	}
	
	public abstract SpriteImage clone();

}
