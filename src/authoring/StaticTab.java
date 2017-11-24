package authoring;

import java.util.ArrayList;
import java.util.List;

import authoring.rightToolBar.NewSpriteTab;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import sprites.StaticObject;

public class StaticTab extends NewSpriteTab{
	private StaticObject myStatic1;
	private StaticObject myStatic2;
	private StaticObject myStatic3;
	private AuthorInterface author;
	private List<StaticObject> myList;
	private ListView<StaticObject> myListView;

	public StaticTab(AuthorInterface ai) {
		super(ai);
		author = ai;
	}

	@Override
	protected void addDefaultImages() {
		myStatic1 = createNewStatic(1, "tortoise.png");
		myStatic2 = createNewStatic(1, "gray_circle.png");
		myStatic3 = createNewStatic(1, "green_soldier.gif");
		
		myList = new ArrayList<>();
		myList.add(myStatic1);
		myList.add(myStatic2);
		myList.add(myStatic3);
		
		ObservableList<StaticObject> items = FXCollections.observableArrayList(myList);
		myListView = new ListView<>();
		myListView.setOnMouseClicked(e->author.clicked(
      		myListView.getSelectionModel().getSelectedItem()));
		myListView.setItems(items);
		this.setContent(myListView);
	}
	
	private StaticObject createNewStatic(int size, String imageString) {
		return new StaticObject(size, author, imageString);
	}

}
