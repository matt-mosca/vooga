package authoring.bottomToolBar;

import java.util.ResourceBundle;

import engine.authoring_engine.AuthoringController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/*
 * @author venkat
 * Window that acts as a display for each individual level. - Super Class
 * Allows editing of information about the level in terms of 
 *  *the number of waves/contents of each wave--for a defensive game.
 *  *the time limit of the game--for the offensive game.
 *  
 *  Honestly, it feels very possible for us to also have the scene and the pane in this class itself, but I'm unsure
 *  Let me know. Make this abstract, maybe?
 * 
 */
public class LevelDisplay {
	private int myNumber;
	private Stage myStage;
	private LevelTab myLv;
	private AuthoringController myController;
	private ResourceDisplay resources;
	private ResourceBundle rb;
	private Scene myScene;
	private GridPane centerPane;
	private BorderPane myRoot;
	
	
	
	public LevelDisplay(int n, LevelTab lv, AuthoringController controller) {
		myRoot = new BorderPane();
		centerPane = new GridPane();
		myRoot.getChildren().add(resources.getRoot());
		myRoot.getChildren().add(centerPane);
		centerPane.setAlignment(Pos.CENTER);	
		myScene = new Scene(myRoot); 
		myNumber = n;
		myController = controller;
		myStage = new Stage();
//		myStage.setTitle(rb.getString("lvNum")+ " " + n);
		myStage.setTitle("Level Number " + n);
//		myStage.setOnCloseRequest(e->myLv.update());
		myStage.setScene(myScene);
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

	public void decrementLevel() {
		myNumber-=1;
	}

	public AuthoringController getAuthor() {
		// TODO Auto-generated method stub
		return myController;
	}
	
	
	protected GridPane getLevelPane() {
		return centerPane;
	}
	
}
