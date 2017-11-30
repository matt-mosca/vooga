package authoring.rightToolBar;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import engine.authoring_engine.AuthoringController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import splashScreen.ScreenDisplay;
import sprites.InteractiveObject;

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
		defaultValues.put("imageWidth", "20");
		defaultValues.put("imageHeight", "20");
		defaultValues.put("Numerical \"team\" association", "0");
		if (this instanceof TowerImage) defaultValues.put("Numerical \"team\" association", "1");
		defaultValues.put("Health points", "50");
		defaultValues.put("Damage dealt to colliding objects", "20");
		defaultValues.put("Speed of movement", "5");
		defaultValues.put("initialAngle", "0");
		defaultValues.put("radius", "10");
		defaultValues.put("centerY", "0");
		defaultValues.put("centerX", "0");
		defaultValues.put("Target y-coordinate", "0");
		defaultValues.put("Target x-coordinate", "0");
		defaultValues.put("Projectile Type Name", "No projectile selected");
		defaultValues.put("Attack period", "10");
		defaultValues.put("PathList", "CurrentPath");
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
				myPossibleProperties.put(s, getDefault(s));
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
	
	public void addBasePropertyMap(Map<String, String> newMap) {
		myBaseProperties = newMap;
	}
	
	public Map<String, String> getPropertiesMap() {
		return myBaseProperties;
	}
	
	public Map<String, String> getAllProperties() {
		allProperties.putAll(myPossibleProperties);		
		allProperties.putAll(myBaseProperties);
		return allProperties;
	}
	
	@Override
	public int getSize() {
		//TODO modify to let spriteimages occupy cells as well
		return 0;
	}
	
	private String getDefault(String property) {
		return defaultValues.get(property);
		
	}
	
	public abstract SpriteImage clone();

}
