package authoring.rightToolBar;

public class NewInventoryTower extends NewInventoryTab {
	
	public NewInventoryTower() {
		super();
		updateImages();
	}

	@Override
	protected void addNewImage(SpriteImage spriteImage) {
		addImage(spriteImage.clone());
		updateImages();
	}
}
