package player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import authoring.PlacementGrid;
import engine.PlayModelController;
import engine.behavior.collision.CollisionHandler;
import engine.behavior.collision.ImmortalCollider;
import engine.behavior.collision.NoopCollisionVisitable;
import engine.behavior.firing.FiringStrategy;
import engine.behavior.firing.NoopFiringStrategy;
import engine.behavior.movement.MovementStrategy;
import engine.behavior.movement.StationaryMovementStrategy;
import engine.play_engine.PlayController;
import factory.MediaPlayerFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Slider;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import networking.MultiPlayerClient;
import networking.protocol.PlayerServer.LevelInitialized;
import networking.protocol.PlayerServer.NewSprite;
import networking.protocol.PlayerServer.SpriteDeletion;
import networking.protocol.PlayerServer.SpriteUpdate;
import networking.protocol.PlayerServer.Update;
import util.io.SerializationUtils;
import util.protocol.ClientMessageUtils;
import display.factory.ButtonFactory;
import display.splashScreen.ScreenDisplay;
import display.splashScreen.SplashPlayScreen;
import display.sprites.StaticObject;
import display.toolbars.InventoryToolBar;

public class PlayDisplay extends ScreenDisplay implements PlayerInterface {

	private final String COST = "Cost";
	private final String GAME_FILE_KEY = "displayed-game-name";

	private InventoryToolBar myInventoryToolBar;
	private TransitorySplashScreen myTransition;
	private WinScreen myWinScreen;
	private GameOverScreen myGameOver;
	private MultiplayerLobby myMulti;
	private Scene myTransitionScene;
	private VBox myLeftBar;
	private PlayArea myPlayArea;
	private List<ImageView> currentElements;
	private PlayModelController myController;
	private Button pause;
	private Button play;
	private Timeline animation;
	private String gameState;
	private Slider volumeSlider;
	private MediaPlayerFactory mediaPlayerFactory;
	private MediaPlayer mediaPlayer;
	private ChoiceBox<Integer> levelSelector;
	private HUD hud;
	private String backgroundSong = "src/MediaTesting/128 - battle (vs gym leader).mp3";
	
//	private ButtonFactory buttonMaker;
//	private Button testButton;

	private int level = 1;
	private final FiringStrategy testFiring = new NoopFiringStrategy();
	private final MovementStrategy testMovement = new StationaryMovementStrategy(new Point2D(0, 0));
	private final CollisionHandler testCollision = new CollisionHandler(new ImmortalCollider(1),
			new NoopCollisionVisitable(), "https://pbs.twimg.com/media/CeafUfjUUAA5eKY.png", 10, 10);
	private boolean selected = false;
	private StaticObject placeable;

	private ClientMessageUtils clientMessageUtils;

	public PlayDisplay(int width, int height, Stage stage, PlayModelController myController) {
		super(width, height, Color.rgb(20, 20, 20), stage);
		
//		buttonMaker = new ButtonFactory();
//		testButton = buttonMaker.buildDefaultTextButton("Test scene", e -> testOpenMultiplayer(stage));
		
		this.myController = myController;
		myTransition = new TransitorySplashScreen(myController);
		myTransitionScene = new Scene(myTransition, width, height);
		myWinScreen = new WinScreen(width, height, Color.WHITE, stage);
		myGameOver = new GameOverScreen(width, height, Color.WHITE, stage);
//		myMulti = new MultiplayerLobby(width, height, Color.WHITE, stage, this);
		clientMessageUtils = new ClientMessageUtils();
		myLeftBar = new VBox();
		hud = new HUD(width);
		styleLeftBar();
		createGameArea(height - 20);
		addItems();
		this.setDroppable(myPlayArea);
		initializeGameState();
		initializeButtons();
		myInventoryToolBar.initializeInventory();
		hud.initialize(myController.getResourceEndowments());
		hud.toFront();
		volumeSlider = new Slider(0,1,.1);
		rootAdd(volumeSlider);
		volumeSlider.setLayoutY(7);
		volumeSlider.setLayoutX(55);
		mediaPlayerFactory = new MediaPlayerFactory(backgroundSong);
		mediaPlayer = mediaPlayerFactory.getMediaPlayer();
		mediaPlayer.play();
		mediaPlayer.volumeProperty().bindBidirectional(volumeSlider.valueProperty());
		
	}
	
	@Override
	public void startDisplay() {
		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step());
		animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();
	}
	
//	private void openSesame(Stage stage) {
//		stage.setScene(myWinScreen.getScene());
//		stage.setScene(myGameOver.getScene());
//	}
	
//	private void testOpenMultiplayer(Stage stage) {
//		stage.setScene(myMulti.getScene());
//	}

	private void addItems() {
		rootAdd(hud);
		myInventoryToolBar = new InventoryToolBar(this, myController);
		levelSelector = new ChoiceBox<>();
		levelSelector.getItems().addAll(1,2,3,4);
		levelSelector.setOnAction(e->{
			changeLevel(levelSelector.getSelectionModel().getSelectedItem());
			//Maybe clear the screen here?? myPlayArea.getChildren().clear() didn't work.
		});
		myLeftBar.getChildren().add(myInventoryToolBar);
		myLeftBar.getChildren().add(levelSelector);
		rootAdd(myLeftBar);
		
	}
	
	public void initializeGameState() {
		System.out.println("initialize");
		List<String> games = new ArrayList<>();
		try {
			for (String title : myController.getAvailableGames().keySet()) {
				games.add(title);
			}
			Collections.sort(games);
			ChoiceDialog<String> loadChoices = new ChoiceDialog<>("Saved games", games);
			loadChoices.setTitle("Load Game");
			loadChoices.setHeaderText("Choose a saved game.");
			loadChoices.setContentText(null);

			Optional<String> result = loadChoices.showAndWait();
			if (result.isPresent()) {
				try {
					gameState = result.get();
					clientMessageUtils.initializeLoadedLevel(myController.loadOriginalGameState(gameState, 1));
				} catch (IOException e) {
					// TODO Change to alert for the user
					e.printStackTrace();
				}
			}
		} catch (IllegalStateException e) {
			InputStream in = getClass().getClassLoader()
					.getResourceAsStream(SplashPlayScreen.EXPORTED_GAME_PROPERTIES_FILE);
			// sorry, this was lazy
			try {
				Properties exportedGameProperties = new Properties();
				exportedGameProperties.load(in);
				String gameName = exportedGameProperties.getProperty(GAME_FILE_KEY) + ".voog";
				clientMessageUtils.initializeLoadedLevel(myController.loadOriginalGameState(gameName, 1));
			} catch (IOException ioException) {
				// todo
			}
		}
	}

	protected void reloadGame() throws IOException {
		clientMessageUtils.initializeLoadedLevel(myController.loadOriginalGameState(gameState, 1));
	}

	private void styleLeftBar() {
		myLeftBar.setPrefHeight(650);
		myLeftBar.setLayoutY(25);
		myLeftBar.getStylesheets().add("player/resources/playerPanes.css");
		myLeftBar.getStyleClass().add("left-bar");
	}

	// TODO - can make it more efficient?
	private void loadSprites() {
		myPlayArea.getChildren().removeAll(currentElements);
		currentElements.clear();
		for (Integer id : clientMessageUtils.getCurrentSpriteIds()) {
			currentElements.add(clientMessageUtils.getRepresentationFromSpriteId(id));
			attachEventHandlers(clientMessageUtils.getRepresentationFromSpriteId(id), id);
		}
		myPlayArea.getChildren().addAll(currentElements);
	}

	private void initializeButtons() {
		pause = new Button();
		pause.setOnAction(e -> {
			myController.pause();
			animation.pause();
		});
		pause.setText("Pause");
		rootAdd(pause);
		pause.setLayoutY(myInventoryToolBar.getLayoutY() + 450);

		play = new Button();
		play.setOnAction(e -> {
			myController.resume();
			animation.play();
		});
		play.setText("Play");
		rootAdd(play);
		play.setLayoutY(pause.getLayoutY() + 30);
		
//		rootAdd(testButton);
//		testButton.setLayoutY(play.getLayoutY() + 30);
	}

	private void step() {
		Update latestUpdate = myController.update();
		if (myController.isReadyForNextLevel()) {
			hideTransitorySplashScreen();
			// animation.play();
			myController.resume();
		}
		if (myController.isLevelCleared()) {
			level++;
			animation.pause();
			myController.pause();
			launchTransitorySplashScreen();
			hud.initialize(myController.getResourceEndowments());
		} else if (myController.isLost()) {
			// launch lost screen
		} else if (myController.isWon()) {
			// launch win screen
		}
		hud.update(myController.getResourceEndowments());
		clientMessageUtils.handleSpriteUpdates(latestUpdate);
		loadSprites();
	}

	private void launchTransitorySplashScreen() {
		this.getStage().setScene(myTransitionScene);
	}

	private void hideTransitorySplashScreen() {
		this.getStage().setScene(this.getScene());
	}

	private void createGameArea(int sideLength) {
		myPlayArea = new PlayArea(myController, clientMessageUtils, sideLength, sideLength);
		myPlayArea.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> this.dropElement(e));
		currentElements = new ArrayList<ImageView>();
		rootAdd(myPlayArea);
	}

	private void dropElement(MouseEvent e) {
		if (selected) {
			selected = false;
			this.getScene().setCursor(Cursor.DEFAULT);
			if (e.getButton().equals(MouseButton.PRIMARY)) {
				Point2D startLocation = new Point2D(e.getX(), e.getY());
				try {
					NewSprite newSprite = myController.placeElement(placeable.getElementName(), startLocation);
					int id = clientMessageUtils.addNewSpriteToDisplay(newSprite);
					ImageView imageView = clientMessageUtils.getRepresentationFromSpriteId(id);
					attachEventHandlers(imageView, id);
					System.out.println(id);
					System.out.println("HIT");
				} catch (ReflectiveOperationException failedToPlaceElementException) {
					// todo - handle
				}
			}
		}
	}
	
	private void attachEventHandlers(ImageView imageView, int id) {
		imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
			if(e.getButton() == MouseButton.SECONDARY) {
				System.out.println("MESSAGE");
				deleteClicked(imageView);
			}else {
				upgradeClicked(imageView, id);
			}
		});
	}

	@Override
	public void listItemClicked(ImageView image) {
		if(!checkFunds(image)) return;
		Alert costDialog = new Alert(AlertType.CONFIRMATION);
		costDialog.setTitle("Purchase Resource");
		costDialog.setHeaderText(null);
		costDialog.setContentText("Would you like to purchase this object?");

		Optional<ButtonType> result = costDialog.showAndWait();
		if (result.get() == ButtonType.OK) {
			placeable = new StaticObject(1, this, (String) image.getUserData());
			placeable.setElementName(image.getId());
			this.getScene().setCursor(new ImageCursor(image.getImage()));
			selected = true;
		}
	}
	
	//TODO call this on click event of the static objects
	private void upgradeClicked(ImageView image, int id) {
		if(!checkFunds(image)) return;
		Alert costDialog = new Alert(AlertType.CONFIRMATION);
		costDialog.setTitle("Upgrade Resource");
		costDialog.setHeaderText(null);
		costDialog.setContentText("Would you like to upgrade this object?");
		
		Optional<ButtonType> result = costDialog.showAndWait();
		if (result.get() == ButtonType.OK) {
			try {
				myController.upgradeElement(id);
			} catch (IllegalArgumentException | ReflectiveOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//TODO allow towers to be sold or deleted?
	private void deleteClicked(ImageView image) {
		
	}
	
	private boolean checkFunds(ImageView image) {
		Map<String, Double> unitCosts = myController.getElementCosts().get(image.getId());
		if (!hud.hasSufficientFunds(unitCosts)) {
			launchInvalidResources();
			return false;
		}
		return true;
	}

	private void launchInvalidResources() {
		Alert error = new Alert(AlertType.ERROR);
		error.setTitle("Resource Error!");
		error.setHeaderText(null);
		error.setContentText("You do not have the funds for this item.");
		error.show();
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub

	}
	
	protected void changeLevel(int newLevel) {
		level = newLevel;
		try {
			clientMessageUtils.initializeLoadedLevel(myController.loadOriginalGameState(gameState, newLevel));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
