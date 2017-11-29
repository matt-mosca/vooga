package authoring.bottomToolBar;

import javafx.scene.Scene;
import javafx.stage.Stage;

/*
 * @author venkat
 * Window that acts as a display for each individual level. - Super Class
 * Allows editing of information about the level in terms of 
 *  *the number of waves/contents of each wave--for a defensive game.
 *  *the time limit of the game--for the offensive game.
 * 
 */
public class LevelDisplay {
	private int myNumber;
	private Stage myStage;
	private LevelTab myLv;
	
	
	
	public LevelDisplay(int n, LevelTab lv) {
		myNumber = n;
		myStage = new Stage();
		myStage.setOnCloseRequest(e->myLv.update());
	}
	
	public void open() {
		//should updating happen here or in the sub
		myStage.show();
	}

	protected Stage getStage() {
		return myStage;
	}
	
	protected int getLevelNumber() {
		return myNumber;
	}
	
}
