package authoring.rightToolBar;

import java.util.ResourceBundle;


public class TroopImage extends SpriteImage {
	
	private ResourceBundle troopResources;
	private String myKey;
	
	public TroopImage(String stringKey) {
		super();
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
		TroopImage cloneImage = new TroopImage(myKey);
		cloneImage.setName(this.getName());
		cloneImage.setFitHeight(this.getFitHeight());
		cloneImage.setFitWidth(this.getFitWidth());
		return cloneImage;
	}

}
