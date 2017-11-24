package authoring.rightToolBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
		List<String> imageList = new ArrayList<String>(Arrays.asList("Gray_Circle",
				"Black_Square", "Black_Square2", "Orange_Splash", "Fireball"));
		addImages(imageList);
	}
	
	private void addImages(List<String> stringNames) {
		for (String s : stringNames) {
			addImage(new ProjectileImage(s));
		}
	}
}
