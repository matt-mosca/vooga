package authoring.rightToolBar;

import java.util.ResourceBundle;

import splashScreen.ScreenDisplay;


public class TroopImage extends SpriteImage {
	
	private ScreenDisplay myDisplay;
	private ResourceBundle troopResources;
	private String myKey;
	
	public TroopImage(ScreenDisplay display, String stringKey) {
		super(display);
		myDisplay = display;
		myKey = stringKey;
		troopResources = ResourceBundle.getBundle("authoring/resources/NewTroopImages");
		if(troopResources.containsKey(stringKey)) {
			this.addImage(troopResources.getString(stringKey));
		}else {
			this.addImage(stringKey);
		}

	}
	
	@Override
	public TroopImage clone() {
		TroopImage cloneImage = new TroopImage(myDisplay, myKey);
		cloneImage.setName(this.getName());
		cloneImage.setFitHeight(this.getFitHeight());
		cloneImage.setFitWidth(this.getFitWidth());
		return cloneImage;
	}

}
