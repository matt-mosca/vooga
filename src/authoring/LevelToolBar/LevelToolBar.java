package authoring.LevelToolBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import authoring.EditDisplay;
import authoring.GameArea;
import authoring.ScrollableArea;
import authoring.PropertiesToolBar.SpriteImage;
import engine.authoring_engine.AuthoringController;
import display.factory.TabFactory;
import display.interfaces.CreationInterface;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import util.protocol.ClientMessageUtils;
import display.sprites.InteractiveObject;

public class LevelToolBar extends VBox {
	private final int CELL_SIZE = 40;

	private AuthoringController myController;
	private TabPane myTabPane;
	private List<LevelTab> myLevels;
	private List<GameArea> myGameAreas;
	private List<List<ImageView>> mySprites;
	private ScrollableArea myScrollableArea;
	private WaveDisplay myWaveDisplay;
	private TabFactory tabMaker;
	private final int X_LAYOUT = 260;
	private final int Y_LAYOUT = 470;
	private Button newLevel;
	private Button editLevel;
	private int currentDisplay;
	private EditDisplay myCreated;
	private SpriteDisplayer mySpriteDisplay;
	private LevelsEditDisplay myLevelDisplayer;
	private Map<Integer, Integer> wavesPerLevel;
	private Map<String, List<ImageView>> waveToImage;
	private Map<String, Object> myProperties;
	private List<String> elementsToSpawn;
	private Map<String, Integer> waveToId;

	private ClientMessageUtils clientMessageUtils;

	public LevelToolBar(EditDisplay created, AuthoringController controller, ScrollableArea area) {
		
		myScrollableArea = area;
		currentDisplay = 1;
		myCreated = created;
		myController = controller;
		clientMessageUtils = new ClientMessageUtils();
		myGameAreas = new ArrayList<>();
		this.setLayoutX(X_LAYOUT);
		this.setLayoutY(Y_LAYOUT);
		this.setWidth(400);
		myLevels = new ArrayList<>();
		mySprites = new ArrayList<>();
		mySprites.add(new ArrayList<>());
		newLevel = new Button("New Level");
		Button newWaveButton = new Button("New Wave");
		wavesPerLevel = new TreeMap<Integer, Integer>();
		waveToId = new TreeMap<String, Integer>();
		waveToImage = new TreeMap<String, List<ImageView>>();
		newLevel.setOnAction(e -> addLevel());
		newWaveButton.setOnAction(e->newWaveButtonPressed());
		myTabPane = new TabPane();
		tabMaker = new TabFactory();
		mySpriteDisplay = new SpriteDisplayer();
		myWaveDisplay = new WaveDisplay(this);
		this.getChildren().add(myWaveDisplay);
		this.getChildren().add(mySpriteDisplay);
		myTabPane.setMaxSize(400, 100);
		myTabPane.setPrefSize(400, 100);
		editLevel = new Button("Edit Level");
		// Need to put the button somewhere first.
		editLevel.setOnAction(e -> {
//			myLevels.get(currentDisplay - 1).openLevelDisplay();
			openLevelDisplay();
			// edited = true;
			// this.update();
		});
//		Button waveButton = new Button("Wave");
//		waveButton.addEventHandler(MouseEvent.MOUSE_CLICKED, 
//				e->{ try {
//					elementsToSpawn = new ArrayList<String>();
//					elementsToSpawn.add(waveToImage.get("1.1").get(0).getId());
//			myController.createWaveProperties(myProperties, elementsToSpawn, new Point2D(100, 100));
//			
//		} catch (ReflectiveOperationException exc) {
//		}});
		elementsToSpawn = new ArrayList<String>();
		this.getChildren().add(myTabPane);
		this.getChildren().add(newLevel);
		this.getChildren().add(editLevel);
		this.getChildren().add(newWaveButton);
//		this.getChildren().add(waveButton);
		loadLevels();
		created.setGameArea(myGameAreas.get(0));
		createProperties();
	}

	private void createProperties() {
		myProperties = new TreeMap<>();
		myProperties.put("Collision effects", "Invulnerable to collision damage");
		myProperties.put("Collided-with effects", "Do nothing to colliding objects");
		myProperties.put("Move an object", "Object will stay at desired location");
		myProperties.put("Firing Behavior", "Shoot periodically");
		myProperties.put("imageHeight", 40);
		myProperties.put("imageWidth", 40);
		myProperties.put("imageUrl", "monkey.png");
		myProperties.put("Name", "myWave");
		myProperties.put("tabName", "Troops");
		myProperties.put("Range of tower", 50000);
		myProperties.put("Attack period", 60);
		myProperties.put("Firing Sound", "Sounds");
//		myProperties.put("Projectile Type Name", "projectile1");
		myProperties.put("Numerical \"team\" association", 1);
//		myProperties.put("period", 10000);
//		myProperties.put("totalWaves", 1);
//		myProperties.put("templatesToFire", "myTroop");
		

	}
	
	private void newWaveButtonPressed() {
		wavesPerLevel.put(currentDisplay, wavesPerLevel.get(currentDisplay)+1);
		updateWaveDisplay();
	}

	private void openLevelDisplay() {
		myLevelDisplayer = new LevelsEditDisplay(myController, myCreated);
		myLevelDisplayer.open();
	}
	
	private void updateWaveDisplay() {
		myWaveDisplay.addTabs(wavesPerLevel.get(currentDisplay));
		updateImages();
	}

	private void loadLevels() {
		if (myController.getGameName().equals("untitled")
				|| myController.getNumLevelsForGame(myController.getGameName(), true) == 0) {
			addLevel();
			return;
		}
		for (int i = 1; i <= myController.getNumLevelsForGame(myController.getGameName(), true); i++) {
			addLevel();
			initializeSprites(i);
		}
	}
	
	private void addLevel() {
		mySprites.add(new ArrayList<>());
		Tab newTab = tabMaker.buildTabWithoutContent("Level " + Integer.toString(myLevels.size() + 1), null, myTabPane);
		newTab.setContent(mySpriteDisplay);
		LevelTab newLv = new LevelTab(myLevels.size() + 1, myController);
		myGameAreas.add(new GameArea(myController));
		myController.setLevel(myLevels.size() + 1);
		wavesPerLevel.put(myLevels.size()+1, 1);
		if (myLevels.size() == 0) {
			newTab.setClosable(false);
		} else {
			newTab.setOnClosed(e -> deleteLevel(newLv.getLvNumber()));
		}
		newTab.setOnSelectionChanged(e -> changeDisplay(newLv.getLvNumber()));
		newLv.attach(newTab);
		myLevels.add(newLv);
		myTabPane.getTabs().add(newTab);
	}

	// TODO need load in static object rather than just imageview
	private void initializeSprites(int level) {
		try {
			clientMessageUtils
					.initializeLoadedLevel(myController.loadOriginalGameState(myController.getGameName(), level));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(myController.getLevelSprites(level));
		for (Integer id : myController.getLevelSprites(level).stream().map(levelSprite -> levelSprite.getSpriteId()).collect(Collectors.toList())) {
			ImageView imageView = clientMessageUtils.getRepresentationFromSpriteId(id);
			InteractiveObject savedObject = new InteractiveObject(myCreated, imageView.getImage().toString());
			savedObject.setImageView(imageView);
			myGameAreas.get(level - 1).addBackObject(savedObject);
		}
	}
	
	public void addToWave (String levelAndWave, int amount, ImageView mySprite) {
		String[] levelWaveArray = levelAndWave.split("\\s+");
		elementsToSpawn.add(mySprite.getId());
		System.out.println(myProperties);
		System.out.println(Arrays.asList(elementsToSpawn).toString());
		try {
			myProperties.put("Projectile Type Name", "myTroop");
			myController.createWaveProperties(myProperties, elementsToSpawn, new Point2D(30,60));
		} catch (ReflectiveOperationException e) {
			System.out.println("Could not add to wave");
			e.printStackTrace();
		}
//		for (String s : levelWaveArray) {
//			
//			for (int i = 0; i < amount; i++) {
//				if (waveToImage.get(s) != null) {
//					//Editing a previously defined wave
////					myController.editWaveProperties(waveId, updatedProperties, newElementNamesToSpawn, newSpawningPoint);
//					waveToImage.get(s).add(mySprite);
//				} else {
//					//New wave you've never seen before
//					ArrayList<ImageView> newImages = new ArrayList<ImageView>();
//					newImages.add(mySprite);
//					waveToImage.put(s, newImages);
//					elementsToSpawn.clear();
//					int level = Integer.valueOf(s.split("\\.+")[0]);
//					changeDisplay(level);
//					for (ImageView imageView : waveToImage.get(s)) {
//						elementsToSpawn.add(imageView.getId());
//					}
//					try {
//						waveToId.put(s, myController.createWaveProperties
//								(myProperties, elementsToSpawn, new Point2D(100,100)));
//						System.out.printf("Created level %s", s);
//						System.out.println(waveToId.get(s));
//					} catch (ReflectiveOperationException e) {
//						System.out.println("Not able to create level");
//						e.printStackTrace();
//					}
//				}
//	
//			}
//		}
//		updateImages();
	}
	
	public void changeDisplay(int i) {
		currentDisplay = i;
		myScrollableArea.changeLevel(myGameAreas.get(i - 1));
		myCreated.setDroppable(myGameAreas.get(i - 1));
		myController.setLevel(i);
		myCreated.setGameArea(myGameAreas.get(i - 1));
//		updateSpriteDisplay(i);
		updateWaveDisplay();
		updateImages();
	}
	
	public void updateImages() {
		mySpriteDisplay.clear();
		if (waveToImage.get(currentDisplay + "." + myWaveDisplay.getCurrTab()) != null) {
			mySpriteDisplay.addToScroll(waveToImage.get(currentDisplay + "." + myWaveDisplay.getCurrTab()));
		}
	}

	private void deleteLevel(int lvNumber) {
		myController.deleteLevel(lvNumber);
		myLevels.remove(lvNumber - 1);
		myGameAreas.remove(lvNumber - 1);
		for (int i = lvNumber - 1; i < myLevels.size(); i++) {
			myLevels.get(i).decrementLevel();
			myTabPane.getTabs().get(i).setText("Level " + Integer.toString(i + 1));
		}

	}

	public int getMaxLevel() {
		return myLevels.size();
	}

	public void addLevelProperties(ImageView currSprite, int level) {
		myLevels.get(level - 1).update(currSprite);

	}
}