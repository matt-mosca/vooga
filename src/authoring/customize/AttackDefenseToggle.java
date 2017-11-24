package authoring.customize;

import interfaces.CustomizeInterface;

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
		myCustomize.attack();
	}

	public void penUp() {
		switchLabel.setText("Defense");
		setStyle("-fx-background-color: yellow;");
		switchLabel.toFront();
		myCustomize.defense();
	}
	}
	
	
	

