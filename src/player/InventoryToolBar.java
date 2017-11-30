package player;

import java.util.ArrayList;

import authoring.tabs.SimpleTab;
import factory.TabFactory;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import splashScreen.ScreenDisplay;

public class InventoryToolBar extends VBox{
	private static final int Y_POSITION = 50;
	private ScreenDisplay myDisplay;
	private TabPane tabPane;
	private TabFactory tabFactory;
	private SimpleTab towerTab;
	
	public InventoryToolBar(ScreenDisplay display) {
		this.setLayoutY(Y_POSITION);
		myDisplay = display;
		tabPane = new TabPane();
		tabFactory = new TabFactory();
		this.getChildren().add(tabPane);
        createAndAddTabs();
	}

	private void createAndAddTabs() {
		towerTab = new SimpleTab(myDisplay, new ArrayList<>());
		tabPane.getTabs().add(tabFactory.buildTab("Towers", "TowerImage", towerTab, tabPane));
		makeTabsUnclosable();
	}
	
	protected void addToToolbar(ImageView imageView) {
		towerTab.addItem(imageView);
	}
	
	private void makeTabsUnclosable() {
		for(int i = 0; i < tabPane.getTabs().size(); i++) {
			tabPane.getTabs().get(i).setClosable(false);
		}
	}
}
