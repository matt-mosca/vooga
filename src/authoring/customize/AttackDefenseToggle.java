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
                attack();
            }
			else {
            	defense();
            }
		});
	}

	public void defense() {
		switchLabel.setText("Attack");
		setStyle("-fx-background-color: red;");
		button.toFront();
		myCustomize.attack();
	}

	public void attack() {
		switchLabel.setText("Defense");
//		setStyle("-fx-background-color: yellow;");
		setStyle("-fx-background-color: rgb(240,200,100);");
		switchLabel.toFront();
		myCustomize.defense();
	}
	}
	
	
	

