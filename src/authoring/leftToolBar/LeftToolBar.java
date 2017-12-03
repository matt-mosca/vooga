package authoring.leftToolBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import authoring.tabs.AddStaticTab;
import authoring.tabs.AddTab;
import authoring.tabs.SimpleTab;
import engine.authoring_engine.AuthoringController;
import factory.TabFactory;

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
	private ResourceBundle defaultProperties;
	
	public LeftToolBar(ScreenDisplay display, AuthoringController controller) {
		this.setLayoutY(Y_POSITION);
		myDisplay = display;
		myController = controller;
		tabPane = new TabPane();
		tabFactory = new TabFactory();
		defaultProperties = ResourceBundle.getBundle("authoring/resources/DefaultProperties");
		this.getChildren().add(tabPane);
        createAndAddTabs();
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
		defineElement(tempStatic.getRealSize(), imageString);
		return tempStatic;
	}
	
	private StaticObject createStaticObject(int size, String imageString) {
		StaticObject tempStatic = new StaticObject(size, myDisplay, imageString);
		defineElement(tempStatic.getRealSize(), imageString);
		return tempStatic;
	}

	public void defineElement(int size, String imageString) {
		Map<String, String> defaultValues = getDefaultProperties();
		defaultValues.put("imageWidth", Integer.toString(size));
		defaultValues.put("imageUrl", imageString);
		defaultValues.put("imageHeight", Integer.toString(size));
		myController.defineElement(imageString, defaultValues);
	}
	
	private Map<String, String> getDefaultProperties() {
		Map<String, String> values = new HashMap<>();
		for(String key:defaultProperties.keySet()) {
			System.out.println(key);
			System.out.println(defaultProperties.getString(key));
			values.put(key, defaultProperties.getString(key));
		}
		return values;
	}
	
	private void makeTabsUnclosable() {
		for(int i = 0; i < tabPane.getTabs().size(); i++) {
			tabPane.getTabs().get(i).setClosable(false);
		}
	}
}
