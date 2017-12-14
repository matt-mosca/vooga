package authoring.LevelToolBar;

import java.util.ArrayList;

import engine.authoring_engine.AuthoringController;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;

public class GameHealthSelector extends VBox{
	private final int HEALTH_DEFAULT = 100;
	private final String HEALTH_HEADER_TEXT = "Input Not Valid";
	private final String HEALTH_PROMPT_TEXT = "Health Amount";
	private final String HEALTH_UPDATE_TEXT = "Update";
	private final String HEALTH_INVALID_INPUT_WARNING = "Your input was invalid, so a default value has been set. If you want";
	private final String HEALTH_INVALID_INPUT_CORRECTION = " to change this, type in a number, please.";
	private ArrayList<CheckBox> checkBoxes;
	private AuthoringController myController;

	public GameHealthSelector(AuthoringController controller) {
		myController = controller;
		
		TextField amount = new TextField();
		amount.setPromptText(HEALTH_PROMPT_TEXT);
		Button update = new Button();
		update.setText(HEALTH_UPDATE_TEXT);
		createLevelBoxes();
		update.setOnAction(e ->{
			record(amount, checkBoxes);});
		this.getChildren().add(amount);
		this.getChildren().add(update);
		
	}

	private void record(TextField amount, ArrayList<CheckBox> levels) {
		ArrayList<Integer> selectedLevels = new ArrayList<>();
		int health;
		for (int i = 0; i<levels.size(); i++) {
			if(levels.get(i).isSelected()) {
				selectedLevels.add(i+1);
			}
		}
		try {
		health = Integer.parseInt(amount.getText());		
		}catch(NumberFormatException nfe) {
			Alert a = new Alert(AlertType.ERROR);
			a.setHeaderText(HEALTH_HEADER_TEXT);
			a.setContentText(HEALTH_INVALID_INPUT_WARNING
				+ HEALTH_INVALID_INPUT_CORRECTION);
			a.showAndWait();
			health = HEALTH_DEFAULT;
		}
		int currLv = myController.getCurrentLevel();
		for (Integer i : selectedLevels) {
			myController.setLevel(i);
			myController.setLevelHealth(health);
		}
	}
	
	private void createLevelBoxes() {
		checkBoxes = new ArrayList<>();
		for (int i = 0; i<myController.getNumLevelsForGame(); i++) {
			checkBoxes.add(new CheckBox());
			checkBoxes.get(i).setText(Integer.toString(i+1));
		}
		this.getChildren().addAll(checkBoxes);
	}
}
