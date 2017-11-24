package authoring.customize;

import interfaces.CustomizeInterface;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public class AttackDefenseToggle extends ToggleSwitch {
	private CustomizeInterface myCustomize;
	
	public AttackDefenseToggle(CustomizeInterface customize) {
		this.setLayoutY(550);
		this.setWidth(200);
		myCustomize = customize;
		this.setUpSwitch();
	}
	
	private void setUpSwitch() {
		switchedOn.addListener((a,b,c) -> {
			if (c) {
                penUp();
            }
			else {
            	penDown();
            }
		});
	}

	public void penDown() {
		switchLabel.setText("Attack");
		setStyle("-fx-background-color: red;");
		button.toFront();
		System.out.println("Attack");
//		myCustomize.changePenStatus(false);
	}

	public void penUp() {
		switchLabel.setText("Defense");
		setStyle("-fx-background-color: yellow;");
		switchLabel.toFront();
		System.out.println("Defense");
//		myCustomize.changePenStatus(true);
	}
	}
	
	
	

