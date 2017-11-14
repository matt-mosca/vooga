package splashScreen;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 * Creates a button to move forwards
 * 
 * @author Matt
 */
public class PlayExistingGameButton extends Button {
	
	private static final String LABEL = "Play Existing Game";
	private static final double XPOS = 50;
	private static final double YPOS = 200;
	private static final double WIDTH = 200;
	
	public PlayExistingGameButton(SplashInterface splash) {
		this.setPrefWidth(WIDTH);
		this.setLayoutX(XPOS);
		this.setLayoutY(YPOS);
		this.setText(LABEL);
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, e->splash.playExisting());
//		this.setStyle(  "-fx-border-color: transparent; -fx-border-width: 0;-fx-background-radius: 0;-fx-background-color: red;");
	}
}
