package authoring;

import java.util.ArrayList;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import splashScreen.ScreenDisplay;

public class editDisplay extends ScreenDisplay implements AuthorInterface {
	
	private LeftToolBar myLeftToolBar;
	private Rectangle currRectangle;
	private Rectangle myMainGrid;
	private RightToolBar myRightToolBar;
	
	public editDisplay(int width, int height) {
		super(width, height, Color.GREEN);
		myLeftToolBar = new LeftToolBar(this);
		rootAdd(myLeftToolBar);
		myMainGrid = new MainGrid(this);
		rootAdd(myMainGrid);
		myRightToolBar = new RightToolBar(this);
		rootAdd(myRightToolBar);
		
	}

	@Override
	public void clicked(Rectangle rec) {
		// TODO Auto-generated method stub
		currRectangle = new Rectangle(rec.getWidth(), rec.getHeight(), rec.getFill());
		currRectangle.addEventHandler(MouseEvent.MOUSE_DRAGGED, e->drag(e));
		rootAdd(currRectangle);
	}
	
	private void drag(MouseEvent e) {
		currRectangle.setX(e.getSceneX() - currRectangle.getWidth() / 2);
		currRectangle.setY(e.getSceneY() - currRectangle.getHeight() / 2);
	}
	

}
