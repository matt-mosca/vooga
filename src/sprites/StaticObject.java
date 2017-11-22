package sprites;

import authoring.AuthorInterface;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class StaticObject extends ImageView {
	
	private static final int CELL_SIZE = 40;
	private int objectSize;
	private int realSize;
	private AuthorInterface myAuthor;
	private String	 myImageString;
	private boolean active;
	
	public StaticObject(int size, AuthorInterface author, String imageString) {
		myAuthor = author; 
		myImageString = imageString;
		realSize = size * CELL_SIZE;
		this.setFitWidth(realSize);
		this.setFitHeight(realSize);
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(imageString));
		this.setImage(image);
		objectSize = size;
		this.addEventHandler(MouseEvent.MOUSE_DRAGGED, e->drag(e));
		this.addEventHandler(MouseEvent.MOUSE_RELEASED, e->released(e));
		this.addEventHandler(MouseEvent.MOUSE_PRESSED, e->pressed(e));
	}
	
	private void drag(MouseEvent e) {
		this.setX(e.getX() - realSize / 2);
		this.setY(e.getY() - realSize / 2);
	}
	
	private void released(MouseEvent e) {
		myAuthor.dropped(this, e);
	}
	
	private void pressed(MouseEvent e) {
		myAuthor.pressed(this, e);
	}
	
	public void toggleActive() {
		if(!active) {
			this.setStyle("-fx-border: white, 2px;");
		}else {
			this.setStyle("-fx-border: white, 2px;");
		}
		active = !active;
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
