package display.toolbars;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import display.tabs.AddStaticTab;
import display.tabs.AddTab;
import display.tabs.SimpleTab;
import engine.authoring_engine.AuthoringController;
import display.factory.TabFactory;

import javafx.scene.control.TabPane;
import display.splashScreen.ScreenDisplay;
import display.sprites.BackgroundObject;
import display.sprites.StaticObject;

/**
 * @author Matt
 */
public class StaticObjectToolBar extends ToolBar {
	private static final int Y_POSITION = 50;
	private ScreenDisplay myDisplay;
	private AuthoringController myController;
	private TabPane tabPane;
	private TabFactory tabFactory;
	private SimpleTab staticTab;
	private SimpleTab backgroundTab;
	private AddTab addTab;
	private ResourceBundle defaultProperties;
	
	public StaticObjectToolBar(ScreenDisplay display, AuthoringController controller) {
		this.setLayoutY(Y_POSITION);
		myDisplay = display;
		myController = controller;
		tabPane = new TabPane();
		tabFactory = new TabFactory();
		defaultProperties = ResourceBundle.getBundle("authoring/resources/DefaultProperties");
		this.getChildren().add(tabPane);
        createAndAddTabs();
	}
	
	@Override
	protected void createAndAddTabs() {
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
		makeTabsUnclosable(tabPane);
	}
	
	private StaticObject createBackgroundObject(int size, String imageString) {
		BackgroundObject tempStatic = new BackgroundObject(size, myDisplay, imageString);
		defineElement(tempStatic.getSize(), imageString);
		return tempStatic;
	}
	
	private StaticObject createStaticObject(int size, String imageString) {
		StaticObject tempStatic = new StaticObject(size, myDisplay, imageString);
		defineElement(tempStatic.getSize(), imageString);
		return tempStatic;
	}

	public void defineElement(double size, String imageString) {
		if(myController.getAllDefinedTemplateProperties().containsKey(imageString)) return;
		Map<String, Object> defaultValues = getDefaultProperties();
		defaultValues.put("imageWidth", size);
		defaultValues.put("imageUrl", imageString);
		defaultValues.put("imageHeight", size);
		myController.defineElement(imageString, defaultValues);
	}
	
	private Map<String, Object> getDefaultProperties() {
		Map<String, Object> values = new HashMap<>();
		for(String key : defaultProperties.keySet()) {
			try {
				values.put(key, Integer.parseInt(defaultProperties.getString(key)));
			} catch(NumberFormatException e) {
				try {
					values.put(key, Double.parseDouble(defaultProperties.getString(key)));
				} catch (NumberFormatException ee) {
					values.put(key, defaultProperties.getString(key));
				}
			}
		}
		return values;
	}
}
