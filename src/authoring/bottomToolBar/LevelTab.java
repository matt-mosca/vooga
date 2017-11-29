package authoring.bottomToolBar;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;

public class LevelTab extends ScrollPane{
	
	//could/SHOULD be refactored to just hold the tab it's a part of?
	private final int DISPLAY_SIZE = 40;
	private int myNumber;
	private Button editLevel;
	private LevelDisplay myLevelDisplay;
	private boolean edited;
	
	public LevelTab(int n) {
		myNumber = n;
		//needs to be a check for what kind of level it is, so that we either create an attackleveldisplay or a 
		//defenseleveldisplay
		
		myLevelDisplay = new DefenseLevelDisplay(n, this); //for now assuming it has to be a defense one.
		Label initialLabel = new Label("You have to add content to this first! Click the edit button!");
		editLevel = new Button("Edit Level");
		editLevel.setAlignment(Pos.CENTER);
		//Need to put the button somewhere first.
		editLevel.setOnAction(e->{
			if (!edited) {
				this.getChildren().remove(initialLabel);
			}
			openLevelDisplay(); 
			edited = true;
			this.update();
			});
		this.getChildren().add(initialLabel);
		this.getChildren().add(editLevel);
	}


	public void openLevelDisplay() {
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
		//probably will involve using a lot of get methods to get stuff from the back end in order to display them.
	}


	public void decrementLevel() {
		myNumber-=1;
		myLevelDisplay.decrementLevel();
		
	}
}
