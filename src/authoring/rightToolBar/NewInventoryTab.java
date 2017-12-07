package authoring.rightToolBar;

import java.util.ArrayList;
import java.util.List;

import interfaces.PropertiesInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;

public abstract class NewInventoryTab extends ScrollPane {
	
	public static final double DISPLAY_SIZE = 60;
	
	private List<SpriteImage> newInventoryImages;
	private ObservableList<SpriteImage> inventoryView;
	private ListView<SpriteImage> list;
	
	/**
	 * @deprecated
	 * @param properties
	 */
	public NewInventoryTab(PropertiesInterface properties) {
		newInventoryImages = new ArrayList<SpriteImage>();
		list = new ListView<SpriteImage>();
		inventoryView = FXCollections.observableArrayList(newInventoryImages);
		list.setItems(inventoryView);
		this.setContent(list);
		list.setOnMouseClicked(e->properties.clicked(
        		list.getSelectionModel().getSelectedItem()));
		
	}
	
	public void attach(Tab newTroopTab) {
		newTroopTab.setContent(this);
	}
	
	protected List<SpriteImage> getImages() {
		return newInventoryImages;
	}
	
	protected void addImage(SpriteImage spriteImage) {
		spriteImage.resize(DISPLAY_SIZE);
		newInventoryImages.add(spriteImage);
	}
	
	protected void updateImages() {
		inventoryView = FXCollections.observableArrayList(newInventoryImages);
		list.setItems(inventoryView);
		this.setContent(list);
	}
	
	protected abstract void addNewImage(SpriteImage spriteImage);

}
