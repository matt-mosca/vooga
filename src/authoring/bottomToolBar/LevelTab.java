package authoring.bottomToolBar;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;

public class LevelTab extends ScrollPane{
	private final int DISPLAY_SIZE = 40;
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
	
	public void attach(Tab level) {
		level.setContent(this);
	}

	public int getLvNumber() {
		return myNumber;
	}

	public void update() {
		//TODO
		//this method will update the different components in this tab, for the display. 	
		//probably will involve using a lot of get methods to get stuff from teh back end in order to display them.
	}


	public void decrementLevel() {
		myNumber-=1;
		myLevelDisplay.decrementLevel();
		
	}
}
