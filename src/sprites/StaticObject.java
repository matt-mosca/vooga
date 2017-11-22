package sprites;

import interfaces.ClickableInterface;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

public class StaticObject extends ImageView {
	
	private static final int CELL_SIZE = 40;
	private int objectSize;
	private int realSize;
	private ClickableInterface myClickable;
	private String	 myImageString;
	
	public StaticObject(int size, ClickableInterface clickable, String imageString) {
		myClickable = clickable; 
		myImageString = imageString;
		realSize = size * CELL_SIZE;
		this.setFitWidth(realSize);
		this.setFitHeight(realSize);
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(imageString));
		this.setImage(image);
		objectSize = size;
		this.addEventHandler(MouseEvent.MOUSE_DRAGGED, e->drag(e));
		this.addEventHandler(MouseEvent.MOUSE_RELEASED, e->dropped(e));
		this.addEventHandler(MouseEvent.MOUSE_PRESSED, e->pressed(e));
		
	}
	
	private void drag(MouseEvent e) {
		this.setX(e.getX() - realSize / 2);
		this.setY(e.getY() - realSize / 2);
	}
	
	private void dropped(MouseEvent e) {
		myClickable.dropped(this, e);
	}
	
	private void pressed(MouseEvent e) {
		myClickable.pressed(this, e);
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
	
	public int getSize() {
		return objectSize;
	}

}
