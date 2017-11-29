package authoring.bottomToolBar;

import javafx.scene.control.Button;

public class LevelTab {
	private int myNumber;
	private Button editLevel;
	private LevelDisplay myLevelDisplay;
	
	public LevelTab(int n) {
		myNumber = n;
		//needs to be a check for what kind of level it is, so that we either create an attackleveldisplay or a 
		//defenseleveldisplay
		myLevelDisplay = new LevelDisplay(n, this);
		editLevel = new Button();
		editLevel.setOnAction(e->openLevelDisplay());
	}


	private void openLevelDisplay() {
		myLevelDisplay.open();
	}


	public void update() {
		//TODO
		//this method will update the different components in this tab, for the display. 	
	}
}
