package player;

import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SmartPlayDisplay extends PlayDisplay {
	
	public SmartPlayDisplay(int width, int height, Stage stage) {
		super(width, height, stage);
		createSlider();
	}

	private void createSlider() {
		Slider slider = new Slider();
		slider.setMin(0);
		slider.setMax(500);
		slider.setValue(0);
		slider.setPrefWidth(300);
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		slider.setMajorTickUnit(50);
		slider.setMinorTickCount(5);
		slider.setBlockIncrement(10);
		rootAdd(slider);
		slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
            		try {
						reloadGame();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            		for (int i = 0; i < new_val.intValue(); i++) {
            			step();
            		}
            }
        });
	}

}
