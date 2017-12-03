package player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import authoring.AuthorInterface;
import authoring.PlacementGrid;
import authoring.rightToolBar.SpriteImage;
import authoring.rightToolBar.TowerImage;
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
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import splashScreen.ScreenDisplay;
import sprites.InteractiveObject;
import sprites.Sprite;
import sprites.StaticObject;

public class PlayDisplay extends ScreenDisplay implements PlayerInterface {
	
	private InventoryToolBar myInventoryToolBar;
	private VBox myLeftBar;
	private List<List<Sprite>> levelSpritesCache;
	private PlacementGrid myMainGrid;
	private HealthBar myHealthBar;
	private DecreaseHealthButton myDecreaseHealthButton;
	private PlayArea myPlayArea;
	private List<ImageView> currentElements;
	private PlayController myController;
	private CoinDisplay myCoinDisplay;
	private HealthDisplay myHealthDisplay;
	private PointsDisplay myPointsDisplay;
	private Button pause;
	private Button play;
	private Timeline animation;

	private int level = 1;
	private Collection<Sprite> testCollection;
	private final FiringStrategy testFiring =  new NoopFiringStrategy();
	private final MovementStrategy testMovement = new StationaryMovementStrategy();
	private final CollisionHandler testCollision =
			new CollisionHandler(new ImmortalCollider(1), new NoopCollisionVisitable(),
					"https://pbs.twimg.com/media/CeafUfjUUAA5eKY.png", 10, 10);
	private boolean selected = false;
	private StaticObject placeable;
	
	public PlayDisplay(int width, int height, Stage stage) {
		super(width, height, Color.rgb(20, 20, 20), stage);
		myController = new PlayController();
		myLeftBar = new VBox();
		styleLeftBar();
		createGameArea(height - 20);
		addItems();
		this.setDroppable(myPlayArea);
		initializeGameState();
		initializeInventory();
		initializeButtons();
		
		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
                e -> step());
		animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();
	}

	private void addItems() {
		myCoinDisplay = new CoinDisplay();
		rootAdd(myCoinDisplay);
		myHealthDisplay = new HealthDisplay();
		rootAdd(myHealthDisplay);
		myPointsDisplay = new PointsDisplay();
		rootAdd(myPointsDisplay);
		myInventoryToolBar = new InventoryToolBar(this);
		myLeftBar.getChildren().add(myInventoryToolBar);
		rootAdd(myLeftBar);
	}
	
	private void initializeGameState() {
		List<String> games = new ArrayList<>();
		for(String title:myController.getAvailableGames().keySet()) {
			games.add(title);
		}
		Collections.sort(games);
		ChoiceDialog<String> loadChoices = new ChoiceDialog<>("Pick a saved game", games);
		loadChoices.setTitle("Load Game");
		loadChoices.setContentText(null);
		
		Optional<String> result = loadChoices.showAndWait();
		if(result.isPresent()) {
			try {
				myController.loadOriginalGameState(result.get(), 1);
			} catch (IOException e) {
				// TODO Change to alert for the user 
				e.printStackTrace();
			}
		}
	}
	
	private void initializeInventory() {
		Map<String, Map<String, String>> templates = myController.getAllDefinedTemplateProperties();
		for(String s:myController.getInventory()) {
			ImageView imageView;
			System.out.println(s);
			try {
				imageView = new ImageView(new Image(templates.get(s).get("imageUrl")));
				
			}catch(NullPointerException e) {
				imageView = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream(templates.get(s).get("imageUrl"))));
			}
			imageView.setFitHeight(70);
			imageView.setFitWidth(60);
			imageView.setId(s);
			imageView.setUserData(templates.get(s).get("imageUrl"));
			myInventoryToolBar.addToToolbar(imageView);
		}
	}
	
	
	private void styleLeftBar() {
		myLeftBar.setPrefHeight(650);
		myLeftBar.setLayoutY(25);
		myLeftBar.getStylesheets().add("player/resources/playerPanes.css");
		myLeftBar.getStyleClass().add("left-bar");
	}
	
	private void initializeButtons() {
		pause = new Button();
		pause.setOnAction(e-> {
			myController.pause();
			animation.pause();
		});
		pause.setText("Pause");
		rootAdd(pause);
		pause.setLayoutY(myInventoryToolBar.getLayoutY() + 450);

		play = new Button();
		play.setOnAction(e-> {
			myController.resume();
			animation.play();
		});
		play.setText("Play");
		rootAdd(play);
		play.setLayoutY(pause.getLayoutY() + 30);
	}
	
	private void loadSprites() {
		myPlayArea.getChildren().removeAll(currentElements);
		currentElements.clear();
		for(Integer id : myController.getLevelSprites(level)) {
			currentElements.add(myController.getRepresentationFromSpriteId(id));
		}
		myPlayArea.getChildren().addAll(currentElements);
	}
	
	private void step() {
		myCoinDisplay.increment();
		myController.update();
		if(myController.isLevelCleared()) {
			
		}
		loadSprites();
	}

	private void createGameArea(int sideLength) {
		myPlayArea = new PlayArea(myController, sideLength, sideLength);
		myPlayArea.addEventHandler(MouseEvent.MOUSE_CLICKED, e->this.dropElement(e));
		currentElements = new ArrayList<ImageView>();
		rootAdd(myPlayArea);
	}

	@Override
	public void decreaseHealth() {
		myHealthBar.decreaseHealth(10);
	}

	@Override
	public void listItemClicked(ImageView image) {
		placeable = new StaticObject(1, this, (String) image.getUserData());
		placeable.setElementName(image.getId());
		this.getScene().setCursor(new ImageCursor(image.getImage()));
		selected = true;
	}
	
	private void dropElement(MouseEvent e) {
		if(selected) {
			selected = false;
			this.getScene().setCursor(Cursor.DEFAULT);
			if(e.getButton().equals(MouseButton.PRIMARY)) myController.placeElement(placeable.getElementName(), new Point2D(e.getX(),e.getY()));
		}
	}

	@Override
	public void save(File saveName) {
		myController.saveGameState(saveName);
	}
}
