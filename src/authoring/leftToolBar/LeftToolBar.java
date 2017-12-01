package authoring.leftToolBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import authoring.tabs.AddStaticTab;
import authoring.tabs.AddTab;
import authoring.tabs.SimpleTab;
import engine.authoring_engine.AuthoringController;
import factory.TabFactory;

import javafx.geometry.Point2D;

import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import splashScreen.ScreenDisplay;
import sprites.BackgroundObject;
import sprites.StaticObject;

/**
 * @author Matt
 */
public class LeftToolBar extends VBox {
	private static final int Y_POSITION = 50;
	private ScreenDisplay myDisplay;
	private AuthoringController myController;
	private TabPane tabPane;
	private TabFactory tabFactory;
	private SimpleTab staticTab;
	private SimpleTab backgroundTab;
	private AddTab addTab;
	private Map<String, String> propertiesMap;
	
	public LeftToolBar(ScreenDisplay display, AuthoringController controller) {
		createPropertiesMap();
		this.setLayoutY(Y_POSITION);
		myDisplay = display;
		myController = controller;
		tabPane = new TabPane();
		tabFactory = new TabFactory();
		this.getChildren().add(tabPane);
        createAndAddTabs();
	}
	
	private void createPropertiesMap() {
		propertiesMap = new HashMap<String, String>();
		propertiesMap.put("Collision effects", "Invulnerable to collision damage");
		propertiesMap.put("Collided-with effects", "Do nothing to colliding objects");
		propertiesMap.put("Firing behavior", "Do not fire projectiles");
		propertiesMap.put("Projectile Type Name", "Projectile");
		propertiesMap.put("Numerical \"team\" association", "0");
	}
	
	private void createAndAddTabs() {
		//TODO Change these addItem calls to run in a loop over properties sent from back end
		staticTab = new SimpleTab(myDisplay, new ArrayList<>());
		staticTab.addItem(createStaticObject(1, "tortoise.png"));
		staticTab.addItem(createStaticObject(2, "gray_circle.png"));		
		staticTab.addItem(createStaticObject(2, "green_soldier.gif"));
		staticTab.addItem(createStaticObject(2, "tree1.png"));
		
		backgroundTab = new SimpleTab(myDisplay, new ArrayList<>());
		
		backgroundTab.addItem(createBackgroundObject(2, "grass_small.png"));
		backgroundTab.addItem(createBackgroundObject(1, "grass2_medium.png"));		
		backgroundTab.addItem(createBackgroundObject(1, "brick_path.png"));
		backgroundTab.addItem(createBackgroundObject(1, "stone_path1.png"));
		backgroundTab.addItem(createBackgroundObject(1, "water_medium.png"));
		backgroundTab.addItem(createBackgroundObject(3, "grass_large.png"));
		
		tabPane.getTabs().add(tabFactory.buildTab("Static", "StaticObject", staticTab, tabPane));
		tabPane.getTabs().add(tabFactory.buildTab("Background", "BackgroundObject", backgroundTab, tabPane));
		
		addTab = new AddStaticTab(myDisplay, tabPane);
		tabPane.getTabs().add(tabFactory.buildTab("Add Image", null, addTab, tabPane));
		makeTabsUnclosable();
	}
	
	private StaticObject createBackgroundObject(int size, String imageString) {
		BackgroundObject tempStatic = new BackgroundObject(size, myDisplay, imageString);
		defineElement(tempStatic.getRealSize()*size, imageString);
		return tempStatic;
	}
	
	private StaticObject createStaticObject(int size, String imageString) {
		StaticObject tempStatic = new StaticObject(size, myDisplay, imageString);
		defineElement(tempStatic.getRealSize()*size, imageString);
		return tempStatic;
	}

	public void defineElement(int size, String imageString) {
		Map<String, String> defaultValues = new HashMap<>(propertiesMap);
		defaultValues.put("Move an object", "Object will stay at desired location");
		defaultValues.put("Collision effects", "Invulnerable to collision damage");
		defaultValues.put("Collided-with effects", "Do nothing to colliding objects");
		defaultValues.put("Firing Behavior", "Do not fire projectiles");
		defaultValues.put("Numerical \"team\" association", "0");
		defaultValues.put("imageWidth", "45.0");
		defaultValues.put("imageUrl", imageString);
		defaultValues.put("imageHeight", "45.0");
		myController.defineElement(imageString, defaultValues);
	}
	
	private void makeTabsUnclosable() {
		for(int i = 0; i < tabPane.getTabs().size(); i++) {
			tabPane.getTabs().get(i).setClosable(false);
		}
	}
}
