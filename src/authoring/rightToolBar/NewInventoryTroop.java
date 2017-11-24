package authoring.rightToolBar;

public class NewInventoryTroop extends NewInventoryTab {
	
	public NewInventoryTroop() {
		super();
		updateImages();
	}

	@Override
	protected void addNewImage(SpriteImage spriteImage) {
		addImage(spriteImage.clone());
		updateImages();
	}
}
