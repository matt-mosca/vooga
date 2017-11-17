package authoring;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import sprites.StaticObject;

public class Cell extends StackPane{
	private boolean active = false;
	private int activeNeighbors = 0;
	private List<StaticObject> myAssignments;
	
	public Cell() {
		myAssignments = new ArrayList<StaticObject>();
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
	
	protected void assignToCell(StaticObject currObject) {
		myAssignments.add(currObject);
	}
	
	protected boolean isEmpty() {
		return myAssignments.isEmpty();
	}
}
