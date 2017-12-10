package player;

import display.splashScreen.ScreenDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import main.Main;

public abstract class EndScreen extends ScreenDisplay {
	public EndScreen(int width, int height, Paint background, Stage currentStage) {
		super(width, height, background, currentStage);
		setScreenBackground(width, height);
		createScreenTitle(width, height);
	}
	
	private void setScreenBackground(int screenWidth, int screenHeight) {
		String backgroundName = "grass_large.png";
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(backgroundName));
		ImageView splashBackground = new ImageView(image);
		splashBackground.setFitWidth(screenWidth);
		splashBackground.setFitHeight(screenHeight);
		rootAdd(splashBackground);
	}
	
	private void createScreenTitle(int screenWidth, int screenHeight) {
		String titleName = "VOOGA_Words.png";
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(titleName));
		ImageView voogaTitle = new ImageView(image);
		double titleWidth = voogaTitle.getBoundsInLocal().getWidth();
		double titleHeight = voogaTitle.getBoundsInLocal().getHeight();
		double ratio = titleWidth / screenWidth;
		voogaTitle.setFitWidth(screenWidth);
		voogaTitle.setFitHeight(titleHeight / ratio);
		rootAdd(voogaTitle);
	}
	
	protected void addLabel(String text, int screenWidth, int screenHeight) {
		Label screenLabel = new Label(text);
		screenLabel.setLayoutY(200);
		screenLabel.getStyleClass().add("player/resources/endScreen.css");
		screenLabel.getStylesheets().add("label");
		double width = screenLabel.getBoundsInLocal().getWidth();
		screenLabel.setLayoutX((screenWidth - width) / 2);
	}
	
	protected abstract void createButtons();

	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void listItemClicked(ImageView object) {
		// TODO Auto-generated method stub
		
	}
}
