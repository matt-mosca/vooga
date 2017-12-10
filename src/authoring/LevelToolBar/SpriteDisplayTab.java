package authoring.LevelToolBar;

import java.util.List;

import authoring.GameArea;
import display.factory.TabFactory;
import engine.authoring_engine.AuthoringController;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;

public class SpriteDisplayTab extends HBox {
	
	private TabPane myTabPane;
	private TabFactory tabMaker;
	private int myWaves;
	private List<LevelTab> myWavesList;
	private SpriteDisplayer mySpriteDisplay;
	private AuthoringController controller;
	private int currentDisplay;
	
	public SpriteDisplayTab(AuthoringController myController) {
		currentDisplay = 1;
		controller = myController;
		myTabPane = new TabPane();
		tabMaker = new TabFactory();
		myWaves = 0;
		myTabPane.setMaxSize(400, 100);
		myTabPane.setPrefSize(400, 100);
		this.getChildren().add(myTabPane);
	}
		
	public void addLevel() {
		Tab newTab = tabMaker.buildTabWithoutContent("Level " + Integer.toString(myWavesList.size() + 1), null, myTabPane);
		newTab.setContent(mySpriteDisplay);
		LevelTab newLv = new LevelTab(myWavesList.size() + 1, controller);
		controller.setLevel(myWavesList.size() + 1);
//		if (myWavesList.size() == 0) {
//			newTab.setClosable(false);
//		} else {
//			newTab.setOnClosed(e -> deleteLevel(newLv.getLvNumber()));
//		}
		newTab.setOnSelectionChanged(e->changeLevel(newLv.getLvNumber()));
		newLv.attach(newTab);
		myWavesList.add(newLv);
		myTabPane.getTabs().add(newTab);
	}
	
	public void changeLevel(int i) {
		currentDisplay = i;
	}
	
	

}
