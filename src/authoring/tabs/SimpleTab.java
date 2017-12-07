package authoring.tabs;

import java.util.ArrayList;
import java.util.List;

import interfaces.ClickableInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import splashScreen.ScreenDisplay;
import sprites.BackgroundObject;
import sprites.InteractiveObject;
import sprites.StaticObject;

public class SimpleTab extends ScrollPane{
	private ScreenDisplay display;
	private List<ImageView> myList;
	protected ListView<ImageView> myListView;
	private ObservableList<ImageView> items;
	
	public SimpleTab(ScreenDisplay display, List<ImageView> defaults) {
		this.display = display;
		addDefaultImages(defaults);
	}

	private void addDefaultImages(List<ImageView> defaults) {
		myList = defaults;
		items = FXCollections.observableArrayList(myList);
		myListView = new ListView<>();
		addHandler();
		myListView.setItems(items);
		this.setContent(myListView);
	}
	
	protected void addHandler() {
		myListView.setOnMouseClicked(e->display.listItemClicked(
	      		myListView.getSelectionModel().getSelectedItem()));
	}
	
	public void addItem(ImageView object) {
		items.add(object);
	}
	
	public List<ImageView> getImages(){
		return new ArrayList<>(myList);
	}
}
