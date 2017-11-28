package sprites;

import java.io.File;

import interfaces.ClickableInterface;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

public class StaticObject extends ImageView {
	
	private static final int CELL_SIZE = 40;
	private Point2D objectSize;
	private Point2D realSize;
	private ClickableInterface myClickable;
	private String myImageString;
	private boolean locked;
	
	public StaticObject(Point2D size, ClickableInterface clickable, String imageString) {
		myClickable = clickable; 
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
		this.addEventHandler(MouseEvent.MOUSE_DRAGGED, e->drag(e));
		this.addEventHandler(MouseEvent.MOUSE_RELEASED, e->dropped(e));
		this.addEventHandler(MouseEvent.MOUSE_PRESSED, e->pressed(e));
		
	}

	private void setSize(Point2D size) {
		realSize = size.multiply(CELL_SIZE);
		this.setFitWidth(realSize.getX());
		this.setFitHeight(realSize.getY());
	}
	
	private void drag(MouseEvent e) {
		//TODO maybe fix??
		if(!locked) {
			this.setX(e.getX() - realSize.getX() / 2);
			this.setY(e.getY() - realSize.getY() / 2);
		}
	}
	
	private void dropped(MouseEvent e) {
		if(!locked) {
			myClickable.dropped(this, e);
		}
	}
	
	private void pressed(MouseEvent e) {
		if(!locked) {
			myClickable.pressed(this, e);
		}
	}
	
	public void setLocked(boolean lock) {
		locked = lock;
	}
	
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
	
	public Point2D getSize() {
		return objectSize;
	}
	
	public void incrementSize() {
		objectSize.add(1, 1);
		setSize(objectSize);
	}
	
	public void decrementSize() {
		if (objectSize.getX() > 1 && objectSize.getY() > 1) {
			objectSize.subtract(1,1);
			setSize(objectSize);
		}
	}

}
