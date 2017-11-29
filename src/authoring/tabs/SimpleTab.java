package authoring.tabs;

import java.util.List;

import engine.authoring_engine.AuthoringController;
import interfaces.ClickableInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import splashScreen.ScreenDisplay;
import sprites.BackgroundObject;
import sprites.InteractiveObject;
import sprites.StaticObject;

public class SimpleTab extends ScrollPane{
	private ScreenDisplay display;
	private List<InteractiveObject> myList;
	private ListView<InteractiveObject> myListView;
	private ObservableList<InteractiveObject> items;
	private AuthoringController myController;
	
	public SimpleTab(ScreenDisplay display, AuthoringController controller, List<InteractiveObject> defaults) {
		this.display = display;
		this.myController = controller;
		addDefaultImages(defaults);
	}

	private void addDefaultImages(List<InteractiveObject> defaults) {
		myList = defaults;
		items = FXCollections.observableArrayList(myList);
		myListView = new ListView<>();
		myListView.setOnMouseClicked(e->display.listItemClicked(
      		myListView.getSelectionModel().getSelectedItem()));
		myListView.setItems(items);
		this.setContent(myListView);
	}
	
	//TODO eliminate addstatic and anddbackground if possible in favor of more general additem
	public void addStaticItem(int size, String imageString) {
		items.add(new StaticObject(size, display, imageString));
	}
	
	public void addBackgroundItem(int size, String imageString) {
		items.add(new BackgroundObject(size, display, imageString));
	}
	
	public void addItem(InteractiveObject object) {
		items.add(object);
		//TODO get this to work with properties map
//		myController.defineElement(object.getImageString(), null);
	}
}
