package authoring.rightToolBar;

public class NewInventoryProjectile extends NewInventoryTab {
	
	public NewInventoryProjectile() {
		super();
		updateImages();
	}

	@Override
	protected void addNewImage(SpriteImage spriteImage) {
		addImage(spriteImage.clone());
		updateImages();
	}
	
	
}
