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
		return new TroopImage(myKey);
	}

}
