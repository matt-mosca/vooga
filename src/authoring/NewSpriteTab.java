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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class NewSpriteTab {
	private ScrollPane spriteArea;
	private List<ImageView> newSpriteImages;
//	private TableView<ImageView> table;
	private ObservableList<ImageView> spritesView;
	private ListView<ImageView> list;
	ResourceBundle images;
	
	public NewSpriteTab() {
		newSpriteImages = new ArrayList<ImageView>();
//		table = new TableView<ImageView>();
		list = new ListView<ImageView>();
		spritesView = FXCollections.observableArrayList(newSpriteImages);
		spriteArea = new ScrollPane();
//		images = ResourceBundle.getBundle("");
		list.setItems(spritesView);
//		table.setItems(troops);
//      table.getColumns().addAll(firstCol, lastCol)
		spriteArea.setContent(list);
	}
	
	public void attach(Tab newTroopTab) {
		newTroopTab.setContent(spriteArea);
	}
	
	protected List<ImageView> getImages() {
		return newSpriteImages;
	}
	
	protected void addImage(String imageName) {
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(imageName));
		ImageView spriteImage = new ImageView(image);
		newSpriteImages.add(spriteImage);
	}
	
	protected void updateImages() {
		spritesView = FXCollections.observableArrayList(newSpriteImages);
		list.setItems(spritesView);
		spriteArea.setContent(list);
	}
	
	/**
	 * 
	 */
	protected abstract void addDefaultImages();
}
