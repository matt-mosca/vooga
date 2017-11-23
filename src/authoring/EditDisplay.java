package authoring;

import java.util.ArrayList;

import com.sun.glass.events.KeyEvent;

import authoring.rightToolBar.RightToolBar;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import splashScreen.ScreenDisplay;
import sprites.BackgroundObject;
import sprites.StaticObject;

public class EditDisplay extends ScreenDisplay implements AuthorInterface {
	
	private static final double GRID_X_LOCATION = 605;
	private static final double GRID_Y_LOCATION = 30;
	private LeftToolBar myLeftToolBar;
	private GameArea myMainGrid;
	private ScrollableArea myGameEnvironment;
	private RightToolBar myRightToolBar;
	private CheckBox gridToggle;
	
	
	public EditDisplay(int width, int height) {
		super(width, height, Color.GREEN);
		addItems();
		createGridToggle();
		rootAdd(gridToggle);
	}

	private void createGridToggle() {
		gridToggle = new CheckBox();
		gridToggle.setLayoutX(GRID_X_LOCATION);
		gridToggle.setLayoutY(GRID_Y_LOCATION);
		gridToggle.setSelected(true);
		gridToggle.setText("Grid");
		gridToggle.setTextFill(Color.BLACK);
		gridToggle.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
			myMainGrid.toggleGridVisibility(gridToggle.isSelected());
		});
	}

	private void addItems() {
		myLeftToolBar = new LeftToolBar(this);
		rootAdd(myLeftToolBar);
		myMainGrid = new GameArea(this);
		myGameEnvironment = new ScrollableArea(myMainGrid);
		rootAdd(myGameEnvironment);
		myRightToolBar = new RightToolBar(this);
		rootAdd(myRightToolBar);
	}
	
	@Override 
	public void clicked(StaticObject object) {
		createOptionButtons(object);
	}
	
	private void createOptionButtons(StaticObject object) {
		Button addNewButton = new Button("New");
		Button incrementButton = new Button("+");
		Button decrementButton = new Button("-");
		incrementButton.setLayoutX(50);
		decrementButton.setLayoutX(85);
		addNewButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->addObject(object));
		incrementButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->object.incrementSize());
		decrementButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->object.decrementSize());
		rootAdd(addNewButton);
		rootAdd(incrementButton);
		rootAdd(decrementButton);
	}

	private void addObject(StaticObject object) {
		StaticObject newObject;
		if (object instanceof BackgroundObject) {
			newObject = new BackgroundObject(object.getSize(), this, object.getImageString());
		} else {
			newObject = new StaticObject(object.getSize(), this, object.getImageString());
		}
		myMainGrid.getChildren().add(newObject);
	}
	
	private void createNewErrorWindow() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Object placement error");
		alert.setHeaderText("Must place object in the main grid");
		alert.show();
	}

	@Override
	public void dropped(StaticObject currObject, MouseEvent e) {
		if(e.getButton() == MouseButton.SECONDARY) {
			deleteObject(currObject);
		} else {
			myMainGrid.placeInGrid(currObject);
			myGameEnvironment.requestFocus();
		}
	}

	@Override
	public void pressed(StaticObject currObject, MouseEvent e) {
		e.consume();
		myMainGrid.removeFromGrid(currObject);
	}
	
	private void deleteObject(StaticObject object) {
		myMainGrid.getChildren().remove(object);
		myLeftToolBar.requestFocus();
		myMainGrid.removeFromGrid(object);
	}

	@Override
	public void newTowerSelected(ImageView myImageView) {
		
	}

	@Override
	public void clicked(ImageView imageView) {
		myRightToolBar.imageSelected(imageView.getImage());
	}
}
