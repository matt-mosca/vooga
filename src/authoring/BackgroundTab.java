package authoring;

import authoring.rightToolBar.NewSpriteTab;
import interfaces.CreationInterface;
import sprites.BackgroundObject;

public class BackgroundTab extends NewSpriteTab{
	private BackgroundObject myBackground1;
	private BackgroundObject myBackground2;
	private BackgroundObject myBackground3;
	private BackgroundObject myBackground4;
	private BackgroundObject myBackground5;
	private AuthorInterface author; 
	
	public BackgroundTab(AuthorInterface ai) {
		super(ai);
		author = ai;
	}

	@Override
	protected void addDefaultImages() {
		myBackground1 = createNewBackground(3, "grass_small.png");
		myBackground2 = createNewBackground(3, "grass2_small.png");
		myBackground3 = createNewBackground(2, "brick_path.png");
		myBackground4 = createNewBackground(2, "stone_path1.png");
		myBackground5 = createNewBackground(3, "water_medium.png");
		
	}
	
	private BackgroundObject createNewBackground(int size, String imageString) {
		return new BackgroundObject(size, author, imageString);
	}

}
