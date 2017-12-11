package authoring;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import authoring.LevelToolBar.LevelToolBar;
import authoring.PropertiesToolBar.PropertiesToolBar;
import authoring.PropertiesToolBar.SpriteImage;
import authoring.customize.AttackDefenseToggle;
import authoring.customize.ColorChanger;
import authoring.customize.ThemeChanger;
import authoring.spriteTester.SpriteTesterButton;
import engine.authoring_engine.AuthoringController;
import factory.MediaPlayerFactory;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import main.Main;
import networking.protocol.PlayerServer;
import networking.protocol.PlayerServer.NewSprite;
import player.PlayDisplay;
import util.protocol.ClientMessageUtils;
import display.splashScreen.ScreenDisplay;
import display.sprites.BackgroundObject;
import display.sprites.InteractiveObject;
import display.sprites.StaticObject;
import display.tabs.SaveDialog;
import display.toolbars.StaticObjectToolBar;

public class EditDisplay extends ScreenDisplay implements AuthorInterface {

	private static final double GRID_X_LOCATION = 620;
	private static final double GRID_Y_LOCATION = 20;
	private final String PATH_DIRECTORY_NAME = "authoring/";
	private AuthoringController controller;
	private StaticObjectToolBar myLeftToolBar;
	private GameArea myGameArea;
	private ScrollableArea myGameEnvironment;
	private PropertiesToolBar myRightToolBar;
	private MainMenuBar myMenuBar;
	private ToggleButton gridToggle;
	private ToggleButton movementToggle;
	private ColorChanger myColorChanger;
	private ThemeChanger myThemeChanger;
	private AttackDefenseToggle myGameChooser;
	private Label attackDefenseLabel;
	private Map<String, String> basePropertyMap;
	private LevelToolBar myBottomToolBar;
	private VBox myLeftBar;
	private VBox myLeftButtonsBar;
	private SpriteTesterButton myTesterButton;
	private Slider volumeSlider;
	private MediaPlayerFactory mediaPlayerFactory;
	private MediaPlayer mediaPlayer;

	private ClientMessageUtils clientMessageUtils;

	public EditDisplay(int width, int height, Stage stage, boolean loaded) {
		super(width, height, Color.BLACK, stage);
		controller = new AuthoringController();
		clientMessageUtils = new ClientMessageUtils();
		if (loaded) {
			loadGame();
		}
		myLeftButtonsBar = new VBox();
		myLeftBar = new VBox();
		basePropertyMap = new HashMap<>();
		addItems();
		formatLeftBar();
		setStandardTheme();
		createGridToggle();
		createMovementToggle();
		createLabel();
		basePropertyMap = new HashMap<>();
		Button saveButton = new Button("Save");
		saveButton.setLayoutY(600);
		rootAdd(saveButton);
		myTesterButton = new SpriteTesterButton(this);
		rootAdd(myTesterButton);
		mediaPlayerFactory = new MediaPlayerFactory("src/MediaTesting/110 - pokemon center.mp3");
		mediaPlayer = mediaPlayerFactory.getMediaPlayer();
		mediaPlayer.play();
		mediaPlayer.volumeProperty().bindBidirectional(volumeSlider.valueProperty());
	}

	private void createGridToggle() {
		gridToggle = new ToggleButton();
		gridToggle.setLayoutX(GRID_X_LOCATION);
		gridToggle.setLayoutY(GRID_Y_LOCATION);
		gridToggle.setSelected(true);
		gridToggle
				.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("grid_icon.png"))));
		gridToggle.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			myGameArea.toggleGridVisibility(gridToggle.isSelected());
		});
		rootAdd(gridToggle);
	}

	private void createMovementToggle() {
		movementToggle = new ToggleButton();
		movementToggle.setLayoutX(GRID_X_LOCATION - 40);
		movementToggle.setLayoutY(GRID_Y_LOCATION);
		movementToggle.setSelected(false);
		movementToggle.setGraphic(
				new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("scroll_arrow_icon.png"))));
		movementToggle.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> toggleMovement(movementToggle));
		rootAdd(movementToggle);
	}

	private void toggleMovement(ToggleButton movement) {
		myGameArea.toggleMovement(movementToggle.isSelected());
		if (movement.isSelected()) {
			this.getScene().setCursor(new ImageCursor(
					new Image(getClass().getClassLoader().getResourceAsStream("scroll_arrow_icon.png")), 30, 30));
		} else {
			this.getScene().setCursor(Cursor.DEFAULT);
		}
	}

	private void createLabel() {
		attackDefenseLabel = new Label("Defense");
		// styleLabel(attackDefenseLabel);
		attackDefenseLabel.setFont(new Font("Times New Roman", 35));
		// attackDefenseLabel.setFont(new Font("American Typewriter", 40));
		// attackDefenseLabel.setFont(new Font("Cambria", 40));
		attackDefenseLabel.setLayoutX(260);
		attackDefenseLabel.setLayoutY(25);
		rootAdd(attackDefenseLabel);
	}

	private void formatLeftBar() {
		myLeftBar.setLayoutY(30);
		myLeftBar.setSpacing(30);
		myLeftButtonsBar.setSpacing(20);
	}

	private void addItems() {
		myGameArea = new GameArea(controller);
		myGameEnvironment = new ScrollableArea(myGameArea);
		rootAdd(myGameEnvironment);
		this.setDroppable(myGameArea);
		addToLeftBar();
		rootAdd(myLeftBar);
		myRightToolBar = new PropertiesToolBar(this, controller);
		rootAdd(myRightToolBar);
		myThemeChanger = new ThemeChanger(this);
		rootAdd(myThemeChanger);
		myMenuBar = new MainMenuBar(this, controller);
		rootAdd(myMenuBar);
		myBottomToolBar = new LevelToolBar(this, controller, myGameEnvironment);
		rootAdd(myBottomToolBar);
		volumeSlider = new Slider(0, 1, .1);
		rootAdd(volumeSlider);
	}

	private void addToLeftBar() {
		myLeftToolBar = new StaticObjectToolBar(this, controller);
		myLeftBar.getChildren().add(myLeftToolBar);
		addToLeftButtonsBar();
		myLeftBar.getChildren().add(myLeftButtonsBar);
	}

	private void addToLeftButtonsBar() {
		myColorChanger = new ColorChanger(this);
		myLeftButtonsBar.getChildren().add(myColorChanger);
		myGameChooser = new AttackDefenseToggle(this);
		myLeftButtonsBar.getChildren().add(myGameChooser);
	}

	@Override
	public void listItemClicked(ImageView clickable) {
		StaticObject object = (StaticObject) clickable;
		Button addNewButton = new Button("New");
		Button incrementButton = new Button("+");
		Button decrementButton = new Button("-");
		addNewButton.setLayoutY(20);
		incrementButton.setLayoutY(20);
		decrementButton.setLayoutY(20);
		incrementButton.setLayoutX(50);
		decrementButton.setLayoutX(85);
		addNewButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> addObject(object));
		incrementButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			object.incrementSize();
			updateObjectSize(object);
		});
		decrementButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			object.decrementSize();
			updateObjectSize(object);
		});
		rootAdd(addNewButton);
		rootAdd(incrementButton);
		rootAdd(decrementButton);
	}

	private void updateObjectSize(StaticObject object) {
		Map<String, Object> newProperties = controller.getTemplateProperties(object.getElementName());
		newProperties.put("imageWidth", object.getSize());
		newProperties.put("imageHeight", object.getSize());
		controller.updateElementDefinition(object.getElementName(), newProperties, false);
	}

	private void addObject(InteractiveObject object) {
		InteractiveObject newObject;
		if (object instanceof BackgroundObject) {
			newObject = new BackgroundObject(object.getCellSize(), this, object.getElementName());
		} else {
			newObject = new StaticObject(object.getCellSize(), this, object.getElementName());
		}
		myGameArea.addBackObject(newObject);
		try {
			NewSprite newSprite = controller.placeElement(newObject.getElementName(), new Point2D(0, 0));
			newObject.setElementId(clientMessageUtils.addNewSpriteToDisplay(newSprite));
		} catch (ReflectiveOperationException failedToAddObjectException) {

		}

	}

	@Override
	public void newTowerSelected(ImageView myImageView) {

	}

	@Override
	public void clicked(SpriteImage imageView) {
		SelectionWindow mySelectionWindow = new SelectionWindow(imageView, this, controller);
	}

	@Override
	public void changeColor(String color) {
		myGameArea.changeColor(color);
	}

	@Override
	public void save() {
		File saveFile = SaveDialog.SaveLocation(getScene());
		if (saveFile != null) {
			controller.setGameName(saveFile.getName());
			// TODO change the save game so it saves a string instead
			controller.saveGameState(saveFile);
			myGameArea.savePath();
		}
	}

	private void loadGame() {
		List<String> games = new ArrayList<>();
		for (String title : controller.getAvailableGames().keySet()) {
			games.add(title);
		}
		Collections.sort(games);
		ChoiceDialog<String> loadChoices = new ChoiceDialog<>("Pick a saved game", games);
		loadChoices.setTitle("Load Game");
		loadChoices.setContentText(null);

		Optional<String> result = loadChoices.showAndWait();
		if (result.isPresent()) {
			try {
				clientMessageUtils.initializeLoadedLevel(controller.loadOriginalGameState(result.get(), 1));
			} catch (IOException e) {
				// TODO Change to alert for the user
				e.printStackTrace();
			}
		} else {
			returnButtonPressed();
		}
	}

	public void changeTheme(String theme) {
		rootStyleAndClear(myThemeChanger.getThemePath(theme));
		myRightToolBar.getStyleClass().add("borders");
		myLeftToolBar.getStyleClass().add("borders");
		myLeftBar.getStyleClass().add("outer-border");
		myLeftButtonsBar.getStyleClass().add("borders");
	}

	private void setStandardTheme() {
		changeTheme(ThemeChanger.STANDARD);
	}

	public void attack() {
		attackDefenseLabel.setText("Defense");
	}

	public void defense() {
		attackDefenseLabel.setText("Attack");
	}
	
	public void submit(String levelAndWave, int amount, ImageView mySprite) {
		myBottomToolBar.addToWave(levelAndWave, amount, mySprite);
	}

	@Override
	public String[] getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void returnButtonPressed() {
		if (!controller.getGameName().equals("untitled")) {
			controller.saveGameState(new File(PATH_DIRECTORY_NAME + controller.getGameName()));
		} else {
			this.save();
		}
		mediaPlayer.stop();
		VBox newProject = new VBox();
		Scene newScene = new Scene(newProject, 400, 400);
		Stage myStage = new Stage();
		myStage.setScene(newScene);
		myStage.show();
		Main restart = new Main();
		restart.start(myStage);
		getStage().close();
	}

	@Override
	public void imageSelected(SpriteImage imageView) {
		imageView.setBaseProperties(basePropertyMap);
		imageView.createInitialProperties(controller.getAuxiliaryElementConfigurationOptions(basePropertyMap));
		controller.defineElement(imageView.getId(), imageView.getAllProperties());
		controller.setUnitCost(imageView.getId(), new HashMap<>());
		controller.addElementToInventory(imageView.getId());
		myRightToolBar.imageSelected(imageView);
	}

	@Override
	public void addToMap(String baseProperty, String value) {
		basePropertyMap.put(baseProperty, value);
		// myRightToolBar.addToMap(baseProperty, value);
	}

	public void setGameArea(GameArea game) {
		this.myGameArea = game;
	}

	@Override
	public void createTesterLevel(Map<String, Object> fun, List<String> sprites) {
		// TODO - Update this method accordingly to determine the isMultiPlayer param
		// for PlayDisplay constructor
		PlayDisplay testingScene = new PlayDisplay(1000, 1000, getStage(), false); // TEMP
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		getStage().setX(primaryScreenBounds.getWidth() / 2 - 1000 / 2);
		getStage().setY(primaryScreenBounds.getHeight() / 2 - 1000 / 2);
		getStage().setScene(testingScene.getScene());
		controller.setGameName("testingGame");
		try {
			controller.createWaveProperties(fun, sprites, new Point2D(100, 100));
		} catch (ReflectiveOperationException failedToGenerateWaveException) {
			// todo - handle
		}
	}

	public void addToBottomToolBar(int level, ImageView currSprite, int kind) {
		if (kind==1) {
//			myBottomToolBar.addToWave(currSprite, level, 3);
		}
		if (kind == 2) {
			myBottomToolBar.addLevelProperties(currSprite, level);
		}
	}
	public int getMaxLevel() {
		return myBottomToolBar.getMaxLevel();
	}

}
