package authoring.rightToolBar;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import authoring.AuthorInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public abstract class NewInventoryTab extends ScrollPane {
	
	public static final double DISPLAY_SIZE = 60;
	
	private List<ImageView> newSpriteImages;
//	private TableView<ImageView> table;
	private ObservableList<ImageView> spritesView;
	private ListView<ImageView> list;
	private AuthorInterface myAuthor;
	ResourceBundle images;
	
	public NewInventoryTab() {
//		myAuthor = author;
		newSpriteImages = new ArrayList<ImageView>();
//		table = new TableView<ImageView>();
		list = new ListView<ImageView>();
		spritesView = FXCollections.observableArrayList(newSpriteImages);
//		images = ResourceBundle.getBundle("");
		list.setItems(spritesView);
//		table.setItems(troops);
//      table.getColumns().addAll(firstCol, lastCol)
		this.setContent(list);
	}
	
	public void attach(Tab newTroopTab) {
		newTroopTab.setContent(this);
	}
	
	protected List<ImageView> getImages() {
		return newSpriteImages;
	}
	
	protected void addImage(String imageName) {
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(imageName));
		ImageView spriteImage = new ImageView(image);
		double spriteWidth = spriteImage.getBoundsInLocal().getWidth();
		double spriteHeight = spriteImage.getBoundsInLocal().getHeight();
		double maxDimension = Math.max(spriteWidth, spriteHeight);
		double scaleValue = maxDimension / DISPLAY_SIZE;
		spriteImage.setFitWidth(spriteWidth / scaleValue);
		spriteImage.setFitHeight(spriteHeight / scaleValue);
		newSpriteImages.add(spriteImage);
	}
	
	protected void updateImages() {
		spritesView = FXCollections.observableArrayList(newSpriteImages);
		list.setItems(spritesView);
		this.setContent(list);
	}
	
//	public void tabClicked(InventoryTab myInventory) {
//		list.getSelectionModel().getSelectedItem().addEventHandler
//		(MouseEvent.MOUSE_CLICKED, e->myAuthor.newTowerSelected(
//				list.getSelectionModel().getSelectedItem()));
//	}
	
	protected abstract void addDefaultImages();
}
