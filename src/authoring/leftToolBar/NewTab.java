package authoring.leftToolBar;

import interfaces.ClickableInterface;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class NewTab extends ScrollPane{
	private final String PROMPT = "Choose Type";
	
	private ClickableInterface clickable;
	private ComboBox<String> objectTypes;
	private Button addImage;
	private FileChooser fileChooser;
	private VBox items;
	private TabPane tabPane;
	private Pane toolbar;
	
	public NewTab(ClickableInterface clickable, TabPane tabs, Pane tools) {
		this.clickable = clickable;
		this.tabPane = tabs;
		this.toolbar = tools;
		this.setFitToWidth(true);
		
		objectTypes = new ComboBox<>();
		initializeOptions();
		addImage = new Button();
		initializeAddImage();
		fileChooser= new FileChooser();
		
		items = new VBox();
		items.setFillWidth(true);
		items.getChildren().add(objectTypes);
		items.getChildren().add(addImage);
		this.setContent(items);
	}

	private void initializeOptions() {
		objectTypes.setPromptText(PROMPT);
		objectTypes.setMaxWidth(Integer.MAX_VALUE);
		for(Tab tab:tabPane.getTabs()) {
			objectTypes.getItems().add(tab.getText());
		}
	}
	
	private void initializeAddImage() {
		if(objectTypes.getSelectionModel().isEmpty()) {
			return;
		}else {
			Image image = new Image(fileChooser.showOpenDialog(this.getScene().getWindow()).getName());
		}
	}

}
