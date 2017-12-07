package authoring.bottomToolBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import authoring.EditDisplay;
import authoring.GameArea;
import authoring.ScrollableArea;
import authoring.rightToolBar.SpriteImage;
import engine.authoring_engine.AuthoringController;
import display.factory.TabFactory;
import display.interfaces.CreationInterface;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import display.sprites.InteractiveObject;

public class BottomToolBar extends VBox {
	private final int CELL_SIZE = 40;
	
	private AuthoringController myController;
	private TabPane myTabPane;
	private List<LevelTab> myLevels;
	private List<GameArea> myGameAreas;
	private List<List<ImageView>> mySprites;
	private ScrollableArea myScrollableArea;
	private TabFactory tabMaker;
	private final int X_LAYOUT = 260;
	private final int Y_LAYOUT = 470;
	private Button newLevel;
	private Button editLevel;
	private int currentDisplay;
	private EditDisplay myCreated;
	private SpriteDisplayer mySpriteDisplay;
	
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
		mySprites = new ArrayList<>();
		mySprites.add(new ArrayList<>());
		newLevel =  new Button("New Level");
		newLevel.setOnAction(e->addLevel());
		myTabPane = new TabPane();
		tabMaker = new TabFactory();
		mySpriteDisplay = new SpriteDisplayer();
		this.getChildren().add(mySpriteDisplay);
		myTabPane.setMaxSize(400, 100);
		myTabPane.setPrefSize(400, 100);
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
		if(myController.getGameName().equals("untitled") || myController.getNumLevelsForGame(myController.getGameName(), true) == 0) {
			addLevel();
			return;
		}
		for(int i = 1; i<=myController.getNumLevelsForGame(myController.getGameName(), true); i++) {
			addLevel();
			initializeSprites(i);
		}
	}

	private void addLevel() {
		mySprites.add(new ArrayList<>());
		Tab newTab = tabMaker.buildTabWithoutContent("Level " + Integer.toString(myLevels.size()+1), null, myTabPane);
		newTab.setContent(mySpriteDisplay);
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
			ImageView imageView = myController.getRepresentationFromSpriteId(id);
			InteractiveObject savedObject = new InteractiveObject(myCreated, imageView.getImage().toString());
			savedObject.setImageView(imageView);
			myGameAreas.get(level-1).addBackObject(savedObject);
		}
	}

	private void changeDisplay(int i) {
		currentDisplay = i;
		myScrollableArea.changeLevel(myGameAreas.get(i-1));
		myCreated.setDroppable(myGameAreas.get(i-1));
		myController.createNewLevel(i);
		myCreated.setGameArea(myGameAreas.get(i-1));
		updateSpriteDisplay(i);
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
	
	public void addToLevel(ImageView newSprite, int level) {
		mySprites.get(level-1).add(newSprite);
		updateSpriteDisplay(currentDisplay);
	}

	private void updateSpriteDisplay(int level) {
		mySpriteDisplay.addToScroll(mySprites.get(level-1));
	}
	
	public int getMaxLevel() {
		return myLevels.size();
	}
}
