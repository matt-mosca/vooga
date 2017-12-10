package authoring.LevelToolBar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import authoring.EditDisplay;
import engine.authoring_engine.AuthoringController;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GameEnder extends VBox{
	private AuthoringController myController;
	private ComboBox<String> victory;
	private ComboBox<String> defeat;
	private EditDisplay myDisplay;
	private ArrayList<CheckBox> checkBoxes;
	private HashSet<Integer> selectedLevels;
	
	
	public GameEnder(AuthoringController controller, EditDisplay edit) {
		myController = controller; 
		this.setPrefWidth(300);
		myDisplay = edit;
		selectedLevels = new HashSet<Integer>();
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
		for (int i = 0; i<myDisplay.getMaxLevel(); i++) {
			checkBoxes.add(new CheckBox());
			checkBoxes.get(i).setText(Integer.toString(i+1));
		}
		this.getChildren().addAll(checkBoxes);
		
	}

	private void record() {
		int currLevel = myController.getCurrentLevel();
		for(int i = 0; i<myDisplay.getMaxLevel(); i++) {
			myController.setLevel(i+1);
			if (checkBoxes.get(i).isSelected()) {
			myController.setVictoryCondition(victory.getValue());
			myController.setDefeatCondition(defeat.getValue());	
			selectedLevels.add(i);
		}
	}
		myController.setLevel(currLevel);
		this.getChildren().clear();
		Label completed = new Label("You have added your win and loss conditions!"
				+ " Remember that every level must have a condition.");
		completed.setWrapText(true);
		this.getChildren().add(completed);
		addMiscElements();
	}
	
	public Set<Integer>getSelectedLevels() {
		return selectedLevels;
	}
	
}
