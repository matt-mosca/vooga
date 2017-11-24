package authoring.rightToolBar;

import java.util.ResourceBundle;


public class TowerImage extends SpriteImage {
	
	private ResourceBundle towerResources;
	private String myKey;
	
	public TowerImage(String stringKey) {
		super();
		myKey = stringKey;
		towerResources = ResourceBundle.getBundle("authoring/resources/NewTowerImages");
		this.addImage(towerResources.getString(stringKey));
	}
	
	@Override
	public TowerImage clone() {
		return new TowerImage(myKey);
	}

}
