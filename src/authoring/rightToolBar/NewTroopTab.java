package authoring.rightToolBar;

import java.util.ResourceBundle;

import interfaces.CreationInterface;
import javafx.scene.image.ImageView;

public class NewTroopTab extends NewSpriteTab {
	
	private ResourceBundle towerResources;
	
	public NewTroopTab(CreationInterface created) {
		super(created);
		towerResources = ResourceBundle.getBundle("authoring/resources/NewTroopImages");
		addDefaultImages();
		updateImages();
	}

	@Override
	protected void addDefaultImages() {
		addImage(towerResources.getString("Green_Tank"));
		addImage(towerResources.getString("Blue_Soldier"));
	}
}
