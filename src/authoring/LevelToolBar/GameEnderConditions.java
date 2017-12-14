package authoring.LevelToolBar;

import java.util.ArrayList;

import engine.authoring_engine.AuthoringController;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GameEnderConditions extends VBox {
	private AuthoringController myController;
	private ComboBox<String> victory;
	private ComboBox<String> defeat;
	private ArrayList<CheckBox> checkBoxes;
	private GameEnderRecorder recorder;
	private GamePointSelector pointManager;

	public GameEnderConditions(AuthoringController controller) {
		myController = controller; 
		addMiscElements();
	}
	
	private void addMiscElements() {
		victory = new ComboBox<>();
		victory.setPromptText("Choose your victory condition!");
		victory.getItems().addAll(myController.getPossibleVictoryConditions());
		defeat = new ComboBox<>();
		defeat.setPromptText("Choose how someone loses!");
		defeat.getItems().addAll(myController.getPossibleDefeatConditions());
		
		Button recordConditions = new Button("Record end condtions!");
		recordConditions.setOnAction(e->record());
		Label l = new Label("Select which levels you want to set these conditions for!");
		l.setWrapText(true);
		this.getChildren().addAll(victory, defeat, l);
		createLevelBoxes();
		this.getChildren().add(recordConditions);
	}
	
	private void createLevelBoxes() {
		checkBoxes = new ArrayList<>();
		for (int i = 0; i<myController.getNumLevelsForGame(); i++) {
			checkBoxes.add(new CheckBox());
			checkBoxes.get(i).setText(Integer.toString(i+1));
			checkBoxes.get(i).setAllowIndeterminate(false);
		}
		this.getChildren().addAll(checkBoxes);
		
	}

	private void record() {
		int currLevel = myController.getCurrentLevel();
		ArrayList<Integer> selectedPointLevels = new ArrayList<>();
		for(int i = 0; i<myController.getNumLevelsForGame(); i++) {
			myController.setLevel(i+1);
			if (checkBoxes.get(i).isSelected()) {
				if(victory.getValue()!=null) {
					myController.setVictoryCondition(victory.getValue());
				}
				if(defeat.getValue()!=null) {
					myController.setDefeatCondition(defeat.getValue());	
				}
				if(victory.getValue().equals("points target reached")) {
					selectedPointLevels.add(Integer.parseInt(checkBoxes.get(i).getText()));
				}
				checkBoxes.get(i).fire();
		}
	
	}
		if (selectedPointLevels.size()>0) {
			pointManager.createCheckBoxes(selectedPointLevels);    
			pointManager.show();
		}
		
		victory.setValue(null);
		defeat.setValue(null);
		myController.setLevel(currLevel);
		
		recorder.update();
	}
	

	public void update() {
		this.getChildren().removeAll(checkBoxes);
		createLevelBoxes();
	}

	public void setRecorder(GameEnderRecorder r) {
		recorder = r;
		
	}

	public void setPointRecorder(GamePointSelector points) {
		pointManager = points;
	}
}