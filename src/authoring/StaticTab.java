package authoring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import authoring.rightToolBar.NewSpriteTab;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import sprites.StaticObject;

public class StaticTab extends NewSpriteTab{
	private AuthorInterface author;
	private List<StaticObject> myList;
	private ListView<StaticObject> myListView;

	public StaticTab(AuthorInterface ai) {
		super(ai);
		author = ai;
		addDefaultImages();
	}

	@Override
	protected void addDefaultImages() {
		myList = new ArrayList<>(Arrays.asList(createNewStatic(1, "tortoise.png"),
				createNewStatic(1, "gray_circle.png"),createNewStatic(1, "green_soldier.gif")));
		
		ObservableList<StaticObject> items = FXCollections.observableArrayList(myList);
		myListView = new ListView<>();
		myListView.setOnMouseClicked(e->author.clicked(
      		myListView.getSelectionModel().getSelectedItem()));
		myListView.setItems(items);
		this.setContent(myListView);
	}
	
	protected StaticObject createNewStatic(int size, String imageString) {
		return new StaticObject(size, author, imageString);
	}

}
