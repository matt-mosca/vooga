package authoring.leftToolBar;

import java.util.ArrayList;

import factory.TabFactory;
import interfaces.ClickableInterface;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

/**
 * 
 * @author Matt
 */
public class LeftToolBar extends VBox {
	private static final int WIDTH = 300;
	private ClickableInterface myClickable;
	private TabPane tabPane;
	private TabFactory tabFactory;
	private SimpleTab staticTab;
	private SimpleTab backgroundTab;
	
	public LeftToolBar(ClickableInterface clickable) {
		this.setLayoutY(50);
		myClickable = clickable;
		tabPane = new TabPane();
		tabFactory = new TabFactory();
		this.getChildren().add(tabPane);
        createAndAddTabs();
	}
	
	private void createAndAddTabs() {
		//TODO Change these addItem calls to run in a loop over properties sent from back end
		staticTab = new SimpleTab(myClickable, new ArrayList<>());
		staticTab.addItem(1, "tortoise.png");
		staticTab.addItem(1, "gray_circle.png");
		staticTab.addItem(1, "green_soldier.gif");
		
		backgroundTab = new SimpleTab(myClickable, new ArrayList<>());
		backgroundTab.addItem(3, "grass_small.png");
		backgroundTab.addItem(3, "grass2_small.png");
		backgroundTab.addItem(2, "brick_path.png");
		backgroundTab.addItem(2, "stone_path1.png");
		backgroundTab.addItem(3, "water_medium.png");
		
		tabPane.getTabs().add(tabFactory.buildTab("Static", staticTab, tabPane));
		tabPane.getTabs().add(tabFactory.buildTab("Background", backgroundTab, tabPane));
		makeTabsUnclosable();
	}
	
	private void makeTabsUnclosable() {
		for(int i = 0; i < tabPane.getTabs().size(); i++) {
			tabPane.getTabs().get(i).setClosable(false);
		}
	}
}
