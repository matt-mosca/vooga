package authoring.LevelToolBar;

import java.util.ArrayList;

import engine.authoring_engine.AuthoringController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class HealthSelector extends HBox{
	private final int HEALTH_DEFAULT = 100;
	private ArrayList<CheckBox> checkBoxes;
	
	private AuthoringController myController;

	public HealthSelector(AuthoringController controller) {
		myController = controller;
		
		TextField amount = new TextField();
		amount.setPromptText("Type in a number for the amount of health you want.");
		ComboBox<Integer> level = new ComboBox<>();
		level.setPromptText("Select Level");
		for(int i = 1; i<=myController.getNumLevelsForGame(myController.getGameName(), true); i++) {
			level.getItems().add(i);
		}
		level.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
				amount.setText(Integer.toString(myController.getLevelHealth(newValue)));
			}
		});
		
		Button update = new Button();
		update.setText("Update");
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
		a.setHeaderText("Input Not Valid");
		a.setContentText("Your input was invalid, so a default value has been set. If you want"
				+ "to change this, type in a number;");
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
		for (int i = 0; i<myController.getNumLevels(); i++) {
			checkBoxes.add(new CheckBox());
			checkBoxes.get(i).setText(Integer.toString(i+1));
		}
		this.getChildren().addAll(checkBoxes);
	}
}
