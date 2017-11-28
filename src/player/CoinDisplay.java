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
		coins = 0;
		myLabel = new Label("Current coins:");
		myCoins = new Label("0");
		myLabel.setLayoutX(0);
		myLabel.setFont(new Font(30));
		myLabel.setTextFill(Color.WHITE);
		myCoins.setLayoutX(100);
		myCoins.setFont(new Font(30));
		myCoins.setTextFill(Color.WHITE);
		this.getChildren().add(myLabel);
		this.getChildren().add(myCoins);
		this.setLayoutX(400);
	}
	
	public void increment() {
		coins++;
		myCoins.setText(Integer.toString(coins));
	}
	
	public void decrease(int number) {
		coins -= number;
		myCoins.setText(Integer.toString(coins));
	}

}
