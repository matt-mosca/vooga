package sprites;

import java.io.File;

import interfaces.ClickableInterface;
import interfaces.Droppable;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import splashScreen.ScreenDisplay;

public class StaticObject extends ImageView implements ClickableInterface{
	
	private static final int CELL_SIZE = 40;
	private int objectSize;
	private int realSize;
	private ScreenDisplay myDisplay;
	private Droppable droppable;
	private String myImageString;
	private boolean locked;
	
	public StaticObject(int size, ScreenDisplay display, String imageString) {
		myDisplay = display; 
		droppable = myDisplay.getDroppable();
		myImageString = imageString;
		setSize(size);
		Image image;
		try {
			image = new Image(getClass().getClassLoader().getResourceAsStream(imageString));
		}catch(NullPointerException e) {
			image = new Image(imageString);
		}
		this.setImage(image);
		objectSize = size;
		this.addEventHandler(MouseEvent.MOUSE_DRAGGED, e->dragged(e));
		this.addEventHandler(MouseEvent.MOUSE_RELEASED, e->dropped(e));
		this.addEventHandler(MouseEvent.MOUSE_PRESSED, e->pressed(e));
		
	}

	private void setSize(int size) {
		realSize = size * CELL_SIZE;
		this.setFitWidth(realSize);
		this.setFitHeight(realSize);
	}
	
	public void setLocked(boolean lock) {
		locked = lock;
	}
	
	@Override
	public Point2D center() {
		return new Point2D(this.getX(), this.getY());
	}
	
	public double getHeight() {
		return this.getFitHeight();
	}
	
	public double getWidth() {
		return this.getFitWidth();
	}
	
	public String getImageString() {
		return myImageString;
	}
	
	public int getSize() {
		return objectSize;
	}
	
	public void incrementSize() {
		objectSize++;
		setSize(objectSize);
	}
	
	public void decrementSize() {
		if (objectSize > 1) {
			objectSize--;
			setSize(objectSize);
		}
	}
	
	@Override
	public void dragged(MouseEvent e) {
		if(!locked) {
			this.setX(e.getX() - realSize / 2);
			this.setY(e.getY() - realSize / 2);
		}
	}

	@Override
	public void dropped(MouseEvent e) {
		if(!locked) {
			droppable.droppedInto(this);
		}
	}

	@Override
	public void pressed(MouseEvent e) {
		if(!locked && droppable != null) {
			e.consume();
			if(e.getButton() == MouseButton.SECONDARY) {
				droppable.objectRemoved(this);
			}
			droppable.freeFromDroppable(this);
		}
	}
}
