package authoring.rightToolBar;

import display.interfaces.PropertiesInterface;

/**
 * @deprecated
 * @author 
 *
 */
public class NewInventoryProjectile extends NewInventoryTab {
	
	public NewInventoryProjectile(PropertiesInterface properties) {
		super(properties);
		updateImages();
	}

	@Override
	protected void addNewImage(SpriteImage spriteImage) {
		spriteImage.setFitWidth(10);
		spriteImage.setFitHeight(10);
		addImage(spriteImage.clone());
		updateImages();
	}
	
	
}
