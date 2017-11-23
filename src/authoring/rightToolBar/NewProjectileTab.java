package authoring.rightToolBar;

import java.util.ResourceBundle;

import interfaces.CreationInterface;

public class NewProjectileTab extends NewSpriteTab {
	
	private ResourceBundle projectileResources;
	
	public NewProjectileTab(CreationInterface created) {
		super(created);
		projectileResources = ResourceBundle.getBundle("authoring/resources/NewProjectileImages");
		addDefaultImages();
		updateImages();
	}

	@Override
	protected void addDefaultImages() {
//		addImage(projectileResources.getString("Gray_Circle"));
//		addImage(projectileResources.getString("Black_Square"));
//		addImage(projectileResources.getString("Black_Square2"));
//		addImage(projectileResources.getString("Orange_Splash"));
//		addImage(projectileResources.getString("Fireball"));
	}
}
