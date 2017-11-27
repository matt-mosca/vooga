package authoring.rightToolBar;


import java.util.ArrayList;
import java.util.List;

import authoring.AuthorInterface;
import authoring.ObjectProperties;
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
	private Rectangle projectileSlot;
	private Button deleteButton;
	private final int X_LAYOUT = 680;
	private final int Y_LAYOUT = 50;

	
	public RightToolBar(CreationInterface created) {
        this.setLayoutX(X_LAYOUT);
		this.setLayoutY(Y_LAYOUT);
	    tabMaker = new TabFactory();
	    topTabPane = new TabPane();
	    bottomTabPane = new TabPane();
	    topTabPane.setPrefHeight(250);
	    bottomTabPane.setPrefHeight(250);
	    createAndAddTabs();
	    newTower = new NewTowerTab(created);   
	    newTroop = new NewTroopTab(created); 
	    newProjectile = new NewProjectileTab(created); 
	    inventoryTower = new NewInventoryTower(this);
	    inventoryTroop = new NewInventoryTroop(this);
	    inventoryProjectile = new NewInventoryProjectile(this);
	    myPropertiesBox = new PropertiesBox(created);
	    myNewButton = new AddNewButton(created);
        this.getChildren().add(topTabPane);
        this.getChildren().add(bottomTabPane);
        
        newTower.attach(topTabPane.getTabs().get(0));
        newTroop.attach(topTabPane.getTabs().get(1));
        newProjectile.attach(topTabPane.getTabs().get(2));
        inventoryTower.attach(bottomTabPane.getTabs().get(0));
        inventoryTroop.attach(bottomTabPane.getTabs().get(1));
        inventoryProjectile.attach(bottomTabPane.getTabs().get(2));
    }
		
	private void createAndAddTabs() {
		topTabPane.getTabs().add(tabMaker.buildTabWithoutContent("New Tower", topTabPane));
		topTabPane.getTabs().add(tabMaker.buildTabWithoutContent("New Troop", topTabPane));
		topTabPane.getTabs().add(tabMaker.buildTabWithoutContent("New Projectile", topTabPane));
		bottomTabPane.getTabs().add(tabMaker.buildTabWithoutContent("Inventory Towers", bottomTabPane));
		bottomTabPane.getTabs().add(tabMaker.buildTabWithoutContent("Inventory Troops", bottomTabPane));
		bottomTabPane.getTabs().add(tabMaker.buildTabWithoutContent("Inventory Projectile", bottomTabPane));
		makeTabsUnclosable();
	}
	
	public void imageSelected(SpriteImage myImageView) {
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
		if (imageView instanceof TowerImage) newPaneWithProjectileSlot(imageView);
		if (imageView instanceof TroopImage) newPane(imageView);
	}
	
	private void newPaneWithProjectileSlot(SpriteImage imageView) {
		projectileLabel = new Label("Click to\nChoose a\nprojectile");
		projectileLabel.setLayoutY(90);
		projectileSlot = new Rectangle();
		projectileSlot.setWidth(80);
		projectileSlot.setHeight(80);
		projectileSlot.setLayoutY(150);
		projectileSlot.addEventHandler(MouseEvent.MOUSE_CLICKED, e->newProjectilesWindow());
		propertiesPane = new Pane();
		deleteButton = new Button("Back");
		deleteButton.setLayoutX(370);
		Label info = new Label("Properties here");
		info.setLayoutY(100);
		info.setFont(new Font("Arial", 30));
		myPropertiesBox.setLayoutX(100);
		deleteButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->removeButtonPressed());
		propertiesPane.getChildren().add(imageView.clone());
		propertiesPane.getChildren().add(deleteButton);
		propertiesPane.getChildren().add(myPropertiesBox);
		propertiesPane.getChildren().add(projectileLabel);
		propertiesPane.getChildren().add(projectileSlot);
		this.getChildren().removeAll(this.getChildren());
		this.getChildren().add(propertiesPane);
		this.getChildren().add(bottomTabPane);

	}
	
	private void newProjectilesWindow() {
		ScrollPane projectilesWindow = new ScrollPane();
		ListView<SpriteImage> projectilesView = new ListView<SpriteImage>();
		ObservableList<SpriteImage> items =FXCollections.observableArrayList(inventoryProjectile.getImages());
        projectilesView.setItems(items);
        projectilesView.getSelectionModel();
        projectilesWindow.setContent(projectilesView);
        projectilesWindow.setLayoutX(100);
        projectilesWindow.setPrefHeight(250);
        propertiesPane.getChildren().remove(myPropertiesBox);
        propertiesPane.getChildren().add(projectilesWindow);
	}

	private void newPane(SpriteImage imageView) {
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
} 