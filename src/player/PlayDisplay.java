package player;

import authoring.AuthorInterface;
import authoring.LeftToolBar;
import authoring.PlacementGrid;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import splashScreen.ScreenDisplay;
import sprites.BackgroundObject;
import sprites.StaticObject;

public class PlayDisplay extends ScreenDisplay implements PlayerInterface {
	
	private GameToolBar myGameToolBar;
	private PlacementGrid myMainGrid;
	private HealthBar myHealthBar;
	private DecreaseHealthButton myDecreaseHealthButton;
	
	public PlayDisplay(int width, int height) {
		super(width, height, Color.BLUE);
//		myLeftToolBar = new LeftToolBar(this);
//		rootAdd(myLeftToolBar);
//		myMainGrid = new MainGrid(this);
//		rootAdd(myMainGrid);
		rootAdd(new HealthBackground());
		myGameToolBar = new GameToolBar(this);
		rootAdd(myGameToolBar);
		myHealthBar = new HealthBar();
		rootAdd(myHealthBar);
		myDecreaseHealthButton = new DecreaseHealthButton(this);
		rootAdd(myDecreaseHealthButton);
		
//		rootAdd(new Rectangle(400, 400, Color.WHITE));
		
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
