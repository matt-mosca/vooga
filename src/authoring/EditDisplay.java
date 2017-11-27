package authoring;

import java.util.ArrayList;

import com.sun.glass.events.KeyEvent;

import authoring.customize.AttackDefenseToggle;
import authoring.customize.BackgroundColorChanger;
import authoring.customize.ThemeChanger;
import authoring.leftToolBar.LeftToolBar;
import authoring.rightToolBar.RightToolBar;
import authoring.rightToolBar.SpriteImage;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
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
	private BackgroundColorChanger myColorChanger;
	private ThemeChanger myThemeChanger;
	private AttackDefenseToggle myGameChooser;
	private Label attackDefenseLabel;
	
	
	public EditDisplay(int width, int height) {
//		super(width, height, Color.GREEN);
//		super(width, height);
		super(width, height, Color.BLACK);
//		super(width, height, Color.GRAY);
		setStandardTheme();
		addItems();
		createGridToggle();
		rootAdd(gridToggle);
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
	public void clicked(SpriteImage imageView) {
		myRightToolBar.imageSelected(imageView);
	}

	@Override
	public void changeBackground(String color) {
		myMainGrid.changeColor(color);
	}
	
	@Override
	public void changeTheme(String theme) {
		if(theme.equals(ThemeChanger.FOREST))
			setForestTheme();
		else if(theme.equals(ThemeChanger.GOLD))
			setGoldTheme();
		else if(theme.equals(ThemeChanger.SKY))
			setSkyTheme();
		else if(theme.equals(ThemeChanger.DARK))
			setDarkTheme();
		else if(theme.equals(ThemeChanger.MIDNIGHT))
			setMidnightTheme();
		else if(theme.equals(ThemeChanger.STANDARD))
			setStandardTheme();
	}
	
	private void setForestTheme() {
		rootStyle("authoring/resources/green.css");
	}
	
	private void setGoldTheme() {
		rootStyle("authoring/resources/gold.css");
	}
	
	private void setSkyTheme() {
		rootStyle("authoring/resources/blue.css");
	}
	
	private void setDarkTheme() {
		rootStyle("authoring/resources/dark.css");
	}
	
	private void setMidnightTheme() {
		rootStyle("authoring/resources/darkpurple.css");
	}
	
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
