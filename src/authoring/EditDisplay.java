package authoring;

import java.util.HashMap;
import java.util.Map;


import authoring.bottomToolBar.BottomToolBar;
import authoring.customize.AttackDefenseToggle;
import authoring.customize.ColorChanger;
import authoring.customize.ThemeChanger;
import authoring.leftToolBar.LeftToolBar;
import authoring.rightToolBar.RightToolBar;
import authoring.rightToolBar.SpriteImage;
import engine.authoring_engine.AuthoringController;
import interfaces.ClickableInterface;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import main.Main;
import splashScreen.ScreenDisplay;
import sprites.BackgroundObject;
import sprites.InteractiveObject;
import sprites.StaticObject;

public class EditDisplay extends ScreenDisplay implements AuthorInterface {
	
	private static final double GRID_X_LOCATION = 620;
	private static final double GRID_Y_LOCATION = 20;
	private AuthoringController controller;
	private LeftToolBar myLeftToolBar;
	private GameArea myGameArea;
	private ScrollableArea myGameEnvironment;
	private RightToolBar myRightToolBar;
	private MainMenuBar myMenuBar;
	private ToggleButton gridToggle;
	private ToggleButton movementToggle;
	private ColorChanger myColorChanger;
	private ThemeChanger myThemeChanger;
	private AttackDefenseToggle myGameChooser;
	private Label attackDefenseLabel;
	private  ReturnButton myReturnButton;
	private Map<String, String> basePropertyMap;
	private BottomToolBar myBottomToolBar;

	
	
	public EditDisplay(int width, int height) {
//		super(width, height, Color.GREEN);
//		super(width, height);
		super(width, height, Color.BLACK);
//		super(width, height, Color.GRAY);
		myReturnButton = new ReturnButton(this);
		rootAdd(myReturnButton);
		addItems();
		setStandardTheme();
		createGridToggle();
		rootAdd(gridToggle);
		createMovementToggle();
		rootAdd(movementToggle);
		createLabel();
		basePropertyMap = new HashMap<String, String>();
	}
	
	private void createLabel() {
		attackDefenseLabel = new Label("Defense");
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
		controller = new AuthoringController();
		myLeftToolBar = new LeftToolBar(this, controller);
		rootAdd(myLeftToolBar);
		myGameArea = new GameArea(controller);
		myGameEnvironment = new ScrollableArea(myGameArea);
		rootAdd(myGameEnvironment);
		this.SetDroppable(myGameArea);
		myRightToolBar = new RightToolBar(this, controller);
		rootAdd(myRightToolBar);
		myColorChanger = new ColorChanger(this);
		rootAdd(myColorChanger);
		myThemeChanger = new ThemeChanger(this);
		rootAdd(myThemeChanger);
		myGameChooser = new AttackDefenseToggle(this);
		rootAdd(myGameChooser);
		myMenuBar = new MainMenuBar(this, controller);
		rootAdd(myMenuBar);
		myBottomToolBar = new BottomToolBar(this, controller, myGameEnvironment);
		rootAdd(myBottomToolBar);
	}
	
	public void listItemClicked(InteractiveObject clickable) {
		StaticObject object = (StaticObject) clickable;
		Button addNewButton = new Button("New");
		Button incrementButton = new Button("+");
		Button decrementButton = new Button("-");
		addNewButton.setLayoutY(20);
		incrementButton.setLayoutY(20);
		decrementButton.setLayoutY(20);

		incrementButton.setLayoutX(50);
		decrementButton.setLayoutX(85);
		addNewButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->addObject(object));
		incrementButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->object.incrementSize());
		decrementButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->object.decrementSize());
		rootAdd(addNewButton);
		rootAdd(incrementButton);
		rootAdd(decrementButton);
	}

	private void addObject(InteractiveObject object) {
		InteractiveObject newObject;
		if (object instanceof BackgroundObject) {
			newObject = new BackgroundObject(object.getSize(), this, object.getImageString());
		} else {
			newObject = new StaticObject(object.getSize(), this, object.getImageString());
		}
		myGameArea.addBackObject(newObject);
		newObject.setElementId(controller.placeElement(object.getImageString(), new Point2D(object.getX(),object.getY())));
	}

	@Override
	public void newTowerSelected(ImageView myImageView) {
		
	}

	@Override
	public void clicked(SpriteImage imageView) {
		SelectionWindow mySelectionWindow = new SelectionWindow(imageView, this, controller);
	}

	@Override
	public void changeColor(String color) {
		myGameArea.changeColor(color);
	}
	
	@Override
	public void save(String saveName) {
		controller.saveGameState(saveName);
		myGameArea.savePath();
	}

	public void changeTheme(String theme) {
		rootStyle(myThemeChanger.getThemePath(theme));
//		myRightToolBar.getStyleClass().add("borders");
//		myLeftToolBar.getStyleClass().add("borders");
	}

	private void setStandardTheme() {
		changeTheme(ThemeChanger.STANDARD);
	}

	public void attack() {
		attackDefenseLabel.setText("Defense");
	}

	public void defense() {
		attackDefenseLabel.setText("Attack");
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

	@Override
	public void returnButtonPressed() {
		VBox newProject = new VBox();
		Scene newScene = new Scene(newProject, 400, 400);
		Stage myStage = new Stage();
		myStage.setScene(newScene);
		myStage.show();
		Main restart = new Main();
		restart.start(myStage);
	}

	@Override
	public void imageSelected(SpriteImage imageView) {
		imageView.addBasePropertyMap(basePropertyMap);
//		System.out.println(controller.getAuxiliaryElementConfigurationOptions(basePropertyMap));
		imageView.createInitialProperties(controller.getAuxiliaryElementConfigurationOptions(basePropertyMap));
		myRightToolBar.imageSelected(imageView);
		
	}

	@Override
	public void addToMap(String baseProperty, String value) {
		basePropertyMap.put(baseProperty, value);
//		myRightToolBar.addToMap(baseProperty, value);
		
	}
}
