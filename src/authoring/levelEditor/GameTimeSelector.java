package authoring.levelEditor;

import java.util.ArrayList;

import display.splashScreen.ScreenDisplay;
import engine.AuthoringModelController;
import factory.AlertFactory;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;

public class GameTimeSelector  extends VBox{
	private static final String TIME_SELECTOR_PROMPT_TEXT = "Enter a time limit!";
	private static final String UPDATE_TEXT = "Update";
	private static final String DONE_LABEL = "Are you done?";
	private static final String TIME_ALERT_MESSAGE = "Your input was invalid, so a default value has been set. If you want"
			+ " to change this, type in a number, please.";
	private final String TIME_ALERT_HEADER = "Input Not Valid";
	private static final int TIME_DEFAULT = 60;
	private ArrayList<CheckBox> checkBoxes;
	private AuthoringModelController myController;
	private TextField amount;
	private Button update;
	private Button done;

	public GameTimeSelector(AuthoringModelController controller) {
		myController = controller;		
		amount = new TextField();
		amount.setPromptText(TIME_SELECTOR_PROMPT_TEXT );
		update = new Button();
		update.setText(UPDATE_TEXT);
		update.setOnAction(e ->record(amount));
		done = new Button(DONE_LABEL);
		done.setOnAction(e->hide());
		this.getChildren().add(done);
		hide();
	}

	private void hide() {
		setVisible(false);
	}
	
	void show() {
		setVisible(true);
	}

	private void record(TextField amount) {
		ArrayList<Integer> selectedLevels = new ArrayList<>();
		int time;
		int currLv = myController.getCurrentLevel();
		for (int i = 0; i<checkBoxes.size(); i++) {
			if(checkBoxes.get(i).isSelected()) {
				selectedLevels.add(Integer.parseInt(checkBoxes.get(i).getText()));
				checkBoxes.get(i).fire();
			}
			else {
				myController.setLevel(Integer.parseInt(checkBoxes.get(i).getText()));
				myController.setLevelPointQuota(TIME_DEFAULT);
			}
		}
		try {
		time = Integer.parseInt(amount.getText());		
		}catch(NumberFormatException nfe) {
			new AlertFactory(TIME_ALERT_MESSAGE,TIME_ALERT_HEADER,AlertType.ERROR);
			time = TIME_DEFAULT;
		}
		
		for (Integer i : selectedLevels) {
			myController.setLevel(i);
			myController.setLevelTimeLimit(time* (int) ScreenDisplay.FRAMES_PER_SECOND);;
		}
		amount.clear();
		myController.setLevel(currLv);
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
