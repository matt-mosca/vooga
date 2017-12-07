package display.interfaces;

<<<<<<< HEAD:src/interfaces/Droppable.java
import java.util.List;
import java.util.Map;

import authoring.path.Path;
import javafx.scene.paint.Color;
import sprites.InteractiveObject;
=======
import display.sprites.InteractiveObject;
>>>>>>> bas65:src/display/interfaces/Droppable.java

public interface Droppable {

	public void droppedInto(InteractiveObject interactive);
	
	public void objectRemoved(InteractiveObject interactive);
	
	public void freeFromDroppable(InteractiveObject interactive);
	
	public Map<Path, Color> getPaths();
}
