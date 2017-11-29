package authoring.bottomToolBar;

import javafx.scene.control.TextField;

public abstract class TextBox {
	private TextField myText;
	
	public TextBox() {
		myText = new TextField();
	}
	
	public void recordInfo() {
		/* this would be an inherited method that is overriden in each individual textbox, 
		 * allowing for the update of that particular backend element.
		 * Clearly, since this is an abstract class, this would never really be called.
		 */
		return;
	}
	
	public TextField getTextField() {
		return myText;
	}
}
