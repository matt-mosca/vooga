package authoring;


import java.util.ArrayList;
import java.util.List;

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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
 
public class RightToolBar extends VBox {
	
	private TableView<ObjectProperties> table;
	private ObjectProperties[] dataArray;
	private ObservableList<ObjectProperties> data;
	private Label label;
	private Button addButton;
	private TextField addFirst;
	private TextField addLast;
	private TableColumn<ObjectProperties, String> firstCol;
	private TableColumn<ObjectProperties, String> lastCol;
	private AuthorInterface myAuthor;
	ButtonFactory buttonMaker;
	TabFactory tabMaker;
	TabPane topTabPane;
	TabPane bottomTabPane;
	List<Tab> tabList;
	
	public RightToolBar(AuthorInterface author) {
		this.setLayoutY(50);
		myAuthor = author;
	    table = new TableView<ObjectProperties>();
	    dataArray = new ObjectProperties[] {new ObjectProperties("Tower 1", "1"),
	            new ObjectProperties("Tower 2", "2"),
	            new ObjectProperties("Tower 3", "3"),
	            new ObjectProperties("Soldier 1", "20"),
	            new ObjectProperties("Solider 2", "50")};
	    data = FXCollections.observableArrayList(dataArray);
	    
	    buttonMaker = new ButtonFactory();
	    tabMaker = new TabFactory();
	    tabList = new ArrayList<Tab>();
	    topTabPane = new TabPane();
	    bottomTabPane = new TabPane();
	    createTabs();
	    addTabsToPane();
	            
  
        label = new Label("Table");
 
//        table.setEditable(true);
 
        firstCol = new TableColumn<ObjectProperties, String>("Object");
        firstCol.setCellValueFactory(
                new PropertyValueFactory<ObjectProperties, String>("first"));
 
        lastCol = new TableColumn<ObjectProperties, String>("Property 1");
        lastCol.setCellValueFactory(
                new PropertyValueFactory<ObjectProperties, String>("last"));
 
        table.setItems(data);
        table.getColumns().addAll(firstCol, lastCol);
 
        addFirst = new TextField();
        addFirst.setPromptText("First");
        addFirst.setMaxWidth(firstCol.getPrefWidth());
        addLast = new TextField();
        addLast.setMaxWidth(lastCol.getPrefWidth());
        addLast.setPromptText("Last");
        this.setLayoutX(680);
//      this.getChildren().add(table);
        this.getChildren().add(topTabPane);
        this.getChildren().add(bottomTabPane);
        tabList.get(0).setContent(table);
        //newTroops.attach(tabList.get(0);
        
        addButton = buttonMaker.buildDefaultTextButton("Add", e -> addData());

        this.getChildren().addAll(addFirst, addLast, addButton);
        this.setSpacing(3);
    }
	
	private void addData() {
    	data.add(new ObjectProperties(addFirst.getText(), addLast.getText()));
        addFirst.clear();
        addLast.clear();
    }
	
	public void updateInfo(String first, String second) {
        data.add(new ObjectProperties(first, second));
	}
		
	private void createTabs() {
		tabList.add(tabMaker.buildTabWithoutContent("New Tower", topTabPane));
		tabList.add(tabMaker.buildTabWithoutContent("New Troop", topTabPane));
		tabList.add(tabMaker.buildTabWithoutContent("New Projectile", topTabPane));
		tabList.add(tabMaker.buildTabWithoutContent("Inventory Towers", bottomTabPane));
		tabList.add(tabMaker.buildTabWithoutContent("Inventory Troops", bottomTabPane));
		tabList.add(tabMaker.buildTabWithoutContent("Inventory Projectile", bottomTabPane));
	}
	
	private void addTabsToPane() {
		for(int i = 0; i < tabList.size() / 2; i++) {
			topTabPane.getTabs().add(tabList.get(i));
		}
		for(int i = tabList.size() / 2; i < tabList.size(); i++) {
			bottomTabPane.getTabs().add(tabList.get(i));
		}
	}
 
} 