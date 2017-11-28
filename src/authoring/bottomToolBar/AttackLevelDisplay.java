package authoring.bottomToolBar;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

public class AttackLevelDisplay extends LevelDisplay {
	private Scene myScene;
	private Map<String, TextBox> myTextBoxes;
	private ResourceBundle myResources;
	public AttackLevelDisplay(int n) {
		super(n);
		//this would have to get refactored out depending on different languages and all that.
		//TODO
		myResources = ResourceBundle.getBundle("/"); //ideally this path would be to a valid resource bundle.
		myTextBoxes = new HashMap<String, TextBox>();
		createScene();
		super.getStage().setScene(myScene);
		
	}
	private void createScene() {
		GridPane pane = new GridPane();
		myScene = new Scene(pane);
	}
	
	

}
