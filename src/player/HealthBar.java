package player;

import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class HealthBar extends Rectangle {
	
	private Rectangle healthBackground;
	private Rectangle healthBar;
	private double myHealth;
	
	public HealthBar() {
		myHealth = 200;
		this.setHeight(40);
		this.setWidth(200);
		this.setFill(Color.LIGHTGREEN);
	}

	public void decreaseHealth(double amount) {
		myHealth -= amount;
		this.setWidth(myHealth);
		
	}

}
