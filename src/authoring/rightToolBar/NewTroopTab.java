package authoring.rightToolBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import interfaces.CreationInterface;
import javafx.scene.image.ImageView;

public class NewTroopTab extends NewSpriteTab {
	
	private ResourceBundle troopResources;
	
	public NewTroopTab(CreationInterface created) {
		super(created);
		troopResources = ResourceBundle.getBundle("authoring/resources/NewTroopImages");
		addDefaultImages();
		updateImages();
	}

	@Override
	protected void addDefaultImages() {
		List<String> imageList = new ArrayList<String>(Arrays.asList("Red_Balloon", 
				"Black_Square", "Black_Square2","Rotating_Black_Square", 
				"Green_Soldier", "Blue_Soldier","Black_Soldier", "Blue_Tank", 
				"Green_Tank", "Red_Tank","Cannon", "Green_Tank_Animated1", 
				"Green_Tank_Animated2", "Blue_Tank_Animated1", "Blue_Tank_Animated2", 
				"Red_Tank_Animated1", "Red_Tank_Animated2"));
		addImages(imageList);
	}
	
	private void addImages(List<String> stringNames) {
		for (String s : stringNames) {
			addImage(new TroopImage(s));
		}
	}
}
