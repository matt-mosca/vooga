package authoring.tabs;

import java.util.List;

import interfaces.ClickableInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import splashScreen.ScreenDisplay;
import sprites.BackgroundObject;
import sprites.StaticObject;

public class SimpleTab extends ScrollPane{
	private ScreenDisplay display;
	private List<StaticObject> myList;
	private ListView<StaticObject> myListView;
	private ObservableList<StaticObject> items;
	
	public SimpleTab(ScreenDisplay display, List<StaticObject> defaults) {
		this.display = display;
		addDefaultImages(defaults);
	}

	private void addDefaultImages(List<StaticObject> defaults) {
		myList = defaults;
		items = FXCollections.observableArrayList(myList);
		myListView = new ListView<>();
		myListView.setOnMouseClicked(e->display.launchCreateButton(
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
	
	public void addItem(StaticObject object) {
		items.add(object);
	}
}
