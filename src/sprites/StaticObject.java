package sprites;

import authoring.AuthorInterface;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class StaticObject extends ImageView {
	
	private static final int CELL_SIZE = 20;
	private int objectSize;
	private int realSize;
	private AuthorInterface myAuthor;
	
	public StaticObject(int size, AuthorInterface author) {
		myAuthor = author;
		realSize = size * CELL_SIZE;
		this.setFitWidth(realSize);
		this.setFitHeight(realSize);
		Image image = new Image(getClass().getClassLoader().getResourceAsStream("tortoise.png"));
		this.setImage(image);
		objectSize = size;
		this.addEventHandler(MouseEvent.MOUSE_DRAGGED, e->drag(e));
		this.addEventHandler(MouseEvent.MOUSE_RELEASED, e->released(e));
		this.addEventHandler(MouseEvent.MOUSE_PRESSED, e->pressed(e));
	}
	
	private void drag(MouseEvent e) {
		this.setX(e.getSceneX() - realSize / 2);
		this.setY(e.getSceneY() - realSize / 2);
	}
	
	private void released(MouseEvent e) {
		myAuthor.dropped(this, e);
	}
	
	private void pressed(MouseEvent e) {
		myAuthor.pressed(this, e);
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
	
	public int getSize() {
		return objectSize;
	}

}
