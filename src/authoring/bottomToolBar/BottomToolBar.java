package authoring.bottomToolBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import authoring.AuthorInterface;
import authoring.EditDisplay;
import authoring.GameArea;
import authoring.ScrollableArea;
import engine.authoring_engine.AuthoringController;
import factory.TabFactory;
import interfaces.CreationInterface;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import sprites.InteractiveObject;
import sprites.StaticObject;

public class BottomToolBar extends VBox {
	private final int CELL_SIZE = 40;
	
	private AuthoringController myController;
	private TabPane myTabPane;
	private List<LevelTab> myLevels;
	private List<GameArea> myGameAreas;
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
		newLevel =  new Button("New Level");
		newLevel.setOnAction(e->addLevel());
		myTabPane = new TabPane();
		tabMaker = new TabFactory();
		myTabPane.setMaxSize(400, 200);
		myTabPane.setPrefSize(400, 200);
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
		loadLevels();
		created.setGameArea(myGameAreas.get(0));
	}
	
	private void loadLevels() {
		if(myController.getNumLevelsForGame(myController.getGameName(), true) == 0) {
			addLevel();
			return;
		}
		for(int i = 1; i<=myController.getNumLevelsForGame(myController.getGameName(), true); i++) {
			addLevel();
			initializeSprites(i);
		}
	}

	private void addLevel() {
		Tab newTab = tabMaker.buildTabWithoutContent("Level " + Integer.toString(myLevels.size()+1), null, myTabPane);
		LevelTab newLv = new LevelTab(myLevels.size()+1, myController);	
		myGameAreas.add(new GameArea(myController));
		myController.createNewLevel(myLevels.size()+1);
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
	
	//TODO need load in static object rather than just imageview
	private void initializeSprites(int level) {
		try {
			myController.loadOriginalGameState(myController.getGameName(), level);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(Integer id : myController.getLevelSprites(level)) {
			System.out.println("HIT");
			ImageView imageView = myController.getRepresentationFromSpriteId(id);
//			StaticObject savedSprite = new StaticObject((int) imageView.getBoundsInLocal().getHeight()/CELL_SIZE, myCreated, imageView.getImage().toString());
			myGameAreas.get(level-1).getChildren().add(imageView);
		}
	}

	private void changeDisplay(int i) {
		currentDisplay = i;
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
}
