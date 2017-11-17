package authoring;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;

public class NewSpriteTab {
	private ScrollPane troopArea;
	private List<ImageView> newTroopImages;
//	private TableView<ImageView> table;
	private ObservableList<ImageView> troops;
	private ListView<ImageView> list;
	ResourceBundle spriteImages;
	
	public NewSpriteTab() {
		newTroopImages = new ArrayList<ImageView>();
//		table = new TableView<ImageView>();
		list = new ListView<ImageView>();
		troops = FXCollections.observableArrayList(newTroopImages);
		troopArea = new ScrollPane();
//		spriteImages = ResourceBundle.getBundle("");
		list.setItems(troops);
//		table.setItems(troops);
//      table.getColumns().addAll(firstCol, lastCol)
		troopArea.setContent(list);
	}
	
	public void attach(Tab newTroopTab) {
		newTroopTab.setContent(troopArea);
	}
	
	private void addDefaultImages() {
		//AccessResourceFiles
	}
}
