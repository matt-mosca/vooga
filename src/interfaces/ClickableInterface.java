package interfaces;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import sprites.BackgroundObject;
import sprites.StaticObject;

public interface ClickableInterface {
	
	public void clicked(StaticObject object);
	
	public void dropped(StaticObject rec, MouseEvent e);
	
//	public void dropped(BackgroundObject rec, MouseEvent e);

	public void pressed(StaticObject staticObject, MouseEvent e);
	
	

}
