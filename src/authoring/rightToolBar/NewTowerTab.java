package authoring.rightToolBar;

import java.util.ResourceBundle;

import interfaces.CreationInterface;
import javafx.scene.image.ImageView;

public class NewTowerTab extends NewSpriteTab {
	
	private ResourceBundle towerResources;
	
	public NewTowerTab(CreationInterface created) {
		super(created);
		towerResources = ResourceBundle.getBundle("authoring/resources/NewTowerImages");
		addDefaultImages();
		updateImages();
	}

	@Override
	protected void addDefaultImages() {
		addImage(towerResources.getString("Black_Square"));
		addImage(towerResources.getString("Black_Square2"));
		addImage(towerResources.getString("Green_Turret1"));
		addImage(towerResources.getString("Green_Turret2"));
		addImage(towerResources.getString("Green_Turret3"));
		addImage(towerResources.getString("Green_Turret4"));
		addImage(towerResources.getString("Green_Turret5"));
		addImage(towerResources.getString("Black_Turret"));
		addImage(towerResources.getString("Cannon1"));
		addImage(towerResources.getString("Cannon2"));
		addImage(towerResources.getString("Castle_Tower1"));
		addImage(towerResources.getString("Castle_Tower2"));
		addImage(towerResources.getString("Castle_Tower3"));
		addImage(towerResources.getString("Castle_Tower4"));
	}
}
