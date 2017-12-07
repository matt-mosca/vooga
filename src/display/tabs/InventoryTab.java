package display.tabs;

import java.util.List;

import authoring.rightToolBar.SpriteImage;
import display.interfaces.PropertiesInterface;
import javafx.scene.image.ImageView;
import display.splashScreen.ScreenDisplay;

public class InventoryTab extends SimpleTab{
	private PropertiesInterface myProperties;
	
	public InventoryTab(ScreenDisplay display, List<ImageView> defaults, PropertiesInterface properties) {
		super(display, defaults);
		myProperties = properties;
	}
	
	@Override
	protected void addHandler() {
		myListView.setOnMouseClicked(e->myProperties.clicked(
        		myListView.getSelectionModel().getSelectedItem()));
	}

}
