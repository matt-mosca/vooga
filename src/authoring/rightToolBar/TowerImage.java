package authoring.rightToolBar;

import java.util.ResourceBundle;


public class TowerImage extends SpriteImage {
	
	private ResourceBundle towerResources;
	private String myKey;
	
	public TowerImage(String stringKey) {
		super();
		myKey = stringKey;
		towerResources = ResourceBundle.getBundle("authoring/resources/NewTowerImages");
		if(towerResources.containsKey(stringKey)) {
			this.addImage(towerResources.getString(stringKey));
		}else {
			this.addImage(stringKey);
		}
		
	}
	
	@Override
	public TowerImage clone() {
		TowerImage cloneImage = new TowerImage(myKey);
		cloneImage.setFitHeight(this.getFitHeight());
		cloneImage.setFitWidth(this.getFitWidth());
		return cloneImage;
	}

}
