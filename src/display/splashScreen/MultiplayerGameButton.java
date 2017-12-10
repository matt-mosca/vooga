package display.splashScreen;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import main.Main;

/**
 * Creates a button to move forwards
 * 
 * @author Matt
 */
public class MultiplayerGameButton extends Button {
	
	private static final String LABEL = "Play Multiplayer Game";
	private static final double WIDTH = Main.WIDTH / 3;
	private static final double XPOS = Main.WIDTH / 2 - WIDTH / 2;
	private static final double YPOS = (7.0 / 9.0) * Main.HEIGHT;
	
	public MultiplayerGameButton(SplashInterface splash) {
		this.setPrefWidth(WIDTH);
		this.setLayoutX(XPOS);
		this.setLayoutY(YPOS);
		this.setText(LABEL);
		//this.addEventHandler(MouseEvent.MOUSE_CLICKED, e->splash.playMultiplayer());
//		this.setStyle(  "-fx-border-color: transparent; -fx-border-width: 0;-fx-background-radius: 0;-fx-background-color: red;");
	}
}
