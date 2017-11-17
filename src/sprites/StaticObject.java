package sprites;

import authoring.AuthorInterface;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class StaticObject extends ImageView {
	
	public StaticObject(int size, AuthorInterface author) {
		this.setFitWidth(size * 20);
		this.setFitHeight(size * 20);
		Image image = new Image(getClass().getClassLoader().getResourceAsStream("tortoise.png"));
		this.setImage(image);
		this.setEventHandler(MouseEvent.MOUSE_CLICKED, e->author.clicked(this));
	}
	
	public double getHeight() {
		return this.getFitHeight();
	}
	
	public double getWidth() {
		return this.getFitWidth();
	}
	
	public int getSize() {
		return this.getSize();
	}

}
