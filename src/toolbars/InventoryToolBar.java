package toolbars;

import java.util.ArrayList;

import authoring.tabs.SimpleTab;
import factory.TabFactory;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import splashScreen.ScreenDisplay;

public class InventoryToolBar extends ToolBar{
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
		this.getStylesheets().add("player/resources/playerPanes.css");
		this.getStyleClass().add("toolbar");
	}

	@Override
	protected void createAndAddTabs() {
		towerTab = new SimpleTab(myDisplay, new ArrayList<>());
		tabPane.getTabs().add(tabFactory.buildTab("Towers", "TowerImage", towerTab, tabPane));
		makeTabsUnclosable(tabPane);
	}
	
	public void addToToolbar(ImageView imageView) {
		towerTab.addItem(imageView);
	}
}
