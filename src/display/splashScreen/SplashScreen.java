package display.splashScreen;

import java.io.File;

import authoring.EditDisplay;
import display.interfaces.ClickableInterface;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import main.Main;
import player.PlayDisplay;

public class SplashScreen extends ScreenDisplay implements SplashInterface {

	private static final int PREFSIZE = 80;
	private static final int MAINWIDTH = 1100;
	private static final int MAINHEIGHT = 750;
	private static final int PLAYWIDTH = 1000;
	private static final int PLAYHEIGHT = 700;
	private static final String TITLEFONT = "Verdana";
	private static final String TITLE = "Welcome to VOOGA";
	private static final double STANDARD_PATH_WIDTH = Main.WIDTH / 15;
	private static final double STANDARD_PATH_HEIGHT = Main.HEIGHT / 15;
	
	private HBox titleBox = new HBox();
	private Text VoogaTitle;
	
	private NewGameButton myNewGameButton;
	private EditGameButton myEditGameButton;
	private PlayExistingGameButton myLoadGameButton;


	public SplashScreen(int width, int height, Paint background, Stage currentStage) {
		super(width, height, background, currentStage);
		
		getStage().setResizable(false);
		basicSetup();
		myNewGameButton = new NewGameButton(this);
		rootAdd(myNewGameButton);
		myEditGameButton = new EditGameButton(this);
		rootAdd(myEditGameButton);
		myLoadGameButton = new PlayExistingGameButton(this);
		rootAdd(myLoadGameButton);
		
	}

	private void basicSetup() {
//		createTitle();
		setSplashBackground();
		createPathTitle();
		createSubtitle();
		addPath();
	}

	private void createTitle() {
		VoogaTitle = new Text(10, 20, TITLE);
		VoogaTitle.setFont(Font.font(TITLEFONT, FontPosture.ITALIC, 30));
		VoogaTitle.setFill(Color.DARKBLUE);
//		VoogaTitle.setFill(Color.GOLD);
//		VoogaTitle.setFill(Color.SILVER);
		titleBox = new HBox();
		titleBox.setAlignment(Pos.CENTER);
		titleBox.getChildren().add(VoogaTitle);
		titleBox.setPrefSize(PREFSIZE, PREFSIZE);
		rootAdd(titleBox);
	}
	
	private void setSplashBackground() {
		String backgroundName = "grass_large.png";
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(backgroundName));
		ImageView splashBackground = new ImageView(image);
		splashBackground.setFitWidth(Main.WIDTH);
		splashBackground.setFitHeight(Main.HEIGHT);
		rootAdd(splashBackground);
	}
	
	private void createPathTitle() {
		String titleName = "VOOGA_Words.png";
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(titleName));
		ImageView voogaTitle = new ImageView(image);
		double width = voogaTitle.getBoundsInLocal().getWidth();
		double height = voogaTitle.getBoundsInLocal().getHeight();
		double ratio = width / Main.WIDTH;
		voogaTitle.setFitWidth(Main.WIDTH);
		voogaTitle.setFitHeight(height / ratio);
		rootAdd(voogaTitle);
	}
	
	private void createSubtitle() {
		Label subtitle = new Label("TOWER DEFENSE GAME AUTHORING & PLAYING ENVIRONMENT");
		subtitle.setFont(new Font("American Typewriter", Main.WIDTH / 40));
		subtitle.setTextFill(Color.BLACK);
		subtitle.setLayoutX(Main.WIDTH / 10);
		subtitle.setLayoutY(Main.HEIGHT / 3);
		rootAdd(subtitle);
	}
	
	private void addPath() {
		for(int i = 0; i < 5; i++) {
			createStandardPath(STANDARD_PATH_WIDTH * i, Main.HEIGHT / 2);
		}
		for(int i = 0; i < 3; i++) {
			createStandardPath(STANDARD_PATH_WIDTH * 4, Main.HEIGHT / 2 + (i + 1) * STANDARD_PATH_HEIGHT);
		}
		for(int i = 4; i < 11; i++) {
			createStandardPath(STANDARD_PATH_WIDTH * i, Main.HEIGHT / 2 + 4 * STANDARD_PATH_HEIGHT);
		}
		//Next two for asymmetric style
		for(int i = 2; i < 3; i++) {
			createStandardPath(STANDARD_PATH_WIDTH * 10, Main.HEIGHT / 2 + (i + 1) * STANDARD_PATH_HEIGHT);
		}
		for(int i = 10; i < 15; i++) {
			createStandardPath(STANDARD_PATH_WIDTH * i, Main.HEIGHT / 2 + 2 * STANDARD_PATH_HEIGHT);
		}
		//Next two for symmetric style
//		for(int i = 0; i < 3; i++) {
//			createStandardPath(STANDARD_PATH_WIDTH * 10, Main.HEIGHT / 2 + (i + 1) * STANDARD_PATH_HEIGHT);
//		}
//		for(int i = 10; i < 15; i++) {
//			createStandardPath(STANDARD_PATH_WIDTH * i, Main.HEIGHT / 2);
//		}
		//First two plus next one for third path style
//		for(int i = 4; i < 15; i++) {
//			createStandardPath(STANDARD_PATH_WIDTH * i, Main.HEIGHT / 2 + 4 * STANDARD_PATH_HEIGHT);
//		}
	}
	
	private ImageView createStandardPath(double xPos, double yPos) {
		String pathName = "brick_path.png";
//		String pathName = "stone_path2.png";
		Image pathImage = new Image(getClass().getClassLoader().getResourceAsStream(pathName));
		ImageView path = new ImageView(pathImage);
		path.setFitWidth(STANDARD_PATH_WIDTH);
		path.setFitHeight(STANDARD_PATH_HEIGHT);
		path.setX(xPos);
		path.setY(yPos);
		rootAdd(path);
		return path;
	}

	@Override
	public void editButtonPressed() {
		EditDisplay myScene = new EditDisplay(MAINWIDTH, MAINHEIGHT, getStage(), true);
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		getStage().setX(primaryScreenBounds.getWidth() / 2 - MAINWIDTH / 2);
		getStage().setY(primaryScreenBounds.getHeight() / 2 - MAINHEIGHT / 2);
		getStage().setScene(myScene.getScene());
	}

	@Override
	public void newGameButtonPressed() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void switchScreen() {
		EditDisplay myScene = new EditDisplay(MAINWIDTH, MAINHEIGHT, getStage(), false);
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		getStage().setX(primaryScreenBounds.getWidth() / 2 - MAINWIDTH / 2);
		getStage().setY(primaryScreenBounds.getHeight() / 2 - MAINHEIGHT / 2);
		getStage().setScene(myScene.getScene());
	}

	@Override
	public void playExisting() {
		// TODO - Update this method accordingly to determine the isMultiPlayer param
		// for PlayDisplay constructor
		PlayDisplay myScene = new PlayDisplay(PLAYWIDTH, PLAYHEIGHT, getStage(), false); // TEMP
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		getStage().setX(primaryScreenBounds.getWidth() / 2 - PLAYWIDTH / 2);
		getStage().setY(primaryScreenBounds.getHeight() / 2 - PLAYHEIGHT / 2);
		getStage().setScene(myScene.getScene());
		
	}

	@Override
	public void save(File saveName) {
		// TODO Auto-generated method stub
		
	}

@Override
public void listItemClicked(ImageView object) {
	// TODO Auto-generated method stub
	
}



}
