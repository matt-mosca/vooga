package authoring.customize;

import authoring.EditDisplay;
import interfaces.CustomizeInterface;

public class AttackDefenseToggle extends ToggleSwitch {
	private EditDisplay myDisplay;
	
	public AttackDefenseToggle(EditDisplay display) {
		this.setLayoutY(550);
		this.setWidth(200);
		myDisplay = display;
		this.setUpSwitch();
	}
	
	private void setUpSwitch() {
		switchedOn.addListener((a,b,c) -> {
			if (c) {
                attack();
            }
			else {
            	defense();
            }
		});
	}

	public void defense() {
		switchLabel.setText("Defense");
		setStyle("-fx-background-color: red;");
//		setStyle("-fx-background-color: rgb(66, 123, 230)");
		button.toFront();
		myDisplay.attack();
	}

	public void attack() {
		switchLabel.setText("Attack");
//		setStyle("-fx-background-color: yellow;");
//		setStyle("-fx-background-color: rgb(240, 200, 100);");
		setStyle("-fx-background-color: rgb(66, 123, 230);");
		switchLabel.toFront();
		myDisplay.defense();
	}
	}
	
	
	

