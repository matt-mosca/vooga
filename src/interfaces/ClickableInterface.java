package interfaces;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import sprites.BackgroundObject;
import sprites.StaticObject;

public interface ClickableInterface{
	public void dragged(MouseEvent e);
	
	public void dropped(MouseEvent e);

	public void pressed(MouseEvent e);
	
	public double getX();
	
	public double getY();
	
	public void setX(double x);
	
	public void setY(double y);
	
	public int getSize();
	
	public Point2D center();
}
