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
		propertiesMap.put("Collided-with effects", "Do nothing to collided objects");
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
		
		addBackgroundObjectToTab(2, "grass_small.png");
		addBackgroundObjectToTab(1, "grass2_medium.png");
		addBackgroundObjectToTab(1, "brick_path.png");
		addBackgroundObjectToTab(1, "stone_path1.png");
		addBackgroundObjectToTab(1, "water_medium.png");
		
		tabPane.getTabs().add(tabFactory.buildTab("Static", "StaticObject", staticTab, tabPane));
		tabPane.getTabs().add(tabFactory.buildTab("Background", "BackgroundObject", backgroundTab, tabPane));
		
		addTab = new AddStaticTab(myDisplay, tabPane);
		tabPane.getTabs().add(tabFactory.buildTab("Add Image", null, addTab, tabPane));
		makeTabsUnclosable();
	}
	
	private void addBackgroundObjectToTab(int size, String imageString) {
		defineElement(size, imageString);
		backgroundTab.addBackgroundItem(size, imageString);

	}
	
	private StaticObject createStaticObject(int size, String imageString) {
		StaticObject tempStatic = new StaticObject(size, myDisplay, imageString);
		defineElement(size, imageString);
		return tempStatic;
	}

	public void defineElement(int size, String imageString) {
		propertiesMap.put("imageWidth", String.valueOf(size));
		propertiesMap.put("imageHeight", String.valueOf(size));
		propertiesMap.put("imageUrl", imageString);
		myController.defineElement(imageString, propertiesMap);
	}
	
	private void makeTabsUnclosable() {
		for(int i = 0; i < tabPane.getTabs().size(); i++) {
			tabPane.getTabs().get(i).setClosable(false);
		}
	}
}
