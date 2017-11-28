package player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import authoring.AuthorInterface;
import authoring.GameArea;
import authoring.PlacementGrid;
import authoring.leftToolBar.LeftToolBar;
import authoring.rightToolBar.SpriteImage;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import splashScreen.ScreenDisplay;
import sprites.BackgroundObject;
import sprites.Sprite;
import sprites.StaticObject;

public class PlayDisplay extends ScreenDisplay implements PlayerInterface {
	
	private GameToolBar myGameToolBar;
	private List<List<Sprite>> levelSpritesCache;
	private PlacementGrid myMainGrid;
	private HealthBar myHealthBar;
	private DecreaseHealthButton myDecreaseHealthButton;
	private HBox myGameArea;
	private PlayController myController;
	private AuthorInterface testAuthor;
	private CoinDisplay myCoinDisplay;
	
	private Collection<Sprite> testCollection;
	private final FiringStrategy testFiring =  new NoopFiringStrategy("test");
	private final MovementStrategy testMovement = new StationaryMovementStrategy();
	private final CollisionHandler testCollision = new CollisionHandler(new ImmortalCollider(1), new NoopCollisionVisitable());
	
	public PlayDisplay(int width, int height) {
		super(width, height, Color.BLUE);
		myCoinDisplay = new CoinDisplay();
		rootAdd(myCoinDisplay);
		
		createTestGameArea();

//		createTestSprites();
//		createTestGameArea();
		myController = new PlayController();
		rootAdd(new HealthBackground());
		myGameToolBar = new GameToolBar(this);
		rootAdd(myGameToolBar);
		myHealthBar = new HealthBar();
		rootAdd(myHealthBar);
		
		
		myDecreaseHealthButton = new DecreaseHealthButton(this);
		rootAdd(myDecreaseHealthButton);
		
		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
                e -> step());
		Timeline animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();
	}
	
	private void step() {
		myCoinDisplay.increment();
	}

	private void createTestGameArea() {
		myGameArea = new HBox();
		myGameArea.setLayoutX(300);
		myGameArea.setLayoutY(50);
		myGameArea.setPrefHeight(400);
		myGameArea.setPrefWidth(400);
		myGameArea.setStyle("-fx-background-color:white");
		rootAdd(myGameArea);
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
	
	private void drag(MouseEvent e, Rectangle currRectangle) {
		currRectangle.setX(e.getSceneX() - currRectangle.getWidth() / 2);
		currRectangle.setY(e.getSceneY() - currRectangle.getHeight() / 2);
	}
	
	private void released(Rectangle currRectangle) {
		if (!currRectangle.intersects(myMainGrid.getBoundsInParent())) {
			createNewErrorWindow();
		}
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
	public void clicked(StaticObject object) {
		// TODO Auto-generated method stub
		myCoinDisplay.decrease();
		
	}

	@Override
	public void dropped(StaticObject rec, MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pressed(StaticObject staticObject, MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clicked(SpriteImage sprite) {
		System.out.println(sprite.toString());
		
	}
	
	
}
