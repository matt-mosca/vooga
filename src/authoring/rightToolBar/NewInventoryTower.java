package authoring.rightToolBar;

import interfaces.PropertiesInterface;

public class NewInventoryTower extends NewInventoryTab {
	
	public NewInventoryTower(PropertiesInterface properties) {
		super(properties);
		updateImages();
	}

	@Override
	protected void addNewImage(SpriteImage spriteImage) {
		addImage(spriteImage.clone());
		updateImages();
	}
}
