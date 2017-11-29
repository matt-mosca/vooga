package authoring.bottomToolBar;

import java.util.ArrayList;
import java.util.List;

import authoring.AuthorInterface;
import authoring.GameArea;
import authoring.ScrollableArea;
import factory.TabFactory;
import interfaces.CreationInterface;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

public class BottomToolBar extends VBox {
	private TabPane myTabPane;
	private List<LevelTab> myLevels;
	private List<GameArea> myGameAreas;
	private ScrollableArea myScrollableArea;
	private TabFactory tabMaker;
	private final int X_LAYOUT = 260;
	private final int Y_LAYOUT = 560;
	private Button newLevel;
	private Button editLevel;
	private int currentDisplay;
	private AuthorInterface myCreated;
	
	public BottomToolBar (AuthorInterface created, ScrollableArea area) {
		myScrollableArea = area;
		myCreated = created;
		myGameAreas = new ArrayList<>();
		this.setLayoutX(X_LAYOUT);
		this.setLayoutY(Y_LAYOUT);
		this.setWidth(400);
		tabMaker = new TabFactory();
		myLevels = new ArrayList<>();
		newLevel =  new Button("New Level");
		newLevel.setOnAction(e->addLevel());
		myTabPane = new TabPane();
		
		addLevel();
		editLevel = new Button("Edit Level");
		//Need to put the button somewhere first.
		editLevel.setOnAction(e->{
			myLevels.get(currentDisplay-1).openLevelDisplay(); 
//			edited = true;
//			this.update();
			});
		this.getChildren().add(myTabPane);
		this.getChildren().add(newLevel);
		this.getChildren().add(editLevel);
	}

	private void addLevel() {
		Tab newTab = tabMaker.buildTabWithoutContent("Level " + Integer.toString(myLevels.size()+1),null,myTabPane);
		LevelTab newLv = new LevelTab(myLevels.size()+1);	
		myGameAreas.add(new GameArea(myCreated));
		if (myLevels.size()==0) {
			newTab.setClosable(false);
		}else {
		newTab.setOnClosed(e->deleteLevel(newLv.getLvNumber()));
		}
		newTab.setOnSelectionChanged(e->changeDisplay(newLv.getLvNumber()));
		newLv.attach(newTab);
		myLevels.add(newLv);
		myTabPane.getTabs().add(newTab);
		
	}

	private void changeDisplay(int i) {
		currentDisplay = i;
		myScrollableArea.setContent(myGameAreas.get(i-1));
	}

	private void deleteLevel(int lvNumber) {
		/*TODO
		 * this deletes the level in the backend and then proceeds to decrement the levelNumbers
		 * of all of the consecutive levels
		 */
		myLevels.remove(lvNumber-1);
		myGameAreas.remove(lvNumber-1);
		for (int i = lvNumber-1; i<myLevels.size(); i++) {
			myLevels.get(i).decrementLevel();
			myTabPane.getTabs().get(i).setText("Level " + Integer.toString(i+1));
		}
		
	}
}
