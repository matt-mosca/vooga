package interfaces;

import java.util.List;
import java.util.Map;

import authoring.path.Path;
import javafx.scene.paint.Color;
import sprites.InteractiveObject;

public interface Droppable {

	public void droppedInto(InteractiveObject interactive);
	
	public void objectRemoved(InteractiveObject interactive);
	
	public void freeFromDroppable(InteractiveObject interactive);
	
	public Map<Path, Color> getPaths();
}
