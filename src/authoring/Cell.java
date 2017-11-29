package authoring;

import java.util.ArrayList;
import java.util.List;

import interfaces.ClickableInterface;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import sprites.BackgroundObject;
import sprites.StaticObject;

public class Cell extends StackPane{
	private boolean active = false;
	private List<ClickableInterface> myAssignments;
	private List<ClickableInterface> myBackgrounds;
	
	public Cell() {
		myAssignments = new ArrayList<>();
		myBackgrounds = new ArrayList<>();
		this.addEventHandler(MouseEvent.MOUSE_ENTERED, e->highlight());
		this.addEventHandler(MouseEvent.MOUSE_EXITED, e->removeHighlight());
	}

	private void highlight() {
		this.setStyle("-fx-border-color:black;");
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
	
	protected void assignToCell(ClickableInterface currObject) {
		if (currObject instanceof BackgroundObject) {
			myBackgrounds.add((BackgroundObject) currObject);
		} else {
			myAssignments.add(currObject);
		}
	}
	
	protected boolean isEmpty() {
		return myAssignments.isEmpty();
	}

	public void removeAssignment(ClickableInterface currObject) {
		if (!isEmpty()) myAssignments.remove(currObject);
	}
	
	public List<ClickableInterface> saveAssignments() {
		return myAssignments;
	}
}
