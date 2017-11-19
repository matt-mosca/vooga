package authoring;

import java.util.ResourceBundle;

public class NewProjectileTab extends NewSpriteTab {
	
	private ResourceBundle projectileResources;
	
	public NewProjectileTab() {
		super();
		projectileResources = ResourceBundle.getBundle("authoring/resources/NewProjectileImages");
		addDefaultImages();
		updateImages();
	}

	@Override
	protected void addDefaultImages() {
		addImage(projectileResources.getString("Gray_Circle"));
		addImage(projectileResources.getString("Black_Square"));
		addImage(projectileResources.getString("Black_Square2"));
	}
}
