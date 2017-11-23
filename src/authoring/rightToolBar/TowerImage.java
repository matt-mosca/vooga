package authoring.rightToolBar;

import java.util.ResourceBundle;


public class TowerImage extends SpriteImage {
	
	private ResourceBundle towerResources;
	
	public TowerImage(String stringKey) {
		super();
		towerResources = ResourceBundle.getBundle("authoring/resources/NewTowerImages");
		this.addImage(towerResources.getString(stringKey));

	}

}
