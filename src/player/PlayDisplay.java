package player;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import splashScreen.ScreenDisplay;
import sprites.InteractiveObject;
import sprites.Sprite;

public class PlayDisplay extends ScreenDisplay implements PlayerInterface {
	
	private GameToolBar myGameToolBar;
	private List<List<Sprite>> levelSpritesCache;
	private PlacementGrid myMainGrid;
	private HealthBar myHealthBar;
	private DecreaseHealthButton myDecreaseHealthButton;
	private PlayArea myPlayArea;
	private PlayController myController;
	private AuthorInterface testAuthor;
	private CoinDisplay myCoinDisplay;
	private Button pause;
	private Button play;
	
	private TowerImage tower1;
	private double xLocation = 0;
	private double yLocation = 0;
	private int level = 1;
	private Timeline animation;
	
	private Collection<Sprite> testCollection;
	private final FiringStrategy testFiring =  new NoopFiringStrategy("test");
	private final MovementStrategy testMovement = new StationaryMovementStrategy();
	private final CollisionHandler testCollision =
			new CollisionHandler(new ImmortalCollider(1), new NoopCollisionVisitable(),
					"https://pbs.twimg.com/media/CeafUfjUUAA5eKY.png", 10, 10);
	private PlayController tester;
	
	//TODO uncomment the initialization and get rid of tester
	public PlayDisplay(int width, int height) {
		super(width, height, Color.BLUE);
		myController = new PlayController();
		addItems();
		this.SetDroppable(myPlayArea);
//		initializeGameState();
//		initializeSprites();
		initializeButtons();
		createTestGameArea();
		createTestImages();
//		createTestSprites();
//		createTestGameArea();
		
		
		
		
		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
                e -> step());
		animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();
		
		tester.getLevelSprites(0);
		try {
			tester.loadSavedPlayState("game1");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Collection<Integer> currentSprites = tester.getLevelSprites(0);
		for (int i : currentSprites) {
			rootAdd(tester.getRepresentationFromSpriteId(i));
		}
	}

	private void addItems() {
		myCoinDisplay = new CoinDisplay();
		rootAdd(myCoinDisplay);
		rootAdd(new HealthBackground());
		myGameToolBar = new GameToolBar(this);
		rootAdd(myGameToolBar);
		myHealthBar = new HealthBar();
		rootAdd(myHealthBar);
		myDecreaseHealthButton = new DecreaseHealthButton(this);
		rootAdd(myDecreaseHealthButton);
	}

	private void createTestImages() {
		tower1 = new TowerImage(this, "Castle_Tower1");
		tower1.setFitHeight(40);
		tower1.setFitWidth(40);
		myPlayArea.placeInGrid(tower1);
	}
	
	//TODO Make sure this works once saved files are all good
	private void initializeSprites() {
		for(Integer id:myController.getLevelSprites(level)) {
			ImageView imageView = myController.getRepresentationFromSpriteId(id);
			myPlayArea.getChildren().add(imageView);
		}
	}
	
	//TODO Same as above
	private void initializeGameState() {
		List<String> games = new ArrayList<>();
		for(String title:myController.getAvailableGames().keySet()) {
			games.add(title);
		}
		ChoiceDialog<String> loadChoices = new ChoiceDialog<>("Pick a saved game", games);
		loadChoices.setTitle("Load Game");
		loadChoices.setContentText(null);
		loadChoices.setDialogPane(null);
		
		Optional<String> result = loadChoices.showAndWait();
		if(result.isPresent()) {
			//Insert method here that will cue the rest of initialization
			try {
				myController.loadOriginalGameState(result.get(), level);
			} catch (FileNotFoundException e) {
				// TODO Change to alert for the user 
				e.printStackTrace();
			}
		}
	}
	
	//TODO there is a problem where the buttons here aren't appearing
	private void initializeButtons() {
		pause = new Button();
		pause.setOnAction(e-> myController.pause());
		pause.setText("Pause");
		rootAdd(pause);
		
		play = new Button();
		play.setOnAction(e-> myController.resume());
		play.setText("Play");
		rootAdd(play);
	}
	
	private void step() {
		
		myCoinDisplay.increment();
		xLocation += 1;
		yLocation += 1;
		tower1.setLayoutX(xLocation);
		tower1.setLayoutY(yLocation);
		tester.update();
		if (tester.isWon()) {
			System.out.println("You win!");
			animation.stop();
		} if (tester.isLost()) {
			System.out.println("You lose!");
			animation.stop();
		}
	}

	private void createTestGameArea() {
		myPlayArea = new PlayArea(this);
		rootAdd(myPlayArea);
	}
	
	
	
	private void createTestSprites() {
		//Method to work on player before linking with backend
		Sprite test1_0 = createSprite();
		Sprite test1_1 = createSprite();
		Sprite test1_2 = createSprite();
		Sprite test2_0 = createSprite();
		Sprite test2_1 = createSprite();
		Sprite test3_0 = createSprite();
		Sprite test3_1 = createSprite();
		List<Sprite> spriteList = new ArrayList<>();
		spriteList.add(test1_0);
		spriteList.add(test1_1);
		spriteList.add(test1_2);
		spriteList.add(test2_0);
		spriteList.add(test2_1);
		spriteList.add(test3_0);
		spriteList.add(test3_1);
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
	public void listItemClicked(InteractiveObject clickable) {
		myPlayArea.getChildren().add(clickable);
		clickable.setLocked(false);
	}

	@Override
	public void save(String saveName) {
		myController.saveGameState(saveName);
	}
	
}
