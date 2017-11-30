package player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import splashScreen.ScreenDisplay;
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
	private PlayController myController;
	private AuthorInterface testAuthor;
	private CoinDisplay myCoinDisplay;
	private HealthDisplay myHealthDisplay;
	private PointsDisplay myPointsDisplay;
	private Button pause;
	private Button play;
	private Timeline animation;

	private TowerImage tower1;
	private double xLocation = 0;
	private double yLocation = 0;
	private int level = 1;
	private Collection<Sprite> testCollection;
	private final FiringStrategy testFiring =  new NoopFiringStrategy();
	private final MovementStrategy testMovement = new StationaryMovementStrategy();
	private final CollisionHandler testCollision =
			new CollisionHandler(new ImmortalCollider(1), new NoopCollisionVisitable(),
					"https://pbs.twimg.com/media/CeafUfjUUAA5eKY.png", 10, 10);
	
	//TODO uncomment the initialization and get rid of tester
	public PlayDisplay(int width, int height, Stage stage) {
		super(width, height, Color.rgb(20, 20, 20), stage);
		myController = new PlayController();
		myLeftBar = new VBox();
		formatLeftBar();
		createGameArea(height - 20);
		addItems();
		this.setDroppable(myPlayArea);
		initializeGameState();
		initializeSprites();
		initializeInventory();
		initializeButtons();
		createTestImages();
//		createTestSprites();
//		createTestGameArea();
		
		
		
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
//		rootAdd(new HealthBackground());
		myInventoryToolBar = new InventoryToolBar(this);
		myLeftBar.getChildren().add(myInventoryToolBar);
		rootAdd(myLeftBar);
//		myHealthBar = new HealthBar();
//		rootAdd(myHealthBar);
//		myDecreaseHealthButton = new DecreaseHealthButton(this);
//		rootAdd(myDecreaseHealthButton);
	}

	private void createTestImages() {
		tower1 = new TowerImage(this, "Castle_Tower1");
		tower1.setFitHeight(40);
		tower1.setFitWidth(40);
		myPlayArea.getChildren().add(tower1);
	}
	
	private void initializeGameState() {
		List<String> games = new ArrayList<>();
		for(String title:myController.getAvailableGames().keySet()) {
			games.add(title);
		}
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
		ImageView newImage;
		for(String s:myController.getInventory()) {
			ImageView imageView;
			try {
				imageView = new ImageView(new Image(templates.get(s).get("imageUrl")));
				
			}catch(NullPointerException e) {
				imageView = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream(templates.get(s).get("imageURL"))));
			}
			imageView.setFitHeight(70);
			imageView.setFitWidth(60);
			imageView.setId(s);
			imageView.setUserData(templates.get(s).get("imageUrl"));
			myInventoryToolBar.addToToolbar(imageView);
		}
	}
	
	//TODO Make sure this works once saved files are all good
	private void initializeSprites() {
		for(Integer id:myController.getLevelSprites(level)) {
			ImageView imageView = myController.getRepresentationFromSpriteId(id);
			myPlayArea.getChildren().add(imageView);
		}
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
	
	private void step() {
		myCoinDisplay.increment();
		xLocation += 1;
		yLocation += 1;
		tower1.setLayoutX(xLocation);
		tower1.setLayoutY(yLocation);
		myController.update();
	}

	private void createGameArea(int sideLength) {
		myPlayArea = new PlayArea(myController, sideLength, sideLength);
		rootAdd(myPlayArea);
	}
	
	private Sprite createSprite() {
		Sprite tempSprite = new Sprite(testFiring, testMovement, testCollision);
		Image myImage = new Image(getClass().getClassLoader().getResourceAsStream("black_soldier.gif"));
		ImageView myImageView = new ImageView();
		myImageView.setImage(myImage);
//		tempSprite.setGraphicalRepresentation(myImageView);
		return tempSprite;
	}
	
	private void createNewErrorWindow() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Object placement error");
		alert.setHeaderText("Must place object in the main grid");
		alert.show();
	}

	@Override
	public void decreaseHealth() {
		myHealthBar.decreaseHealth(10);
	}

	@Override
	public void clicked(SpriteImage sprite) {
		System.out.println(sprite.toString());
		
	}

	//TODO clone objects so that they don't dissappear out of the list
	@Override
	public void listItemClicked(ImageView image) {
		StaticObject placeable = new StaticObject(1, this, (String) image.getUserData());
		placeable.setElementName(image.getId());
		myPlayArea.getChildren().add(placeable);
	}

	@Override
	public void save(File saveName) {
		myController.saveGameState(saveName);
	}
	
	private void formatLeftBar() {
		myLeftBar.setPrefHeight(650);
		myLeftBar.setLayoutY(25);
		myLeftBar.getStylesheets().add("player/resources/playerPanes.css");
		myLeftBar.getStyleClass().add("left-bar");
	}
}
