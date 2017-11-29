package authoring.bottomToolBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

public class DefenseLevelDisplay extends LevelDisplay{
	private Scene myScene;
	private GridPane pane;
//	private Map<String, TextBox> myTextBoxes;
	private List<TextBox> myTextBoxes;
	private ResourceBundle myResources;
	
	public DefenseLevelDisplay(int n, LevelTab lv) {
		super(n, lv);
		//this would have to get refactored out depending on different languages and all that.
		//TODO
		myResources = ResourceBundle.getBundle("authoring/resources/DefenseLevel"); //ideally this path would be to a valid resource bundle.
//		myTextBoxes = new HashMap<String, TextBox>();
		myTextBoxes = new ArrayList<>();
		createScene();
		super.getStage().setScene(myScene);	
		}

	private void createScene() {
		pane = new GridPane();
		myScene = new Scene(pane); 
		placeTextAndTextBoxes();
	}

	private void placeTextAndTextBoxes() {
		myTextBoxes.add(new WaveTextBox());
	    myTextBoxes.add(new WaveComponentsTextBox());
		for (TextBox t : myTextBoxes) {
			t.getTextField().setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent e) {
				if(e.getCode()==KeyCode.ENTER) {
					t.recordInfo();
				}
			}
		});
		}
		
	}

}
