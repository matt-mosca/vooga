package authoring.bottomToolBar;

import javafx.scene.control.Button;

public class LevelTab {
	private int myNumber;
	private Button editLevel;
	
	
	public LevelTab(int n) {
		myNumber = n;
		LevelDisplay myLevelDisplay = new LevelDisplay();
		editLevel = new Button();
		editLevel.setOnAction(e->openLevelDisplay());
	}


	private void openLevelDisplay() {
		//get information from the backend as to 
	}
}
