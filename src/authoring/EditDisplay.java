package authoring;

import authoring.customize.AttackDefenseToggle;
import authoring.customize.BackgroundColorChanger;
import authoring.leftToolBar.LeftToolBar;
import authoring.rightToolBar.RightToolBar;
import authoring.rightToolBar.SpriteImage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import splashScreen.ScreenDisplay;
import sprites.BackgroundObject;
import sprites.StaticObject;

public class EditDisplay extends ScreenDisplay implements AuthorInterface {
	
	private static final double GRID_X_LOCATION = 620;
	private static final double GRID_Y_LOCATION = 20;
	private LeftToolBar myLeftToolBar;
	private GameArea myGameArea;
	private ScrollableArea myGameEnvironment;
	private RightToolBar myRightToolBar;
	private ToggleButton gridToggle;
	private ToggleButton movementToggle;
	private BackgroundColorChanger myColorChanger;
	private AttackDefenseToggle myGameChooser;
	private Label attackDefenseLabel;
	
	
	public EditDisplay(int width, int height) {
		super(width, height, Color.GREEN);
//		super(width, height, Color.GRAY);
		addItems();
		createGridToggle();
		rootAdd(gridToggle);
		createMovementToggle();
		rootAdd(movementToggle);
		createLabel();
//		setGreen();
//		setGold();
	}
//	
//	private void setGreen() {
//		rootStyle("authoring/resources/green.css");
//	}
//	
//	private void setGold() {
//		rootStyle("authoring/resources/gold.css");
//	}
	
	private void createLabel() {
		attackDefenseLabel = new Label("Attack");
		attackDefenseLabel.setFont(new Font("Arial", 40));
//		attackDefenseLabel.setFont(new Font("American Typewriter", 40));
//		attackDefenseLabel.setFont(new Font("Cambria", 40));
		attackDefenseLabel.setLayoutX(300);
		rootAdd(attackDefenseLabel);

	}

	private void createGridToggle() {
		gridToggle = new ToggleButton();
		gridToggle.setLayoutX(GRID_X_LOCATION);
		gridToggle.setLayoutY(GRID_Y_LOCATION);
		gridToggle.setSelected(true);
		gridToggle.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("grid_icon.png"))));
		gridToggle.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
			myGameArea.toggleGridVisibility(gridToggle.isSelected());
		});
	}
	
	private void createMovementToggle() {
		movementToggle = new ToggleButton();
		movementToggle.setLayoutX(GRID_X_LOCATION - 40);
		movementToggle.setLayoutY(GRID_Y_LOCATION);
		movementToggle.setSelected(true);
		movementToggle.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("scroll_arrow_icon.png"))));
		movementToggle.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
			myGameArea.toggleMovement(movementToggle.isSelected());
		});
	}

	private void addItems() {
		myLeftToolBar = new LeftToolBar(this);
		rootAdd(myLeftToolBar);
		myGameArea = new GameArea(this);
		myGameEnvironment = new ScrollableArea(myGameArea);
		rootAdd(myGameEnvironment);
		myRightToolBar = new RightToolBar(this);
		rootAdd(myRightToolBar);
		myColorChanger = new BackgroundColorChanger(this);
		rootAdd(myColorChanger);
		myGameChooser = new AttackDefenseToggle(this);
		rootAdd(myGameChooser);
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
		myGameArea.addBackObject(newObject);
	}
	
	//No longer needed with children added to game area
//	private void createNewErrorWindow() {
//		Alert alert = new Alert(AlertType.INFORMATION);
//		alert.setTitle("Object placement error");
//		alert.setHeaderText("Must place object in the main grid");
//		alert.show();
//	}

	@Override
	public void dropped(StaticObject currObject, MouseEvent e) {
		if(e.getButton() == MouseButton.SECONDARY) {
			deleteObject(currObject);
		} else {
			myGameArea.placeInGrid(currObject);
			myGameEnvironment.requestFocus();
		}
	}

	@Override
	public void pressed(StaticObject currObject, MouseEvent e) {
		e.consume();
		myGameArea.removeFromGrid(currObject);
	}
	
	private void deleteObject(StaticObject object) {
		myGameArea.removeObject(object);
		myLeftToolBar.requestFocus();
		myGameArea.removeFromGrid(object);
	}

	@Override
	public void newTowerSelected(ImageView myImageView) {
		
	}

	@Override
	public void clicked(SpriteImage imageView) {
		myRightToolBar.imageSelected(imageView);
	}

	@Override
	public void changeBackground(String color) {
		myGameArea.changeBackground(color);
		
	}

	@Override
	public void attack() {
		attackDefenseLabel.setText("Attack");
	}

	@Override
	public void defense() {
		attackDefenseLabel.setText("Defense");

		
	}

	@Override
	public void doSomething() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] getInfo() {
		// TODO Auto-generated method stub
		return null;
	}
}
