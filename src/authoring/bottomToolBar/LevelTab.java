package authoring.bottomToolBar;

import javafx.scene.control.Button;

public class LevelTab {
	private int myNumber;
	private Button editLevel;
	private LevelDisplay myLevelDisplay;
	
	public LevelTab(int n) {
		myNumber = n;
		myLevelDisplay = new LevelDisplay(n);
		editLevel = new Button();
		editLevel.setOnAction(e->openLevelDisplay());
	}


	private void openLevelDisplay() {
		myLevelDisplay.open();
	}
}
