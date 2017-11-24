package authoring;

import java.util.ArrayList;
import java.util.List;

import interfaces.ClickableInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import sprites.BackgroundObject;
import sprites.StaticObject;

/**
 * 
 * @author Matt
 */
public class LeftToolBar extends ScrollPane {
	private static final int WIDTH = 300;
	private List<StaticObject> myList;
	private ListView<StaticObject> myListView;
	private ClickableInterface myClickable;
	private StaticObject myStatic1;
	private StaticObject myStatic2;
	private StaticObject myStatic3;
	private BackgroundObject myBackground1;
	private BackgroundObject myBackground2;
	private BackgroundObject myBackground3;
	private BackgroundObject myBackground4;
	
	public LeftToolBar(ClickableInterface clickable) {
		this.setLayoutY(50);
		myClickable = clickable;
        init();
	}

	public void init() {
		createDefaultObjects();
		addToList();
        addToToolbar();
	}

	public void createDefaultObjects() {
		myStatic1 = createNewStatic(1, "tortoise.png");
		myStatic2 = createNewStatic(1, "gray_circle.png");
		myStatic3 = createNewStatic(1, "green_soldier.gif");
		myBackground1 = createNewBackground(3, "grass_small.png");
		myBackground2 = createNewBackground(3, "grass2_small.png");
		myBackground3 = createNewBackground(2, "brick_path.png");
		myBackground4 = createNewBackground(2, "stone_path1.png");
		myBackground4 = createNewBackground(3, "water_medium.png");
	}

	public void addToList() {
		myList = new ArrayList<StaticObject>();
        myList.add(myStatic1);
        myList.add(myStatic2);
        myList.add(myStatic3);
        myList.add(myBackground1);
        myList.add(myBackground2);
        myList.add(myBackground3);
        myList.add(myBackground4);
	}
	
	public void addToToolbar() {
        ObservableList<StaticObject> items = FXCollections.observableArrayList(myList);
        myListView = new ListView<StaticObject>();
        myListView.setOnMouseClicked(e->myClickable.clicked(
        		myListView.getSelectionModel().getSelectedItem()));
        myListView.setItems(items);
        this.setContent(myListView);
	}
	
	private StaticObject createNewStatic(int size, String imageString) {
		return new StaticObject(size, myClickable, imageString);
	}
	
	private BackgroundObject createNewBackground(int size, String imageString) {
		return new BackgroundObject(size, myClickable, imageString);
	}
	

	
}
