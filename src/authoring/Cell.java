package authoring;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class Cell extends StackPane{
	private boolean active = false;
	
	public Cell() {
		this.addEventHandler(MouseEvent.MOUSE_ENTERED, e->highlight());
		this.addEventHandler(MouseEvent.MOUSE_EXITED, e->removeHighlight());
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, e->pathToggle());
	}

	private void pathToggle() {
		this.setStyle("-fx-background-color:#FF0033;");
		active = !active;
	}

	private void highlight() {
		if(!active) {
			this.setStyle("-fx-background-color:#51525D;");
		}
	}

	private void removeHighlight() {
		if(!active) {
			this.setStyle("-fx-background-color:#3E3F4B;");
		}
	}
}
