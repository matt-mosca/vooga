package player;

import java.io.IOException;

import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

public class GameSlider extends Slider {
	
	public GameSlider(PlayerInterface player) {
		this.setMin(0);
		this.setMax(1);
		this.setLayoutY(600);
		this.setPrefWidth(300);
		this.setShowTickLabels(true);
		this.setShowTickMarks(true);
		this.setMajorTickUnit(200);
		this.setMinorTickCount(20);
		this.setBlockIncrement(10);
		this.addEventHandler(MouseEvent.MOUSE_RELEASED, e->player.loadSliderState((int) this.getValue()));
	}
	
	public void incrementWidth() {
		this.setMax(this.getMax() + 1);
	}
}