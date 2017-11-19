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
		
	}
}
