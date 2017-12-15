package player;

import engine.PlayModelController;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class TransitorySplashScreen extends BorderPane{
	private final String MESSAGE = "The next level is currently loading. Please wait.";
	
	public TransitorySplashScreen(PlayModelController controller) {
		Text loadMessage = new Text();
		loadMessage.setText(MESSAGE);
		this.setCenter(loadMessage);
	}
}
