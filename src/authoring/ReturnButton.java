package authoring;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class ReturnButton extends Button {

	public ReturnButton(AuthorInterface author) {
		this.setLayoutX(1000);
		this.setLayoutY(500);
		this.setText("Return to main menu");
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, e->author.returnButtonPressed());
	}
}
