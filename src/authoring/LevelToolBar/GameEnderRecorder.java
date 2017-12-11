package authoring.LevelToolBar;

import java.util.Set;

import authoring.PropertiesToolBar.Properties;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class GameEnderRecorder extends VBox{
//	Set<Integer >myCompletedLevels;
	private TableView <Properties> VictoryConditions;
	private TableColumn<Properties, String> myVictory;
	private TableColumn <Properties, String> myDefeat;
	
	public GameEnderRecorder() {
//		myCompletedLevels=levels;
		update();
	}

	public void update() {
		
	}
}
