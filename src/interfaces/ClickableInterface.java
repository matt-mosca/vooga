package interfaces;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public interface ClickableInterface{
	
	public void dragged(MouseEvent e);
	
	public void dropped(MouseEvent e);

	public void pressed(MouseEvent e);

}
