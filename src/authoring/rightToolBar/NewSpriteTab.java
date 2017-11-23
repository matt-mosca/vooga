package authoring.rightToolBar;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import interfaces.CreationInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public abstract class NewSpriteTab extends ScrollPane {
	
	public static final double DISPLAY_SIZE = 60;
	
	private List<ImageView> newSpriteImages;
//	private TableView<ImageView> table;
	private ObservableList<ImageView> spritesView;
	private ListView<ImageView> list;
	private CreationInterface myCreated;
	private ResourceBundle images;
	private SpriteImage spriteImage;
	
	public NewSpriteTab(CreationInterface created) {
		myCreated = created;
		newSpriteImages = new ArrayList<ImageView>();
		list = new ListView<ImageView>();
		spritesView = FXCollections.observableArrayList(newSpriteImages);
		list.setItems(spritesView);
		this.setContent(list);
		list.setOnMouseClicked(e->myCreated.clicked(
        		list.getSelectionModel().getSelectedItem()));
	}
	
	public void attach(Tab newTroopTab) {
		newTroopTab.setContent(this);
	}
	
	protected List<ImageView> getImages() {
		return newSpriteImages;
	}
	
	protected void addImage(SpriteImage spriteImage) {
//		spriteImage = new SpriteImage(DISPLAY_SIZE, imageName);
//		Image image = new Image(getClass().getClassLoader().getResourceAsStream(imageName));
//		ImageView spriteImage = new ImageView(image);
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
	
	public void tabClicked() {
		list.getSelectionModel().getSelectedItem().addEventHandler
		(MouseEvent.MOUSE_CLICKED, e->myCreated.clicked(
				list.getSelectionModel().getSelectedItem()));
	}
	
	protected abstract void addDefaultImages();
}
