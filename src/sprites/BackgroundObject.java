package sprites;

import splashScreen.ScreenDisplay;

public class BackgroundObject extends StaticObject {
	
	public BackgroundObject(int size, ScreenDisplay display, String imageString) {
		super(size, display, imageString);
		this.toBack();
	}
}
