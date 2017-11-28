package authoring.leftToolBar;

import java.util.ArrayList;

import authoring.tabs.AddStaticTab;
import authoring.tabs.AddTab;
import authoring.tabs.SimpleTab;
import engine.authoring_engine.AuthoringController;
import factory.TabFactory;
import interfaces.ClickableInterface;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

/**
 * @author Matt
 */
public class LeftToolBar extends VBox {
	private static final int Y_POSITION = 50;
	private ClickableInterface myClickable;
	private AuthoringController myController;
	private TabPane tabPane;
	private TabFactory tabFactory;
	private SimpleTab staticTab;
	private SimpleTab backgroundTab;
	private AddTab addTab;
	
	public LeftToolBar(ClickableInterface clickable, AuthoringController controller) {
		this.setLayoutY(Y_POSITION);
		myClickable = clickable;
		myController = controller;
		tabPane = new TabPane();
		tabFactory = new TabFactory();
		this.getChildren().add(tabPane);
        createAndAddTabs();
	}
	
	private void createAndAddTabs() {
		//TODO Change these addItem calls to run in a loop over properties sent from back end
		staticTab = new SimpleTab(myClickable, new ArrayList<>());
		staticTab.addStaticItem(1, "tortoise.png");
		staticTab.addStaticItem(1, "gray_circle.png");
		staticTab.addStaticItem(1, "green_soldier.gif");
		
		backgroundTab = new SimpleTab(myClickable, new ArrayList<>());
		backgroundTab.addBackgroundItem(3, "grass_small.png");
		backgroundTab.addBackgroundItem(3, "grass2_small.png");
		backgroundTab.addBackgroundItem(2, "brick_path.png");
		backgroundTab.addBackgroundItem(2, "stone_path1.png");
		backgroundTab.addBackgroundItem(3, "water_medium.png");
		
		tabPane.getTabs().add(tabFactory.buildTab("Static", "StaticObject", staticTab, tabPane));
		tabPane.getTabs().add(tabFactory.buildTab("Background", "BackgroundObject", backgroundTab, tabPane));
		
		addTab = new AddStaticTab(myClickable, tabPane);
		tabPane.getTabs().add(tabFactory.buildTab("Add Image", null, addTab, tabPane));
		makeTabsUnclosable();
	}
	
	private void makeTabsUnclosable() {
		for(int i = 0; i < tabPane.getTabs().size(); i++) {
			tabPane.getTabs().get(i).setClosable(false);
		}
	}
}
