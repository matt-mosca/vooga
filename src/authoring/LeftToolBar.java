package authoring;

import factory.TabFactory;

import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

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
		staticTab = new StaticTab(author);
		backgroundTab = new BackgroundTab(author);
		this.getChildren().add(tabPane);
        createAndAddTabs();
	}
	
	private void createAndAddTabs() {
		tabPane.getTabs().add(tabFactory.buildTab("Static", staticTab, tabPane));
		tabPane.getTabs().add(tabFactory.buildTab("Background", backgroundTab, tabPane));
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
