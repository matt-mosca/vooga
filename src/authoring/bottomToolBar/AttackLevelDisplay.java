package authoring.bottomToolBar;

import javafx.scene.Scene;

public class AttackLevelDisplay extends LevelDisplay {
	private Scene myScene;
	public AttackLevelDisplay(int n) {
		super(n);
		createScene();
		super.getStage().setScene(myScene);
		
	}
	private void createScene() {
		// TODO Auto-generated method stub
		
	}

}
