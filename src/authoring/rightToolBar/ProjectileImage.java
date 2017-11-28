package authoring.rightToolBar;

import java.util.ResourceBundle;


public class ProjectileImage extends SpriteImage {
	
	private ResourceBundle projectileResources;
	private String myKey;
	
	public ProjectileImage(String stringKey) {
		super();
		myKey = stringKey;
		projectileResources = ResourceBundle.getBundle("authoring/resources/NewProjectileImages");
		if(projectileResources.containsKey(stringKey)) {
			this.addImage(projectileResources.getString(stringKey));
		}else {
			this.addImage(stringKey);
		}

	}

	@Override
	public ProjectileImage clone() {
		ProjectileImage cloneImage = new ProjectileImage(myKey);
		cloneImage.setFitHeight(this.getFitHeight());
		cloneImage.setFitWidth(this.getFitWidth());
		return cloneImage;
	}

}
