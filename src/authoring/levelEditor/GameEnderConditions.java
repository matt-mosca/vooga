package authoring.levelEditor;

import java.util.ArrayList;

import engine.AuthoringModelController;
import engine.authoring_engine.AuthoringController;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GameEnderConditions extends VBox {
	private static final Object VICTORY_TIME_CONDITIONS = null;
	private AuthoringModelController myController;
	private ComboBox<String> victory;
	private ComboBox<String> defeat;
	private ArrayList<CheckBox> checkBoxes;
	private GameEnderRecorder recorder;
	private GamePointSelector pointManager;
	private final String VICTORY_PROMPT_TEXT = "Choose your victory condition!";
	private final String DEFEAT_PROMPT_TEXT = "Choose how someone loses!";
	private final String RECORD_CONDITIONS_LABEL = "Record end condtions!";
	private final String LEVELS_CONDITIONS_PROMPT_TEXT = "Select which levels you want to set these conditions for!";
	private final String VICTORY_POINTS_CONDITIONS = "points target reached";
	private final boolean IS_WRAPPED = true;
	private final boolean IS_ALLOW_INDETERMINATE = false;

	public GameEnderConditions(AuthoringModelController controller) {
		myController = controller; 
		addMiscElements();
	}
	
	private void addMiscElements() {
		victory = new ComboBox<>();
		victory.setPromptText(VICTORY_PROMPT_TEXT);
		victory.getItems().addAll(myController.getPossibleVictoryConditions());
		defeat = new ComboBox<>();
		defeat.setPromptText(DEFEAT_PROMPT_TEXT);
		defeat.getItems().addAll(myController.getPossibleDefeatConditions());
		
		Button recordConditions = new Button(RECORD_CONDITIONS_LABEL);
		recordConditions.setOnAction(e->record());
		Label l = new Label(LEVELS_CONDITIONS_PROMPT_TEXT);
		l.setWrapText(IS_WRAPPED);
		this.getChildren().addAll(victory, defeat, l);
		createLevelBoxes();
		this.getChildren().add(recordConditions);
	}
	
	private void createLevelBoxes() {
		checkBoxes = new ArrayList<>();
		for (int i = 1; i<=myController.getNumLevelsForGame(); i++) {
			System.out.println(myController.getNumLevelsForGame());
			checkBoxes.add(new CheckBox());
			checkBoxes.get(i-1).setText(Integer.toString(i));
			checkBoxes.get(i-1).setAllowIndeterminate(IS_ALLOW_INDETERMINATE);
		}
		this.getChildren().addAll(checkBoxes);
		
	}

	private void record() {
		int currLevel = myController.getCurrentLevel();
		ArrayList<Integer> selectedPointLevels = new ArrayList<>();
		for(int i = 1; i<=myController.getNumLevelsForGame(); i++) {
			myController.setLevel(i);
			if (checkBoxes.get(i-1).isSelected()) {
				if(victory.getValue()!=null) {
					myController.setVictoryCondition(victory.getValue());
				}
				if(defeat.getValue()!=null) {
					myController.setDefeatCondition(defeat.getValue());	
				}
				if(victory.getValue().equals(VICTORY_POINTS_CONDITIONS)) {
					selectedPointLevels.add(Integer.parseInt(checkBoxes.get(i-1).getText()));
				}
				if(victory.getValue().equals(VICTORY_TIME_CONDITIONS)) {
					selectedPointLevels.add(Integer.parseInt(checkBoxes.get(i-1).getText()));
				}
				checkBoxes.get(i-1).fire();
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