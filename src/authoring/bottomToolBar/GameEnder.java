package authoring.bottomToolBar;

import java.util.ArrayList;
import java.util.Collection;

import engine.authoring_engine.AuthoringController;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

public class GameEnder extends VBox{
	private AuthoringController myController;
	private ComboBox<String> victory;
	private ComboBox<String> defeat;
	
	
	public GameEnder(AuthoringController controller) {
		myController = controller; 
		victory = new ComboBox<>();
		victory.setPromptText("Choose your victory condition!");
		victory.getItems().addAll(myController.getPossibleVictoryConditions());
		defeat = new ComboBox<>();
		defeat.setPromptText("Choose how someone loses!");
		defeat.getItems().addAll(myController.getPossibleDefeatConditions());
		Button recordConditions = new Button("Record end condtion!");
		recordConditions.setOnAction(e->record());
		this.getChildren().addAll(victory, defeat, recordConditions);
		}
	
	private void record() {
		myController.setVictoryCondition(victory.getValue());
		myController.setDefeatCondition(defeat.getValue());
	}
	
}
