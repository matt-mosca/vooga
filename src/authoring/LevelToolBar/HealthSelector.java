package authoring.LevelToolBar;

import engine.authoring_engine.AuthoringController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class HealthSelector extends HBox{
	private final int HEALTH_DEFAULT = 100;
	
	private AuthoringController myController;

	public HealthSelector(AuthoringController controller) {
		myController = controller;
		
		TextField amount = new TextField();
		ComboBox<Integer> level = new ComboBox<>();
		for(int i = 1; i<=myController.getNumLevelsForGame(myController.getGameName(), true); i++) {
			level.getItems().add(i);
		}
		level.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
				amount.setText(Integer.toString(myController.getLevelHealth(newValue)));
			}
		});
		
		Button update = new Button();
		update.setText("Update");
		update.addEventHandler(MouseEvent.MOUSE_CLICKED, event->{
			try{
				int health = Integer.parseInt(amount.getText());
				myController.setLevelHealth(level.getSelectionModel().getSelectedItem(), health);
			}catch(NumberFormatException e) {
				myController.setLevelHealth(level.getSelectionModel().getSelectedItem(), HEALTH_DEFAULT);
			}
		});
		
		this.getChildren().add(level);
		this.getChildren().add(amount);
		this.getChildren().add(update);
	}
}
