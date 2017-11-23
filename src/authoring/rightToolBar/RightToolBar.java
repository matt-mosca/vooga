package authoring.rightToolBar;


import java.util.ArrayList;
import java.util.List;

import authoring.AuthorInterface;
import authoring.ObjectProperties;
import factory.ButtonFactory;
import factory.TabFactory;
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
	
	private TableView<ObjectProperties> table;
	private AuthorInterface myAuthor;
	private TabFactory tabMaker;
	private TabPane topTabPane;
	private TabPane bottomTabPane;
	private NewSpriteTab newTower;
	private NewTroopTab newTroop;
	private NewSpriteTab newProjectile;
	
	public RightToolBar(AuthorInterface author) {
		this.setLayoutY(50);
		myAuthor = author;
	    tabMaker = new TabFactory();
	    topTabPane = new TabPane();
	    bottomTabPane = new TabPane();
	    createAndAddTabs();
	    
	    newTower = new NewTowerTab();   
	    newTroop = new NewTroopTab(); 
	    newProjectile = new NewProjectileTab(); 
   
//        table.setEditable(true);
        this.setLayoutX(680);
//      this.getChildren().add(table);
        this.getChildren().add(topTabPane);
        this.getChildren().add(bottomTabPane);
        topTabPane.getTabs().get(0).setContent(table);
        //newTroops.attach(tabList.get(0);
        
        newTower.attach(topTabPane.getTabs().get(0));
        newTroop.attach(topTabPane.getTabs().get(1));
        newProjectile.attach(topTabPane.getTabs().get(2));
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