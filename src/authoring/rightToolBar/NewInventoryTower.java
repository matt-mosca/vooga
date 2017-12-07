package authoring.rightToolBar;

import interfaces.PropertiesInterface;

public class NewInventoryTower extends NewInventoryTab {
	
	/**
	 * @deprecated
	 * @param properties
	 */
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
