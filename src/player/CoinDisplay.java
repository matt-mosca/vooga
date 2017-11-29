package player;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class CoinDisplay extends HBox {
	
	private Label myCoins;
	private Label myLabel;
	private int coins;
	
	public CoinDisplay() {
		coins = 20;
		myLabel = new Label("Current coins:");
		myCoins = new Label(Integer.toString(coins));
		myLabel.setLayoutX(0);
		myLabel.setFont(new Font(30));
		myLabel.setTextFill(Color.WHITE);
		myCoins.setLayoutX(100);
		myCoins.setFont(new Font(30));
		myCoins.setTextFill(Color.WHITE);
		this.getChildren().add(myLabel);
		this.getChildren().add(myCoins);
		this.setLayoutX(400);
//		this.getStyleClass().add("coin-display");
	}
	
	public void increment() {
		coins++;
		myCoins.setText(Integer.toString(coins));
	}
	
	public void decrease() {
		if (coins >= 5) {
			coins -= 5;
		}
		myCoins.setText(Integer.toString(coins));
	}

	public boolean decreaseByAmount(int amount) {
		if(coins >= amount)
			coins -= amount;
		return coins >= amount;
	}
	
	public void increaseByAmount(int amount) {
		coins += amount;
	}
}
