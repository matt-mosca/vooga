package splashScreen;

import authoring.EditDisplay;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import player.PlayDisplay;

public class SplashScreen extends ScreenDisplay implements SplashInterface {

	private static final int PREFSIZE = 80;
	private static final int MAINWIDTH = 1000;
	private static final int MAINHEIGHT = 600;
	private static final String TITLEFONT = "Verdana";
	private static final String TITLE = "Welcome to VOOGA";
	private HBox titleBox = new HBox();
	private Text VoogaTitle;
	private Stage stage;
	private NewGameButton myNewGameButton;
	private EditGameButton myEditGameButton;
	private PlayExistingGameButton myLoadGameButton;


	public SplashScreen(int width, int height, Paint background, Stage currentStage) {
		super(width, height, background);
		stage = currentStage;
		basicSetup();
		myNewGameButton = new NewGameButton(this);
		rootAdd(myNewGameButton);
		myEditGameButton = new EditGameButton(this);
		rootAdd(myEditGameButton);
		myLoadGameButton = new PlayExistingGameButton(this);
		rootAdd(myLoadGameButton);
	}

	private void basicSetup() {
		createTitle();
	}

	public void createTitle() {
		VoogaTitle = new Text(10, 20, TITLE);
		VoogaTitle.setFont(Font.font(TITLEFONT, FontPosture.ITALIC, 30));
		VoogaTitle.setFill(Color.DARKBLUE);
		titleBox = new HBox();
		titleBox.setAlignment(Pos.CENTER);
		titleBox.getChildren().add(VoogaTitle);
		titleBox.setPrefSize(PREFSIZE, PREFSIZE);
		rootAdd(titleBox);
	}

	@Override
	public void editButtonPressed() {
		// TODO Auto-generated method stub
		System.out.println("Edit");
	}

	@Override
	public void newGameButtonPressed() {
		// TODO Auto-generated method stub
		System.out.println("New");
	}
	
	@Override
	public void switchScreen() {
		EditDisplay myScene = new EditDisplay(MAINWIDTH, MAINHEIGHT);
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		stage.setX(primaryScreenBounds.getWidth() / 2 - MAINWIDTH / 2);
		stage.setY(primaryScreenBounds.getHeight() / 2 - MAINHEIGHT / 2);
		stage.setScene(myScene.getScene());
	}

	@Override
	public void playExisting() {
		PlayDisplay myScene = new PlayDisplay(MAINWIDTH, MAINHEIGHT);
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		stage.setX(primaryScreenBounds.getWidth() / 2 - MAINWIDTH / 2);
		stage.setY(primaryScreenBounds.getHeight() / 2 - MAINHEIGHT / 2);
		stage.setScene(myScene.getScene());
		
	}



}
