package player;

import java.util.ArrayList;
import java.util.Collection;

import authoring.AuthorInterface;
import authoring.GameArea;
import authoring.PlacementGrid;
import authoring.leftToolBar.LeftToolBar;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
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
	private PlacementGrid myMainGrid;
	private HealthBar myHealthBar;
	private DecreaseHealthButton myDecreaseHealthButton;
	private GameArea myGameArea;
	private PlayController myController;
	private AuthorInterface testAuthor;
	private int coins;
	private Label myCoins;
	private Collection<Sprite> testCollection;
	private final FiringStrategy testFiring =  new NoopFiringStrategy("test");
	private final MovementStrategy testMovement = new StationaryMovementStrategy();
	
	public PlayDisplay(int width, int height) {
		super(width, height, Color.BLUE);
		createTestGameArea();
		myController = new PlayController();
		createTestingSprites();
		rootAdd(new HealthBackground());
		myGameToolBar = new GameToolBar(this);
		rootAdd(myGameToolBar);
		myHealthBar = new HealthBar();
		rootAdd(myHealthBar);
		coins = 0;
		myCoins = new Label("0");
		myCoins.setLayoutX(400);
		myCoins.setFont(new Font(30));
		myCoins.setTextFill(Color.WHITE);
		rootAdd(myCoins);
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
		coins++;
		myCoins.setText(Integer.toString(coins));
//		System.out.println(counter);
	}

	private void createTestGameArea() {
		myGameArea = new GameArea(testAuthor);
		myGameArea.setLayoutX(300);
		myGameArea.setLayoutY(50);
		myGameArea.setPrefHeight(400);
		myGameArea.setPrefWidth(400);
		rootAdd(myGameArea);
	}
	
	private void createTestingSprites() {
		//Method to work on player before linking with backend
		testCollection = new ArrayList<Sprite>();
		Sprite test1 = new Sprite(testFiring, testMovement, null);
		Sprite test2 = new Sprite(testFiring, testMovement, null);
		Sprite test3 = new Sprite(testFiring, testMovement, null);
		Sprite test4 = new Sprite(testFiring, testMovement, null);
		testCollection.add(test1);
		testCollection.add(test2);
		testCollection.add(test3);
		testCollection.add(test4);
		for (Sprite s : testCollection) {
//			myGameArea.getChildren().add(s);
		}
	}

	@Override
	public void clicked(Rectangle rec) {
		// TODO Auto-generated method stub
		Rectangle currRectangle = new Rectangle(rec.getWidth(), rec.getHeight(), rec.getFill());
		currRectangle.addEventHandler(MouseEvent.MOUSE_DRAGGED, e->drag(e, currRectangle));
		currRectangle.addEventHandler(MouseEvent.MOUSE_RELEASED, e->released(currRectangle));
		rootAdd(currRectangle);
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
		
	}

	@Override
	public void dropped(StaticObject rec, MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pressed(StaticObject staticObject, MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}
