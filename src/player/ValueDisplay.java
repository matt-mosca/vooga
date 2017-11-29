package player;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ValueDisplay extends HBox {
	private static final double IMAGE_WIDTH= 40;
	private static final double IMAGE_HEIGHT= 32;
	
	private Label myValue;
	private Label myLabel;
	private ImageView myValueImage;
	private int quantity;
	
	public ValueDisplay() {
		quantity = 0;
		myValue = new Label(Integer.toString(quantity));
		setStandardDisplayValue();
		addItems();
		setStandardBoxStyle();
//		this.getStyleClass().add("coin-display");
	}
	
	private void addItems() {
//		this.getChildren().add(myLabel);
		this.getChildren().add(myValueImage);
		this.getChildren().add(myValue);
	}
	
	protected void setStandardDisplayImage(String imageName) {
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(imageName));
		myValueImage = new ImageView(image);
		myValueImage.setFitWidth(40);
		myValueImage.setFitHeight(32);
	}
	
	protected void setStandardDisplayLabel(String labelText) {
		myLabel = new Label(labelText);
//		myLabel.setLayoutX(0);
//		myLabel.setFont(new Font(30));
//		myLabel.setTextFill(Color.WHITE);
		myLabel.getStylesheets().add("player/resources/valueDisplay.css");
		myLabel.getStyleClass().add("label");
	}
	
	protected void setStandardDisplayValue() {
		myValue.getStylesheets().add("player/resources/valueDisplay.css");
		myValue.getStyleClass().add("label");
	}
	
	protected void setStandardImageViewSize() {
		myValueImage.setFitWidth(IMAGE_WIDTH);
		myValueImage.setFitHeight(IMAGE_HEIGHT);
	}
	
	private void setStandardBoxStyle() {
		this.getStylesheets().add("player/resources/valueDisplay.css");
		this.getStyleClass().add("coin-display");
	}
	
	protected void setBoxPosition(double x, double y) {
		this.setLayoutX(x);
		this.setLayoutY(y);
	}
	
	public void increment() {
		quantity++;
		myValue.setText(Integer.toString(quantity));
	}
	
	public void decrease() {
		if (quantity >= 5) {
			quantity -= 5;
		}
		myValue.setText(Integer.toString(quantity));
	}

	public boolean decreaseByAmount(int amount) {
		if(quantity >= amount)
			quantity -= amount;
		return quantity >= amount;
	}
	
	public void increaseByAmount(int amount) {
		quantity += amount;
	}
	
	protected int getQuantity() {
		return quantity;
	}
}
