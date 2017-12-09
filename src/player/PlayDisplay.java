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
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Slider;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import networking.MultiPlayerClient;
import networking.protocol.PlayerServer.LevelInitialized;
import networking.protocol.PlayerServer.NewSprite;
import networking.protocol.PlayerServer.SpriteDeletion;
import networking.protocol.PlayerServer.SpriteUpdate;
import networking.protocol.PlayerServer.Update;
import util.protocol.ClientMessageUtils;
import display.splashScreen.ScreenDisplay;
import display.splashScreen.SplashPlayScreen;
import display.sprites.StaticObject;
import display.toolbars.InventoryToolBar;

public class PlayDisplay extends ScreenDisplay implements PlayerInterface {

	private final String COST = "Cost";
	private final String GAME_FILE_KEY = "gameFile";

	private InventoryToolBar myInventoryToolBar;
	private VBox myLeftBar;
	private PlayArea myPlayArea;
	private List<ImageView> currentElements;
	private PlayModelController myController;
	private Button pause;
	private Button play;
	private Timeline animation;
	private String gameState;
	private Slider volumeSlider;
	private HUD hud;

	private int level = 1;
	private final FiringStrategy testFiring = new NoopFiringStrategy();
	private final MovementStrategy testMovement = new StationaryMovementStrategy(new Point2D(0, 0));
	private final CollisionHandler testCollision = new CollisionHandler(new ImmortalCollider(1),
			new NoopCollisionVisitable(), "https://pbs.twimg.com/media/CeafUfjUUAA5eKY.png", 10, 10);
	private boolean selected = false;
	private StaticObject placeable;

	private ClientMessageUtils clientMessageUtils;

	public PlayDisplay(int width, int height, Stage stage, boolean isMultiPlayer) {
		super(width, height, Color.rgb(20, 20, 20), stage);
		myController = isMultiPlayer ? new MultiPlayerClient() : new PlayController();
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

		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step());
		animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();
		tester();

	}

	public void tester() {
		for (int i = 0; i < 100; i++) {
			step();
		}
	}

	private void addItems() {
		rootAdd(hud);
		myInventoryToolBar = new InventoryToolBar(this, myController);
		myLeftBar.getChildren().add(myInventoryToolBar);
		rootAdd(myLeftBar);
		volumeSlider = new Slider(0, 100, 5);
		rootAdd(volumeSlider);
	}

	public void initializeGameState() {
		List<String> games = new ArrayList<>();
		try {
			for (String title : myController.getAvailableGames().keySet()) {
				games.add(title);
			}
			Collections.sort(games);
			ChoiceDialog<String> loadChoices = new ChoiceDialog<>("Pick a saved game", games);
			loadChoices.setTitle("Load Game");
			loadChoices.setContentText(null);

			Optional<String> result = loadChoices.showAndWait();
			if (result.isPresent()) {
				try {
					gameState = result.get();
					clientMessageUtils.initializeLoadedLevel(myController.loadOriginalGameState(gameState, 1));
					System.out.println(gameState);
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
				String gameName = exportedGameProperties.getProperty(GAME_FILE_KEY);
				System.out.println("GN: " + gameName);
				clientMessageUtils.initializeLoadedLevel(myController.loadOriginalGameState(gameName, 1));
			} catch (IOException ioException) {
				// todo
			}
		}
	}

	protected void reloadGame() throws IOException {
		clientMessageUtils.initializeLoadedLevel(myController.loadOriginalGameState(gameState, 1));
	}
	//
	// private void initializeInventory() {
	// Map<String, Map<String, String>> templates =
	// myController.getAllDefinedTemplateProperties();
	// for (String s : myController.getInventory()) {
	// ImageView imageView;
	// try {
	// imageView = new ImageView(new Image(templates.get(s).get("imageUrl")));
	//
	// } catch (NullPointerException e) {
	// imageView = new ImageView(
	// new
	// Image(getClass().getClassLoader().getResourceAsStream(templates.get(s).get("imageUrl"))));
	// }
	// imageView.setFitHeight(70);
	// imageView.setFitWidth(60);
	// imageView.setId(s);
	// imageView.setUserData(templates.get(s).get("imageUrl"));
	// // myInventoryToolBar.addToToolbar(imageView);
	// }
	// }

	private void styleLeftBar() {
		myLeftBar.setPrefHeight(650);
		myLeftBar.setLayoutY(25);
		myLeftBar.getStylesheets().add("player/resources/playerPanes.css");
		myLeftBar.getStyleClass().add("left-bar");
	}

	/*
	 * private void loadSprites() {
	 * myPlayArea.getChildren().removeAll(currentElements); currentElements.clear();
	 * for (Integer id : myController.getLevelSprites(level)) {
	 * currentElements.add(myController.getRepresentationFromSpriteId(id)); }
	 * myPlayArea.getChildren().addAll(currentElements); }
	 */

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
	}

	private void step() {
		Update latestUpdate = myController.update();
		if (myController.isLevelCleared()) {
			level++;
			animation.pause();
			myController.pause();
			hud.initialize(myController.getResourceEndowments());
		} else if (myController.isLost()) {
			// launch lost screen
		} else if (myController.isWon()) {
			// launch win screen
		}
		hud.update(myController.getResourceEndowments());
		clientMessageUtils.handleSpriteUpdates(latestUpdate);
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
			if (e.getButton().equals(MouseButton.PRIMARY))
				clientMessageUtils.addNewSpriteToDisplay(
						myController.placeElement(placeable.getElementName(), new Point2D(e.getX(), e.getY())));
		}
	}

	@Override
	public void listItemClicked(ImageView image) {
		Map<String, Double> unitCosts = myController.getElementCosts().get(image.getId());
		System.out.println("Get");
		if (!hud.hasSufficientFunds(unitCosts)) {
			launchInvalidResources();
			return;
		} else {
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

	}

	private void launchInvalidResources() {
		Alert error = new Alert(AlertType.ERROR);
		error.setTitle("Resource Error!");
		error.setHeaderText(null);
		error.setContentText("You do not have the funds for this item.");
		error.show();
	}

	// TODO - Check if this is repeated code,

	public void save(File saveName) {
		myController.saveGameState(saveName);
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

}
