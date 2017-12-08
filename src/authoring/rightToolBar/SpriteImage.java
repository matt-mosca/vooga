package authoring.rightToolBar;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import engine.authoring_engine.AuthoringController;
import javafx.scene.image.Image;
import display.splashScreen.ScreenDisplay;
import display.sprites.InteractiveObject;

public abstract class SpriteImage extends InteractiveObject {
	private String myImageName;
	private AuthoringController controller;
	private Map<String, String> myPossibleProperties;
	private Map<String, String> myBaseProperties;
	private String myName;
	private ResourceBundle myResourceBundle;
	private Map<String, String> defaultValues;
	private Map<String, String> allProperties;

	
	public SpriteImage(ScreenDisplay display) {
		super(display,null);
		defaultValues = new HashMap<>();
		myResourceBundle = ResourceBundle.getBundle("authoring/resources/SpriteProperties");
		myBaseProperties = new HashMap<String, String>();
		myPossibleProperties = new HashMap<String, String>();
		addDefaultValues();
		allProperties = new HashMap<String, String>();
	}
	
	private void addDefaultValues() {
		if (this instanceof TroopImage) {
			defaultValues.put("Numerical \"team\" association", "2");
			defaultValues.put("tabName", "Troops");
		}else if(this instanceof TowerImage) {
			defaultValues.put("Numerical \"team\" association", "1");
			defaultValues.put("tabName", "Towers");
		}else if(this instanceof ProjectileImage) {
			defaultValues.put("tabName", "Projectiles");
		}
	}
	
	public void addImage(String imageName) {
		myImageName = imageName;
		Image image;
		try {
			image = new Image(getClass().getClassLoader().getResourceAsStream(imageName));
		}catch (NullPointerException e) {
			image = new Image(imageName);
		}
		defaultValues.put("imageUrl", imageName);
		this.setImage(image);
		this.setId(imageName);
	}
	
	public void setName(String name) {
		myName = name;
	}
	
	public String getName() {
		return myName;
	}
	
	public void createInitialProperties(Map<String, Class> newMap) {
		if (myPossibleProperties.isEmpty()) {
			myPossibleProperties.put("Name", myName);
			for (String s : newMap.keySet()) {
				String def = getDefault(s);
				if(def != null) myPossibleProperties.put(s, def);
			}
		} 
	}
	
	public void update(String newProperty, String newValue) {
		myPossibleProperties.put(newProperty, newValue);
	}
	
	public void assignProjectile(String imageName) {
		myPossibleProperties.put("Projectile Type Name", imageName);
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
	
	public Map<String, String> getAllProperties() {
		allProperties.putAll(myPossibleProperties);
		allProperties.putAll(defaultValues);
		if (this instanceof TroopImage) {
			allProperties.put("tabName", "Troops");
		}else if(this instanceof TowerImage) {
			allProperties.put("tabName", "Towers");
		}else if(this instanceof ProjectileImage) {
			allProperties.put("tabName", "Projectiles");
		}
		return allProperties;
	}
	
	private String getDefault(String property) {
		if(myResourceBundle.containsKey(property)) return myResourceBundle.getString(property);
		return null;
	}
	
	public abstract SpriteImage clone();

}
