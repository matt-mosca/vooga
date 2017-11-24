package authoring.rightToolBar;

import java.util.ResourceBundle;


public class TroopImage extends SpriteImage {
	
	private ResourceBundle troopResources;
	private String myKey;
	
	public TroopImage(String stringKey) {
		super();
		myKey = stringKey;
		troopResources = ResourceBundle.getBundle("authoring/resources/NewTroopImages");
		this.addImage(troopResources.getString(stringKey));

	}
	
	@Override
	public TroopImage clone() {
		TroopImage cloneImage = new TroopImage(myKey);
		cloneImage.setFitHeight(this.getFitHeight());
		cloneImage.setFitWidth(this.getFitWidth());
		return cloneImage;
	}

}
