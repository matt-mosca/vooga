package authoring.bottomToolBar;

import javafx.scene.control.TextField;

public class TextBox {
	private TextField myText;
	
	public TextBox() {
		myText = new TextField();
	}
	
	public void recordInfo() {
		//this would be an inherited method that is overriden in each individual textbox, allowing for the update of 
		//that particular backend element.
	}
}
