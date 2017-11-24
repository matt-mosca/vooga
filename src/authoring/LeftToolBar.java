package authoring;

import java.util.ArrayList;
import java.util.List;

import factory.TabFactory;
import interfaces.ClickableInterface;
import interfaces.CreationInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import sprites.BackgroundObject;
import sprites.StaticObject;

/**
 * 
 * @author Matt
 */
public class LeftToolBar extends VBox {
	private static final int WIDTH = 300;
	private AuthorInterface author;
	private TabPane tabPane;
	private TabFactory tabFactory;
	private StaticTab staticTab;
	private BackgroundTab backgroundTab;
	
	public LeftToolBar(AuthorInterface ai) {
		this.setLayoutY(50);
		author = ai;
		tabPane = new TabPane();
		tabFactory = new TabFactory();
		this.getChildren().add(tabPane);
        createAndAddTabs();
	}
	
	private void createAndAddTabs() {
		tabPane.getTabs().add(tabFactory.buildTabWithoutContent("Static", tabPane));
		tabPane.getTabs().add(tabFactory.buildTabWithoutContent("Background", tabPane));
		staticTab = new StaticTab(author);
		backgroundTab = new BackgroundTab(author);
		staticTab.attach(tabPane.getTabs().get(0));
		backgroundTab.attach(tabPane.getTabs().get(1));
		makeTabsUnclosable();
	}
	
	private void makeTabsUnclosable() {
		for(int i = 0; i < tabPane.getTabs().size(); i++) {
			tabPane.getTabs().get(i).setClosable(false);
		}
	}

//	public void addToList() {
//		myList = new ArrayList<StaticObject>();
//        myList.add(myStatic1);
//        myList.add(myStatic2);
//        myList.add(myStatic3);
//        myList.add(myBackground1);
//        myList.add(myBackground2);
//        myList.add(myBackground3);
//        myList.add(myBackground4);
//	}
	
//	public void addToToolbar() {
//        ObservableList<StaticObject> items = FXCollections.observableArrayList(myList);
//        myListView = new ListView<StaticObject>();
//        myListView.setOnMouseClicked(e->myClickable.clicked(
//        		myListView.getSelectionModel().getSelectedItem()));
//        myListView.setItems(items);
//        this.setContent(myListView);
//	}
}
