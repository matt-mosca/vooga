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
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DefenseLevelDisplay extends LevelDisplay{
	
	private Button newWave;
	private ResourceBundle myResources;
	private Map<String, List<String>> defaults;
	private List<ComboBox> myDropDowns;
	
	public DefenseLevelDisplay(int n, LevelTab lv, AuthoringController myController) {
		super(n, lv, myController);
		defaults = myController.getElementBaseConfigurationOptions();
		for(String s:myController.getElementBaseConfigurationOptions().keySet()) {
			System.out.println(s);
			System.out.println(myController.getElementBaseConfigurationOptions().get(s).toString());
		}
		//this would have to get refactored out depending on different languages and all that.
		//TODO
		myResources = ResourceBundle.getBundle("authoring/resources/DefenseLevel"); //ideally this path would be to a valid resource bundle.
		createScene();
		
		
		}

	private void createScene() {
		newWave = new Button("Create new wave.");
		newWave.setOnAction(e->createNewWave());
		newWave.setLayoutX(400);
		super.getLevelPane().getChildren().add(newWave);
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
		for(String s: defaults.keySet()) {
			ComboBox x = new ComboBox();
			x.setPromptText(s);
			x.getItems().addAll(defaults.get(x));
			myDropDowns.add(x);
			super.getLevelPane().getChildren().add(x);
		}
		
		/*
		 * There are some other properties over here, I'm sure, that I need to care about, but I'm not sure what 
		 * they are.
		 */
		addWave.setOnAction(e->{
			Map<String, String> fun = new HashMap<>();
			fun.put("frequency", frequency.getText());
			fun.put("number", number.getText());
			for (int i=0; i<defaults.size(); i++) {
				fun.put((String) defaults.keySet().toArray()[i], (String) myDropDowns.get(i).getValue());
			}
			super.getAuthor().setWaveProperties(fun, Arrays.asList(order.getText().split(",")),
					new Point2D(Double.parseDouble(start.getText().split(",")[0]), Double.parseDouble(start.getText().split(",")[1])));
			
		});
		super.getLevelPane().getChildren().addAll(order, start, frequency, number, addWave);
		
		
		
	}
	
}
