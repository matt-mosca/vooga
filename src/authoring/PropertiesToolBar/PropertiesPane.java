package authoring.PropertiesToolBar;

import java.util.Map;

import display.factory.TabFactory;
import display.splashScreen.ScreenDisplay;
import engine.authoring_engine.AuthoringController;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;

public class PropertiesPane extends TabPane{
	private ScreenDisplay myDisplay;
	private AuthoringController myController;
    private ImageView myImageView;
    private PropertiesToolBar myProperties;
    private Tab addTab;
    private TabFactory tabMaker;
    private int upgradeSize = 0;
    private boolean projectile;
	
	public PropertiesPane(ScreenDisplay display, PropertiesToolBar properties, ImageView imageView,
			AuthoringController controller, boolean hasProjectile) {
		myImageView = imageView;
		myProperties = properties;
		myController = controller;
		myDisplay = display;
		projectile = hasProjectile;
		tabMaker = new TabFactory();
		
		addTab = new Tab();
		addTab.setClosable(false);
		addTab.setText("Add Upgrade");
		this.getSelectionModel().selectedItemProperty().addListener(e->{
			if(this.getSelectionModel().getSelectedItem() == addTab) {
				Map<String, String> lastUpdateProperties = (upgradeSize > 1) ?
						myController.getAllDefinedElementUpgrades().get(myImageView.getId()).get(upgradeSize-1) :
							myController.getAllDefinedTemplateProperties().get(myImageView.getId());
				myController.defineElementUpgrade(myImageView.getId(), upgradeSize, lastUpdateProperties);
				addUpgrade(lastUpdateProperties);
				this.getSelectionModel().select(upgradeSize);
			}
		});
		
		addUpgrade(myController.getTemplateProperties(imageView.getId()));
		
		if(!myController.getAllDefinedElementUpgrades().isEmpty() &&
				myController.getAllDefinedElementUpgrades().get(imageView.getId()) == null) {
			for(Map<String, String> upgradeMap : myController.getAllDefinedElementUpgrades().get(imageView.getId())) {
				addUpgrade(upgradeMap);
			}
		}
	}
	
	private void addUpgrade(Map<String, String> propertyMap) {
		PropertiesTab newTab = (projectile) ? new PropertiesTabWithProjectile(myDisplay, myProperties, myImageView, propertyMap, myController) 
				: new PropertiesTab(myDisplay, myProperties, myImageView, propertyMap, myController);
		if(this.getTabs().isEmpty()) {
			this.getTabs().add(tabMaker.buildTab("Base Level", null, newTab, this));
			this.getTabs().get(0).setClosable(false);
			
			this.getTabs().add(addTab);
		}else {
			this.getTabs().add(upgradeSize, tabMaker.buildTab("Upgrade " + ++upgradeSize, null, newTab, this));
		}
	}

}
