package authoring;

import com.sun.javafx.geom.Point2D;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import sprites.StaticObject;

public interface AuthorInterface {

	public void clicked(StaticObject object);
	
	public void dropped(StaticObject rec, MouseEvent e);

	public void pressed(StaticObject staticObject, MouseEvent e);
	
	public void newTowerSelected(ImageView myImageView);
	
}
