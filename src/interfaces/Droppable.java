package interfaces;

public interface Droppable {

	public void droppedInto(ClickableInterface clickable);
	
	public void objectRemoved(ClickableInterface clickable);
	
	public void freeFromDroppable(ClickableInterface clickable);
}
