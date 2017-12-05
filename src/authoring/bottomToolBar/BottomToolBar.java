package authoring.bottomToolBar;

import java.util.ArrayList;
import java.util.List;

import authoring.AuthorInterface;
import authoring.EditDisplay;
import authoring.GameArea;
import authoring.ScrollableArea;
import authoring.rightToolBar.SpriteImage;
import engine.authoring_engine.AuthoringController;
import factory.TabFactory;
import interfaces.CreationInterface;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

public class BottomToolBar extends VBox {
	private AuthoringController myController;
	private TabPane myTabPane;
	private List<LevelTab> myLevels;
	private List<GameArea> myGameAreas;
	private List<List<SpriteImage>> mySprites;
	private ScrollableArea myScrollableArea;
	private TabFactory tabMaker;
	private final int X_LAYOUT = 260;
	private final int Y_LAYOUT = 470;
	private Button newLevel;
	private Button editLevel;
	private int currentDisplay;
	private EditDisplay myCreated;
	
	public BottomToolBar (EditDisplay created, AuthoringController controller, ScrollableArea area) {
		myScrollableArea = area;
		currentDisplay = 1;
		myCreated = created;
		myController = controller;
		myGameAreas = new ArrayList<>();
		this.setLayoutX(X_LAYOUT);
		this.setLayoutY(Y_LAYOUT);
		this.setWidth(400);
		myLevels = new ArrayList<>();
		mySprites = new ArrayList<List<SpriteImage>>();
		newLevel =  new Button("New Level");
		newLevel.setOnAction(e->addLevel());
		myTabPane = new TabPane();
		tabMaker = new TabFactory();
		myTabPane.setMaxSize(400, 200);
		myTabPane.setPrefSize(400, 200);
		addLevel();
		created.setGameArea(myGameAreas.get(0));
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
		Tab newTab = tabMaker.buildTabWithoutContent("Level " + Integer.toString(myLevels.size()+1), null, myTabPane);
		LevelTab newLv = new LevelTab(myLevels.size()+1, myController);	
		myGameAreas.add(new GameArea(myController));
		myController.createNewLevel(myLevels.size()+1);
		myController.createNewLevel(currentDisplay);
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
		if (mySprites.get(i-1).isEmpty()) {
			mySprites.add(i-1, new ArrayList<>());
		}
		myScrollableArea.changeLevel(myGameAreas.get(i-1));
		myCreated.setDroppable(myGameAreas.get(i-1));
		myController.createNewLevel(i);
		myCreated.setGameArea(myGameAreas.get(i-1));
	}

	private void deleteLevel(int lvNumber) {
		myController.deleteLevel(lvNumber);
		myLevels.remove(lvNumber-1);
		myGameAreas.remove(lvNumber-1);
		for (int i = lvNumber-1; i<myLevels.size(); i++) {
			myLevels.get(i).decrementLevel();
			myTabPane.getTabs().get(i).setText("Level " + Integer.toString(i+1));
		}
		
	}
	
	public void addToCurrLevel(SpriteImage newSprite) {
		mySprites.get(currentDisplay-1).add(newSprite);
	}
}
