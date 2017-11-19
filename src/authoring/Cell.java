package authoring;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class Cell extends StackPane{
	private boolean active = false;
	
	public Cell() {
		this.addEventHandler(MouseEvent.MOUSE_ENTERED, e->highlight());
		this.addEventHandler(MouseEvent.MOUSE_EXITED, e->removeHighlight());
	}

	private void highlight() {
		this.setStyle("-fx-background-color:#51525D;");
	}

	private void removeHighlight() {
		this.setStyle("-fx-background-color:transparent;");
	}
	
	protected boolean pathActive() {
		return active;
	}
	
	protected void activate() {
		active = true;
	}
	
	protected void deactivate() {
		active = false;
	}
}
