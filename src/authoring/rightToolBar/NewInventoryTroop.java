package authoring.rightToolBar;

import interfaces.PropertiesInterface;

public class NewInventoryTroop extends NewInventoryTab {
	
	public NewInventoryTroop(PropertiesInterface properties) {
		super(properties);
		updateImages();
	}

	@Override
	protected void addNewImage(SpriteImage spriteImage) {
		addImage(spriteImage.clone());
		updateImages();
	}
}
