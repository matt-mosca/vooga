package player;

import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class WinScreen extends EndScreen {

	public WinScreen(int width, int height, Paint background, Stage currentStage) {
		super(width, height, background, currentStage);
		// TODO Auto-generated constructor stub
		addLabel("You Win!", width, height);
	}

	@Override
	protected void createButtons() {
		// TODO Auto-generated method stub
		
	}

}
