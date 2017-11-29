package authoring.rightToolBar;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import authoring.AuthorInterface;
import authoring.EditDisplay;
import authoring.ObjectProperties;
import authoring.tabs.AddSpriteImageTab;
import authoring.tabs.AddTab;
import authoring.tabs.NewProjectileTab;
import authoring.tabs.NewSpriteTab;
import authoring.tabs.NewTowerTab;
import authoring.tabs.NewTroopTab;
import engine.authoring_engine.AuthoringController;
import factory.ButtonFactory;
import factory.TabFactory;
import interfaces.CreationInterface;
import interfaces.PropertiesInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
 
public class RightToolBar extends VBox implements PropertiesInterface {
	
	private TabFactory tabMaker;
	private TabPane topTabPane;
	private TabPane bottomTabPane;
	private AddTab addTab;
	private NewSpriteTab newTower;
	private NewSpriteTab newTroop;
	private NewSpriteTab newProjectile;
	private NewInventoryTab inventoryTower;
	private NewInventoryTab inventoryTroop;
	private NewInventoryTab inventoryProjectile;
	private AddNewButton myNewButton;
	private PropertiesBox myPropertiesBox;
	private Pane propertiesPane;
	private Label projectileLabel;
	private HBox projectileSlot;
	private Button deleteButton;
	private CreationInterface created;
	private AuthoringController myController;
	private Map<String, String> basePropertyMap;
	private final int X_LAYOUT = 680;
	private final int Y_LAYOUT = 25;

	
	public RightToolBar(EditDisplay display, AuthoringController controller) {
		this.created = created;
		myController = controller;
        this.setLayoutX(X_LAYOUT);
		this.setLayoutY(Y_LAYOUT);
	    tabMaker = new TabFactory();
	    topTabPane = new TabPane();
	    bottomTabPane = new TabPane();
	    topTabPane.setPrefHeight(250);
	    bottomTabPane.setPrefHeight(250);
	    createAndAddTabs();
	    newTower = new NewTowerTab(display);   
	    newTroop = new NewTroopTab(display); 
	    newProjectile = new NewProjectileTab(display); 
	    inventoryTower = new NewInventoryTower(this);
	    inventoryTroop = new NewInventoryTroop(this);
	    inventoryProjectile = new NewInventoryProjectile(this);
	    myNewButton = new AddNewButton(created);
        this.getChildren().add(topTabPane);
        this.getChildren().add(bottomTabPane);
        
        newTower.attach(topTabPane.getTabs().get(0));
        newTroop.attach(topTabPane.getTabs().get(1));
        newProjectile.attach(topTabPane.getTabs().get(2));
        inventoryTower.attach(bottomTabPane.getTabs().get(0));
        inventoryTroop.attach(bottomTabPane.getTabs().get(1));
        inventoryProjectile.attach(bottomTabPane.getTabs().get(2));
        basePropertyMap = new HashMap<String, String>();
    }
		
	private void createAndAddTabs() {
		topTabPane.getTabs().add(tabMaker.buildTabWithoutContent("New Tower", "TowerImage", topTabPane));
		topTabPane.getTabs().add(tabMaker.buildTabWithoutContent("New Troop", "TroopImage", topTabPane));
		topTabPane.getTabs().add(tabMaker.buildTabWithoutContent("New Projectile", "ProjectileImage", topTabPane));
		addTab = new AddSpriteImageTab(null , topTabPane);
		topTabPane.getTabs().add(tabMaker.buildTab("Add Image", null, addTab, topTabPane));
		
		bottomTabPane.getTabs().add(tabMaker.buildTabWithoutContent("Inventory Towers", "TowerImage", bottomTabPane));
		bottomTabPane.getTabs().add(tabMaker.buildTabWithoutContent("Inventory Troops", "TroopImage", bottomTabPane));
		bottomTabPane.getTabs().add(tabMaker.buildTabWithoutContent("Inventory Projectile", "ProjectileImage", bottomTabPane));
		makeTabsUnclosable();
	}
	
	@Override
	public void imageSelected(SpriteImage myImageView) {
		myPropertiesBox = new PropertiesBox(created, myImageView);
		if (myImageView instanceof TowerImage) inventoryTower.addNewImage(myImageView);
		if (myImageView instanceof TroopImage) inventoryTroop.addNewImage(myImageView);
		if (myImageView instanceof ProjectileImage) inventoryProjectile.addNewImage(myImageView);
	}
	
	private void makeTabsUnclosable() {
		for(int i = 0; i < topTabPane.getTabs().size(); i++) {
			topTabPane.getTabs().get(i).setClosable(false);
		}
		for(int i = 0; i < bottomTabPane.getTabs().size(); i++) {
			bottomTabPane.getTabs().get(i).setClosable(false);
		}
	}

	@Override
	public void clicked(SpriteImage imageView) {	
		myPropertiesBox = new PropertiesBox(created, imageView);
		if (imageView instanceof TowerImage) newPaneWithProjectileSlot((TowerImage) imageView);
		if (imageView instanceof TroopImage) newPane(imageView);
	}
	
	private void newPaneWithProjectileSlot(TowerImage imageView) {
		/**
		 * Awful code atm, it'll be refactored dw, just trying to get it all to work <3
		 */
		projectileLabel = new Label("Click to\nChoose a\nprojectile");
		projectileLabel.setLayoutY(90);
		projectileSlot = new HBox();
		projectileSlot.setPrefWidth(50);
		projectileSlot.setPrefHeight(50);
		projectileSlot.setLayoutY(170);
		projectileSlot.setStyle("-fx-background-color: white");
		projectileSlot.addEventHandler(MouseEvent.MOUSE_CLICKED, e->newProjectilesWindow((TowerImage) imageView));
		propertiesPane = new Pane();
		deleteButton = new Button("Back");
		deleteButton.setLayoutX(370);
		Label info = new Label("Properties here");
		info.setLayoutY(100);
		info.setFont(new Font("Arial", 30));
		myPropertiesBox.setLayoutX(100);
		deleteButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->removeButtonPressed());
		HBox imageBackground = new HBox();
		imageBackground.setStyle("-fx-background-color: white");
		imageBackground.getChildren().add(imageView.clone());
		if (imageView.hasProjectile()) projectileSlot.getChildren().add(imageView.getProjectileImage());
		propertiesPane.getChildren().add(imageBackground);
		propertiesPane.getChildren().add(deleteButton);
		propertiesPane.getChildren().add(myPropertiesBox);
		propertiesPane.getChildren().add(projectileLabel);
		propertiesPane.getChildren().add(projectileSlot);
		this.getChildren().removeAll(this.getChildren());
		this.getChildren().add(propertiesPane);
		this.getChildren().add(bottomTabPane);

	}
	
	private void newProjectilesWindow(TowerImage myTowerImage) {
		ScrollPane projectilesWindow = new ScrollPane();
		ListView<SpriteImage> projectilesView = new ListView<SpriteImage>();
		if (inventoryProjectile.getImages().isEmpty()) {
			Label emptyLabel = new Label("You have no projectiles\nin your inventory");
			propertiesPane.getChildren().remove(myPropertiesBox);
			emptyLabel.setLayoutX(100);
			propertiesPane.getChildren().add(emptyLabel);
		} else {
			List<SpriteImage> cloneList = new ArrayList<>();
			for (SpriteImage s : inventoryProjectile.getImages()) {
				cloneList.add(s.clone());
			}
			ObservableList<SpriteImage> items =FXCollections.observableArrayList(cloneList);
	        projectilesView.setItems(items);
	        projectilesView.getSelectionModel();
	        projectilesWindow.setContent(projectilesView);
	        projectilesWindow.setLayoutX(100);
	        projectilesWindow.setPrefHeight(250);
	        projectilesView.setOnMouseClicked(e->projectileSelected(myTowerImage,
	        		projectilesView.getSelectionModel().getSelectedItem().clone()));
        propertiesPane.getChildren().remove(myPropertiesBox);
        propertiesPane.getChildren().add(projectilesWindow);
		}
	}
	
	private void projectileSelected(TowerImage myTowerImage, SpriteImage imageClone) {
		projectileSlot.getChildren().removeAll(projectileSlot.getChildren());
		projectileSlot.getChildren().add(imageClone);
		myTowerImage.addProjectileImage(imageClone);
		
	}

	private void newPane(SpriteImage imageView) {
//		myPropertiesBox = new PropertiesBox(created, imageView);
		propertiesPane = new Pane();
		Button deleteButton = new Button("Back");
		deleteButton.setLayoutX(300);
		Label info = new Label("Properties here");
		info.setLayoutY(100);
		info.setFont(new Font("Arial", 30));
		deleteButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->removeButtonPressed());
		propertiesPane.getChildren().add(imageView.clone());
		propertiesPane.getChildren().add(deleteButton);
		propertiesPane.getChildren().add(myPropertiesBox);
		this.getChildren().removeAll(this.getChildren());
		this.getChildren().add(propertiesPane);
		this.getChildren().add(bottomTabPane);
	}
	
	private void removeButtonPressed() {
		this.getChildren().removeAll(this.getChildren());
		this.getChildren().add(topTabPane);
		this.getChildren().add(bottomTabPane);
	}
	
	public void addToMap(String property, String value) {
		basePropertyMap.put(property, value);
	}
} 