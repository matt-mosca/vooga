package authoring.rightToolBar;

import java.util.ResourceBundle;


public class TroopImage extends SpriteImage {
	
	private ResourceBundle troopResources;
	
	public TroopImage(String stringKey) {
		super();
		troopResources = ResourceBundle.getBundle("authoring/resources/NewTroopImages");
		this.addImage(troopResources.getString(stringKey));

	}

}
