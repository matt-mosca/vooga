package authoring.LevelToolBar;

import java.util.Set;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GameEnderRecorder extends VBox{
	Set<Integer >myCompletedLevels;
	
	public GameEnderRecorder(Set<Integer> levels) {
		myCompletedLevels=levels;
		update();
	}

	public void update() {
		this.getChildren().clear();
		Label completed = new Label("Levels for which you have set a victory condition:");
		this.getChildren().add(completed);
		for (int i : myCompletedLevels) {
			Label l = new Label(Integer.toString(i));
			this.getChildren().add(l);
		}
	}
}
