package authoring.LevelToolBar;





import java.util.Map;

import engine.authoring_engine.AuthoringController;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;

public class ResourceTab extends ScrollPane{
	private AuthoringController myController;
	private int myLv;
	private ResourceTable resourceTable;
	
	public ResourceTab(int i, AuthoringController controller) {
		myController = controller;
		myLv = i;
	}
	
	public void attach(Tab level) {
		level.setContent(this);
	}
	
	public void update() {
		int curr =myController.getCurrentLevel();
		myController.setLevel(myLv);
		resourceTable = new ResourceTable(myController, myLv);
		
	}
	
}
