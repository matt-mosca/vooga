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
	}
}
