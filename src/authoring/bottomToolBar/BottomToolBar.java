package authoring.bottomToolBar;

import java.util.ArrayList;
import java.util.List;

import factory.TabFactory;
import interfaces.CreationInterface;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

public class BottomToolBar extends VBox {
	private TabPane myTabPane;
	private List<LevelTab> myLevels;
	private TabFactory tabMaker;
	private final int X_LAYOUT = 260;
	private final int Y_LAYOUT = 500;
	private Button newLevel;
	
	public BottomToolBar (CreationInterface created) {
		this.setLayoutX(X_LAYOUT);
		this.setLayoutY(Y_LAYOUT);
		tabMaker = new TabFactory();
		myLevels = new ArrayList<>();
		newLevel =  new Button();
		newLevel.setOnAction(e->addLevel());
		myTabPane = new TabPane();
		this.getChildren().add(myTabPane);
		addLevel();
	}

	private void addLevel() {
		Tab newTab = tabMaker.buildTabWithoutContent("Level" + myLevels.size()+1, myTabPane);
		LevelTab newLv = new LevelTab(myLevels.size()+1);	
		if (myLevels.size()==0) {
			newTab.setClosable(false);
		}else {
		newTab.setOnClosed(e->deleteLevel(newLv.getLvNumber()));
		}
		newLv.attach(newTab);
		myLevels.add(newLv);
	}

	private void deleteLevel(int lvNumber) {
		/*TODO
		 * this deletes the level in the backend and then proceeds to decrement the levelNumbers
		 * of all of the consecutive levels
		 */
		myLevels.remove(lvNumber-1);
		for (int i = lvNumber-1; i<myLevels.size(); i++) {
			myLevels.get(i).decrementLevel();
		}	
	}
}
