package authoring.rightToolBar;

import display.interfaces.PropertiesInterface;

/**
 * @deprecated
 * @author 
 *
 */
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
