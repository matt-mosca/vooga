package authoring;

import authoring.customize.AttackDefenseToggle;
import authoring.customize.BackgroundColorChanger;
import authoring.customize.ThemeChanger;
import authoring.leftToolBar.LeftToolBar;
import authoring.rightToolBar.RightToolBar;
import authoring.rightToolBar.SpriteImage;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
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
	private ThemeChanger myThemeChanger;
	private AttackDefenseToggle myGameChooser;
	private Label attackDefenseLabel;
	private Button yesButton;
	private Button noButton;
	private Label optionLabel;
	
	
	public EditDisplay(int width, int height) {
//		super(width, height, Color.GREEN);
//		super(width, height);
		super(width, height, Color.BLACK);
//		super(width, height, Color.GRAY);
		setStandardTheme();
		addItems();
		createGridToggle();
		rootAdd(gridToggle);
		createMovementToggle();
		rootAdd(movementToggle);
		createLabel();
	}
	
	private void createLabel() {
		attackDefenseLabel = new Label("Attack");
//		styleLabel(attackDefenseLabel);
		attackDefenseLabel.setFont(new Font("Times New Roman", 35));
//		attackDefenseLabel.setFont(new Font("American Typewriter", 40));
//		attackDefenseLabel.setFont(new Font("Cambria", 40));
		attackDefenseLabel.setLayoutX(260);
		attackDefenseLabel.setLayoutY(25);
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
		movementToggle.setSelected(false);
		movementToggle.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("scroll_arrow_icon.png"))));
		movementToggle.addEventHandler(MouseEvent.MOUSE_CLICKED, e->toggleMovement(movementToggle));
	}
	
	private void toggleMovement(ToggleButton movement) {
		myGameArea.toggleMovement(movementToggle.isSelected());
		if(movement.isSelected()) {
			this.getScene().setCursor(new ImageCursor(new Image(getClass().getClassLoader().getResourceAsStream("scroll_arrow_icon.png"))));
		}else {
			this.getScene().setCursor(Cursor.DEFAULT);
		}
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
		myThemeChanger = new ThemeChanger(this);
		rootAdd(myThemeChanger);
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
		noButtonPressed(imageView);
		optionLabel = new Label("Do you want to add this sprite to inventory?");
		yesButton = new Button("Yes");
		noButton = new Button("No");
		yesButton.setLayoutX(1000);
		noButton.setLayoutX(1050);
		optionLabel.setLayoutX(700);
		rootAdd(yesButton);
		rootAdd(optionLabel);
		rootAdd(noButton);
		yesButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->myRightToolBar.imageSelected(imageView));
		noButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->noButtonPressed(imageView));
	}
	
	private void noButtonPressed(SpriteImage imageView) {
		rootRemove(yesButton);
		rootRemove(noButton);
		rootRemove(optionLabel);

	}

	@Override
	public void changeBackground(String color) {
		myGameArea.changeBackground(color);
	}
	
	@Override
	public void changeTheme(String theme) {
//		if(theme.equals(ThemeChanger.FOREST))
//			setForestTheme();
//		else if(theme.equals(ThemeChanger.GOLD))
//			setGoldTheme();
//		else if(theme.equals(ThemeChanger.SKY))
//			setSkyTheme();
//		else if(theme.equals(ThemeChanger.DARK))
//			setDarkTheme();
//		else if(theme.equals(ThemeChanger.MIDNIGHT))
//			setMidnightTheme();
//		else if(theme.equals(ThemeChanger.STANDARD))
//			setStandardTheme();
		rootStyle(myThemeChanger.getThemePath(theme));
	}
	
//	private void setForestTheme() {
//		rootStyle("authoring/resources/green.css");
//	}
//	
//	private void setGoldTheme() {
//		rootStyle("authoring/resources/gold.css");
//	}
//	
//	private void setSkyTheme() {
//		rootStyle("authoring/resources/blue.css");
//	}
//	
//	private void setDarkTheme() {
//		rootStyle("authoring/resources/dark.css");
//	}
//	
//	private void setMidnightTheme() {
//		rootStyle("authoring/resources/darkpurple.css");
//	}
//	
	private void setStandardTheme() {
		rootStyle("authoring/resources/standard.css");
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
	
//	private void styleLabel(Label l) {
////		l.setStyle("-fx-background-color: aliceblue; -fx-text-fill: white;");
//		l.setFont(new Font("Arial", 40));
//		l.setTextFill(Color.WHITE);
//		l.setStyle("-fx-stroke: black; -fx-stroke-width: 1;");
//	}
}
