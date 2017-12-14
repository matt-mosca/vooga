package authoring.LevelToolBar;

import authoring.EditDisplay;
import engine.authoring_engine.AuthoringController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LevelsEditDisplay {
	private static final int SIZE = 800;
	
	private Stage myStage;
	private Scene myScene;
	private AuthoringController myController;
	private BorderPane myRoot;
	private ResourceDisplay resourceEditor;
	private GameEnder gameEnder;
	private EditDisplay myDisplay;
	private GameEnderRecorder recorder;
	private HealthSelector health;
	
	public LevelsEditDisplay(AuthoringController controller, EditDisplay display) {
		myController = controller;
		myStage = new Stage();
		myStage.setTitle("Level Editor");
		myRoot = new BorderPane();
		myScene = new Scene(myRoot, SIZE, SIZE);
		resourceEditor = new ResourceDisplay(controller);
		gameEnder = new GameEnder(controller, display);
		recorder = new GameEnderRecorder(controller);
		health = new HealthSelector(controller);
		gameEnder.setRecorder(recorder);
		myRoot.setBottom(health);
		myRoot.setLeft(gameEnder);
		myRoot.setRight(resourceEditor);
		myRoot.setCenter(recorder);
		myStage.setScene(myScene);
	}
	
	
	public void open() {
		myStage.show();
		resourceEditor.updateCurrentState();
		gameEnder.update();
		recorder.update();
	}
}
