package sprites;

import interfaces.ClickableInterface;
import interfaces.Droppable;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import splashScreen.ScreenDisplay;

public abstract class InteractiveObject extends ImageView implements ClickableInterface{
	private boolean locked;
	private int id;
	private Droppable droppable;
	private ScreenDisplay myDisplay;
	private String elementName;
	
	//TODO set ID
	public InteractiveObject(ScreenDisplay display, String name) {
		myDisplay = display; 
		droppable = myDisplay.getDroppable();
		elementName = name;
		
		this.addEventHandler(MouseEvent.MOUSE_DRAGGED, e->dragged(e));
		this.addEventHandler(MouseEvent.MOUSE_RELEASED, e->dropped(e));
		this.addEventHandler(MouseEvent.MOUSE_PRESSED, e->pressed(e));
	}
	
	@Override
	public void dragged(MouseEvent e) {
		if(!locked) {
			this.setX(e.getX() - this.getFitWidth()/ 2);
			this.setY(e.getY() - this.getFitHeight() / 2);
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
	
	public void setLocked(boolean lock) {
		locked = lock;
	}
	
	public Point2D center() {
		return new Point2D(this.getX(), this.getY());
	}
	
	public int getElementId() {
		return id;
	}
	
	public void setElementId(int id) {
		this.id = id;
	}
	
	public String getElementName() {
		return elementName;
	}
	
	public abstract int getSize();
}
