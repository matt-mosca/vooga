package authoring.rightToolBar;


import java.util.ArrayList;
import java.util.List;

import authoring.AuthorInterface;
import authoring.ObjectProperties;
import factory.ButtonFactory;
import factory.TabFactory;
import interfaces.CreationInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
 
public class RightToolBar extends VBox {
	
	private TabFactory tabMaker;
	private TabPane topTabPane;
	private TabPane bottomTabPane;
	private NewSpriteTab newTower;
	private NewTroopTab newTroop;
	private NewSpriteTab newProjectile;
	private NewInventoryTower inventoryTower;
	private NewInventoryTroop inventoryTroop;
	private NewInventoryProjectile inventoryProjectile;

	
	
	public RightToolBar(CreationInterface created) {
        this.setLayoutX(680);
		this.setLayoutY(50);
	    tabMaker = new TabFactory();
	    topTabPane = new TabPane();
	    bottomTabPane = new TabPane();
	    createAndAddTabs();
	    newTower = new NewTowerTab(created);   
	    newTroop = new NewTroopTab(created); 
	    newProjectile = new NewProjectileTab(created); 
	    inventoryTower = new NewInventoryTower();
	    inventoryTroop = new NewInventoryTroop();
	    inventoryProjectile = new NewInventoryProjectile();
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
	
	private void makeTabsUnclosable() {
		for(int i = 0; i < topTabPane.getTabs().size(); i++) {
			topTabPane.getTabs().get(i).setClosable(false);
		}
		
		for(int i = 0; i < bottomTabPane.getTabs().size(); i++) {
			bottomTabPane.getTabs().get(i).setClosable(false);
		}
	}
	
	public void doSomething() {
		System.out.println("Done");
	}
} 