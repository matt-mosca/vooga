package authoring.rightToolBar;

import java.util.ResourceBundle;


public class TowerImage extends SpriteImage {
	
	private ResourceBundle towerResources;
	private String myKey;
	private SpriteImage myProjectile;
	
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
	
	public void addProjectileImage(SpriteImage newProjectile) {
		myProjectile = newProjectile;
	}
	
	public SpriteImage getProjectileImage() {
		return myProjectile;
	}
	
	public boolean hasProjectile() {
		return myProjectile != null;
	}
	
	@Override
	public TowerImage clone() {
		TowerImage cloneImage = new TowerImage(myKey);
		cloneImage.setFitHeight(this.getFitHeight());
		cloneImage.setFitWidth(this.getFitWidth());
		return cloneImage;
	}
}
