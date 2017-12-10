package authoring.LevelToolBar;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import engine.authoring_engine.AuthoringController;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

public class AttackLevelDisplay extends LevelDisplay {
	private Scene myScene;
	private Map<String, TextBox> myTextBoxes;
	private ResourceBundle myResources;
	public AttackLevelDisplay(int n, LevelTab lv, AuthoringController controller) {
		super(n, lv, controller);
		//this would have to get refactored out depending on different languages and all that.
		//TODO
		myResources = ResourceBundle.getBundle("authoring/resources/AttackLevel"); //ideally this path would be to a valid resource bundle.
		myTextBoxes = new HashMap<String, TextBox>();
		createScene();
		super.getStage().setScene(myScene);
		
	}
	private void createScene() {
		GridPane pane = new GridPane();
		myScene = new Scene(pane);
		
	}
	
	

}
