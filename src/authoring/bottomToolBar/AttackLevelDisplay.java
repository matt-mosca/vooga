package authoring.bottomToolBar;

import java.util.Map;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.control.TextField;

public class AttackLevelDisplay extends LevelDisplay {
	private Scene myScene;
	private Map<String, TextBox> myTextBoxes;
	public AttackLevelDisplay(int n) {
		super(n);
		createScene();
		super.getStage().setScene(myScene);
		
	}
	private void createScene() {
		Pane pane = new Pane();
		
	}

}
