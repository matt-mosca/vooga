package authoring.rightToolBar;

import java.util.ResourceBundle;


public class ProjectileImage extends SpriteImage {
	
	private ResourceBundle projectileResources;
	
	public ProjectileImage(String stringKey) {
		super();
		projectileResources = ResourceBundle.getBundle("authoring/resources/NewProjectileImages");
		this.addImage(projectileResources.getString(stringKey));

	}

}
