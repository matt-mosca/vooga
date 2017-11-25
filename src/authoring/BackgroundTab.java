package authoring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import authoring.rightToolBar.NewSpriteTab;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import sprites.BackgroundObject;
import sprites.StaticObject;

public class BackgroundTab extends NewSpriteTab{
	private AuthorInterface author; 
	private List<StaticObject> myList;
	private ListView<StaticObject> myListView;
	
	public BackgroundTab(AuthorInterface ai) {
		super(ai);
		author = ai;
		addDefaultImages();
	}

	@Override
	protected void addDefaultImages() {
		myList = new ArrayList<>(Arrays.asList(createNewBackground(3, "grass_small.png"),
				createNewBackground(3, "grass2_small.png"),createNewBackground(2, "brick_path.png"),
				createNewBackground(2, "stone_path1.png"), createNewBackground(3, "water_medium.png")));
		
		ObservableList<StaticObject> items = FXCollections.observableArrayList(myList);
		myListView = new ListView<>();
		myListView.setOnMouseClicked(e->author.clicked(
      		myListView.getSelectionModel().getSelectedItem()));
		myListView.setItems(items);
		this.setContent(myListView);
	}
	
	private BackgroundObject createNewBackground(int size, String imageString) {
		return new BackgroundObject(size, author, imageString);
	}

}
