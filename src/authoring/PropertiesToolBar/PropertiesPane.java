package authoring.PropertiesToolBar;

import display.factory.TabFactory;
import engine.authoring_engine.AuthoringController;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;

public class PropertiesPane extends TabPane{
	private AuthoringController myController;
    private ImageView myImageView;
    private PropertiesToolBar myProperties;
    private Tab addTab;
    private TabFactory tabMaker;
    private int upgradeSize = 1;
    private boolean projectile;
	
	public PropertiesPane(PropertiesToolBar properties, ImageView imageView, AuthoringController controller, boolean hasProjectile) {
		myImageView = imageView;
		myProperties = properties;
		myController = controller;
		projectile = hasProjectile;
		tabMaker = new TabFactory();
		
		addTab = new Tab();
		addTab.setClosable(false);
		addTab.setText("Add Upgrade");
		
		addUpgrade();
	}
	
	private void addUpgrade() {
		PropertiesTab newTab = (projectile) ? new PropertiesTab(myProperties, myImageView) 
				: new PropertiesTabWithProjectile(myProperties, myImageView, myController);
		if(this.getTabs().isEmpty()) {
			this.getTabs().add(tabMaker.buildTab("Base Level", null, newTab, this));
			this.getTabs().get(0).setClosable(false);
			
			this.getTabs().add(addTab);
		}else {
			this.getTabs().add(upgradeSize, tabMaker.buildTab("Upgrade " + upgradeSize++, null, newTab, this));
		}
	}

}
