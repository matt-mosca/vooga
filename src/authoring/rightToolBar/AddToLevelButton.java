package authoring.rightToolBar;

import display.interfaces.PropertiesInterface;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class AddToLevelButton extends Button {
	
	public AddToLevelButton(PropertiesInterface property) {
		this.setText("Add to Level");
		this.setLayoutX(350);
		this.setLayoutY(100);
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, e->property.addToLevel());
	}
}
