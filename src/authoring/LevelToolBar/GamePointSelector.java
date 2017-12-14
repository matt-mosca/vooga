package authoring.LevelToolBar;

import java.util.ArrayList;

import engine.authoring_engine.AuthoringController;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GamePointSelector extends VBox{
	private final int POINT_DEFAULT = 100;
	private final String POINT_SELECTOR_PROMPT_TEXT = "Point Amount";
	private final String UPDATE_TEXT = "Update";
	private final String DONE_LABEL = "Are you done?";
	
	private ArrayList<CheckBox> checkBoxes;
	private AuthoringController myController;
	private TextField amount;
	private Button update;
	private Button done;
	
	public GamePointSelector(AuthoringController controller) {
		myController = controller;		
		amount = new TextField();
		amount.setPromptText(POINT_SELECTOR_PROMPT_TEXT );
		update = new Button();
		update.setText(UPDATE_TEXT);
		update.setOnAction(e ->record(amount));
		done = new Button(DONE_LABEL);
		done.setOnAction(e->hide());
		this.getChildren().add(done);
		hide();

	}
	
	
	public void show() {
		setVisible(true);
	}
	
	private void hide() {
		setVisible(false);
	}
	
	private void record(TextField amount) {
		ArrayList<Integer> selectedLevels = new ArrayList<>();
		int points;
		int currLv = myController.getCurrentLevel();
		for (int i = 0; i<checkBoxes.size(); i++) {
			if(checkBoxes.get(i).isSelected()) {
				selectedLevels.add(Integer.parseInt(checkBoxes.get(i).getText()));
				checkBoxes.get(i).fire();
			}
			else {
				myController.setLevel(Integer.parseInt(checkBoxes.get(i).getText()));
				myController.setLevelPointQuota(POINT_DEFAULT);
			}
		}
		try {
		points = Integer.parseInt(amount.getText());		
		}catch(NumberFormatException nfe) {
			Alert a = new Alert(AlertType.ERROR);
			a.setHeaderText("Input Not Valid");
			a.setContentText("Your input was invalid, so a default value has been set. If you want"
				+ " to change this, type in a number, please.");
			a.showAndWait();
			points = POINT_DEFAULT;
		}
		
		for (Integer i : selectedLevels) {
			myController.setLevel(i);
			myController.setLevelPointQuota(points);
		}
		amount.clear();
	}
	
	public void createCheckBoxes(ArrayList<Integer> lvs) {
		checkBoxes = new ArrayList<>();
		for (Integer i : lvs) {
			CheckBox c =  new CheckBox();
			c.setAllowIndeterminate(false);
			c.setText(Integer.toString(i));
			checkBoxes.add(c);
		}
		this.getChildren().clear();
		this.getChildren().addAll(checkBoxes);
		this.getChildren().addAll(amount, update, done);
		
	}

}
