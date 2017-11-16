package authoring;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class Cell extends StackPane{
	private boolean active = false;
	private int activeNeighbors = 0;
	
	public Cell() {
		this.addEventHandler(MouseEvent.MOUSE_ENTERED, e->highlight());
		this.addEventHandler(MouseEvent.MOUSE_EXITED, e->removeHighlight());
	}

	private void highlight() {
		if(!active && activeNeighbors>0) {
			this.setStyle("-fx-background-color:#51525D;");
		}
	}

	private void removeHighlight() {
		if(!active && activeNeighbors>0) {
			this.setStyle("-fx-background-color:#3E3F4B;");
		}
	}
	
	protected boolean pathActive() {
		return active;
	}
	
	protected boolean activeNeighbors() {
		return (activeNeighbors>0) ? true : false;
	}
	
	protected void addActive() {
		activeNeighbors++;
	}
	
	protected void removeActive() {
		activeNeighbors--;
	}
	
	protected void activate() {
		this.setStyle("-fx-background-color:#FF0033;");
		active = true;
	}
	
	protected void deactivate() {
		this.setStyle("-fx-background-color:#3E3F4B;");
		active = false;
	}
}
