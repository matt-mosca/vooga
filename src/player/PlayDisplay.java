package player;

import engine.PlayModelController;

import javafx.stage.Stage;

/**
 * Main play display, uses file chooser.
 */
public class PlayDisplay extends AbstractPlayDisplay {

	public PlayDisplay(int width, int height, Stage stage, PlayModelController myController) {
		super(width, height, stage, myController);
		initializeGameState();
	}
}
