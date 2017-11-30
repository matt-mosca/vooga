package authoring.rightToolBar;

import interfaces.PropertiesInterface;

public class NewInventoryProjectile extends NewInventoryTab {
	
	public NewInventoryProjectile(PropertiesInterface properties) {
		super(properties);
		updateImages();
	}

	@Override
	protected void addNewImage(SpriteImage spriteImage) {
		addImage(spriteImage.clone());
		updateImages();
	}
	
	
}
