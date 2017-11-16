package authoring;

import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import splashScreen.ScreenDisplay;

public class EditDisplay extends ScreenDisplay implements AuthorInterface {
	
	private LeftToolBar myLeftToolBar;
	private GridPane myMainGrid;
	private RightToolBar myRightToolBar;
	private Scene drawingScene;
	private Stage drawingStage;
	
	public EditDisplay(int width, int height) {
		super(width, height, Color.GREEN);
		myLeftToolBar = new LeftToolBar(this);
		rootAdd(myLeftToolBar);
		myMainGrid = new GridGameArea(this);
		rootAdd(myMainGrid);
		myRightToolBar = new RightToolBar(this);
		rootAdd(myRightToolBar);
	}

	@Override
	public void clicked(Rectangle rec) {
		// TODO Auto-generated method stub
		Rectangle currRectangle = new Rectangle(rec.getWidth(), rec.getHeight(), rec.getFill());
		currRectangle.addEventHandler(MouseEvent.MOUSE_DRAGGED, e->drag(e, currRectangle));
		currRectangle.addEventHandler(MouseEvent.MOUSE_RELEASED, e->released(currRectangle));
		rootAdd(currRectangle);
		myRightToolBar.updateInfo(Double.toString(currRectangle.getWidth()),
				Double.toString(currRectangle.getWidth()));
	}
	
	private void drag(MouseEvent e, Rectangle currRectangle) {
		currRectangle.setX(e.getSceneX() - currRectangle.getWidth() / 2);
		currRectangle.setY(e.getSceneY() - currRectangle.getHeight() / 2);
	}
	
	private void released(Rectangle currRectangle) {
		if (!currRectangle.intersects(myMainGrid.getBoundsInParent())) {
			createNewErrorWindow();
		}
		getInfo(currRectangle);
	}
	
	private void getInfo(Rectangle rec) {
//		myRightToolBar.updateInfo(rec);
		System.out.println(rec.getWidth());
		System.out.println(rec.getHeight());
		System.out.println(rec.getFill());
		System.out.println(rec.getX());
		System.out.println(rec.getY());
	}
	
	private void createNewErrorWindow() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Object placement error");
		alert.setHeaderText("Must place object in the main grid");
		alert.show();
	}

	//@Override
	public void decreaseHealth() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dropped(Rectangle rec) {
		// TODO Auto-generated method stub
		
	}
	
	

}
