package authoring.bottomToolBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import engine.authoring_engine.AuthoringController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DefenseLevelDisplay extends LevelDisplay{
	
//	private Map<String, TextBox> myTextBoxes;
	private List<TextBox> myTextBoxes;
	private Button newWave;
	private ResourceBundle myResources;
	
	public DefenseLevelDisplay(int n, LevelTab lv, AuthoringController myController) {
		super(n, lv, myController);
		//this would have to get refactored out depending on different languages and all that.
		//TODO
		myResources = ResourceBundle.getBundle("authoring/resources/DefenseLevel"); //ideally this path would be to a valid resource bundle.
//		myTextBoxes = new HashMap<String, TextBox>();
		myTextBoxes = new ArrayList<>();
		createScene();
		
		}

	private void createScene() {
		newWave = new Button("Create new wave.");
		newWave.setOnAction(e->createNewWave());
		super.getLevelPane().getChildren().add(newWave);
//		createTextBoxes();
//		placeTextAndTextFields();
	}

	private void createNewWave() {
		super.getLevelPane().getChildren().clear();
		TextField order = new TextField();
		order.setPromptText("Troops in this wave, separated by commas.");
		TextField start = new TextField();
		start.setPromptText("Starting point of this wave");
		TextField frequency = new TextField();
		frequency.setPromptText("How often do you want this wave to spawn?");
		TextField number = new TextField();
		number.setPromptText("How many times do you want this wave to spawn?");
		Button addWave = new Button("Add this wave!");
		/*
		 * There are some other properties over here, I'm sure, that I need to care about, but I'm not sure what 
		 * they are.
		 */
		addWave.setOnAction(e->{
			Map<String, String> fun = new HashMap<>();
			fun.put("frequency", frequency.getText());
			fun.put("number", number.getText());
			super.getAuthor().setWaveProperties(fun, Arrays.asList(order.getText().split(",")),
					new Point2D(Double.parseDouble(start.getText().split(",")[0]), Double.parseDouble(start.getText().split(",")[1])));
			
		});
		super.getLevelPane().getChildren().addAll(order, start, frequency, number, addWave);
		
		
		
	}

//	private void createTextBoxes() {
//		myTextBoxes.add(new WaveTextBox(myResources.getString("waves")));
//	    myTextBoxes.add(new WaveComponentsTextBox(myResources.getString("order")));
//	    myTextBoxes.add(new GameDurationTextBox(myResources.getString("duration")));
//		for (TextBox t : myTextBoxes) {
//			t.getTextField().setOnKeyPressed(new EventHandler<KeyEvent>() {
//
//			@Override
//			public void handle(KeyEvent e) {
//				if(e.getCode()==KeyCode.ENTER) {
//					t.recordInfo();
//				}
//			}
//		});
//		}
//		
//	}

	
//	private void placeTextAndTextFields() {
//		int height = 30;
//		for (TextBox t: myTextBoxes) {
//			super.getLevelPane().add(new Label(t.getString()), 10, height);
//			super.getLevelPane().add(t.getTextField(), 50, height);
//			height+=10;
//		}
//	}
	
	

}
