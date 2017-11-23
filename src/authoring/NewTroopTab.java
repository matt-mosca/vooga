package authoring;

import java.util.ResourceBundle;

public class NewTroopTab extends NewSpriteTab {
	
	private ResourceBundle troopResources;
	
	public NewTroopTab() {
		super();
		troopResources = ResourceBundle.getBundle("authoring/resources/NewTroopImages");
		addDefaultImages();
		updateImages();
	}

	@Override
	protected void addDefaultImages() {
		// TODO Auto-generated method stub
		addImage(troopResources.getString("Red_Balloon"));
		addImage(troopResources.getString("Black_Square"));
		addImage(troopResources.getString("Black_Square2"));
		addImage(troopResources.getString("Rotating_Black_Square"));
		addImage(troopResources.getString("Green_Soldier"));
		addImage(troopResources.getString("Blue_Soldier"));
		addImage(troopResources.getString("Black_Soldier"));
		addImage(troopResources.getString("Blue_Tank"));
		addImage(troopResources.getString("Green_Tank"));
		addImage(troopResources.getString("Red_Tank"));
		addImage(troopResources.getString("Cannon"));
		addImage(troopResources.getString("Green_Tank_Animated1"));
		addImage(troopResources.getString("Green_Tank_Animated2"));
		addImage(troopResources.getString("Blue_Tank_Animated1"));
		addImage(troopResources.getString("Blue_Tank_Animated2"));
		addImage(troopResources.getString("Red_Tank_Animated1"));
		addImage(troopResources.getString("Red_Tank_Animated2"));
	}
}
