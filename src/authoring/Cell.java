package authoring;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import sprites.BackgroundObject;
import sprites.StaticObject;

public class Cell extends StackPane{
	private boolean active = false;
	private List<StaticObject> myAssignments;
	private List<BackgroundObject> myBackgrounds;
	
	public Cell() {
		myAssignments = new ArrayList<StaticObject>();
		myBackgrounds = new ArrayList<BackgroundObject>();
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
	
	protected void assignToCell(StaticObject currObject) {
		if (currObject instanceof BackgroundObject) {
			myBackgrounds.add((BackgroundObject) currObject);
		} else {
			myAssignments.add(currObject);
		}
	}
	
	protected boolean isEmpty() {
		return myAssignments.isEmpty();
	}

	public void removeAssignment(StaticObject currObject) {
		if (!isEmpty()) myAssignments.remove(currObject);
	}
}
