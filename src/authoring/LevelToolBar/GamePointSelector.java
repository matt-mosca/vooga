package authoring.LevelToolBar;

import java.util.ArrayList;

import engine.authoring_engine.AuthoringController;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;

public class GamePointSelector extends HBox{
	private static final int POINT_DEFAULT = 100;
	private ArrayList<CheckBox> checkBoxes;
	private AuthoringController myController;
	
	
	
	public GamePointSelector(AuthoringController controller) {
		myController = controller;
		TextField amount = new TextField();
		amount.setPromptText("Health Amount");
		Button update = new Button();
		update.setText("Update");
		update.setOnAction(e ->record(amount));
		this.getChildren().add(amount);
		this.getChildren().add(update);
		
	}
	
	
	public void show() {
		setVisible(true);
	}
	
	public void hide() {
		setVisible(false);
	}
	
	private void record(TextField amount) {
		ArrayList<Integer> selectedLevels = new ArrayList<>();
		int points;
		
		for (int i = 0; i<checkBoxes.size(); i++) {
			if(checkBoxes.get(i).isSelected()) {
				selectedLevels.add(i+1);
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
		int currLv = myController.getCurrentLevel();
		for (Integer i : selectedLevels) {
			myController.setLevel(i);
			myController.setLevelPointQuota(points);
		}
		
	}
	
	public void createCheckBoxes(ArrayList<Integer> lvs) {
		checkBoxes = new ArrayList<>();
		for (Integer i : lvs) {
			CheckBox c =  new CheckBox();
			c.setText(Integer.toString(i+1));
			checkBoxes.add(c);
		}
		
	}

}
