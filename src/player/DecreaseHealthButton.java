package player;

import authoring.AuthorInterface;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class DecreaseHealthButton extends Button {
	public DecreaseHealthButton(PlayerInterface player) {
		this.setText("Decrease Health");
		this.setLayoutX(250);
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, e->player.decreaseHealth());
	}
}
