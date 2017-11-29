package interfaces;

import sprites.InteractiveObject;

public interface Droppable {

	public void droppedInto(InteractiveObject interactive);
	
	public void objectRemoved(InteractiveObject interactive);
	
	public void freeFromDroppable(InteractiveObject interactive);
}
