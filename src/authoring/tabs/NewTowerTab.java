package authoring.tabs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import authoring.rightToolBar.TowerImage;
import interfaces.CreationInterface;
import javafx.scene.image.ImageView;

public class NewTowerTab extends NewSpriteTab {
	
	private ResourceBundle towerResources;
	
	public NewTowerTab(CreationInterface created) {
		super(created);
		towerResources = ResourceBundle.getBundle("authoring/resources/NewTowerImages");
		addDefaultImages();
	}
	
	@Override
	protected void addDefaultImages() {
		List<String> imageList = new ArrayList<String>(Arrays.asList("Black_Square2",
				"Green_Turret1", "Green_Turret2", "Green_Turret3", "Green_Turret4",
				"Green_Turret5", "Black_Turret", "Cannon1", "Cannon2", 
				"Castle_Tower1", "Castle_Tower2", "Castle_Tower3", "Castle_Tower4"));
		addImages(imageList);
	}

private void addImages(List<String> stringNames) {
	for (String s : stringNames) {
		addImage(new TowerImage(s));
	}
}
}
