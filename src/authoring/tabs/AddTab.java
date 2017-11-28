package authoring.tabs;

import java.io.File;

import interfaces.ClickableInterface;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public abstract class AddTab extends ScrollPane{
	private final String PROMPT = "Choose Type";
	private final String ADD_PROMPT = "Click to add image";
	
	private Button addImage;
	private FileChooser fileChooser;
	private VBox items;
	protected ClickableInterface clickable;
	protected ComboBox<String> objectTypes;
	protected TabPane tabPane;
	
	public AddTab(ClickableInterface clickable, TabPane tabs) {
		this.clickable = clickable;
		this.tabPane = tabs;
		this.setFitToWidth(true);
		
		objectTypes = new ComboBox<>();
		initializeOptions();
		addImage = new Button();
		addImage.setText(ADD_PROMPT);
		addImage.setOnAction((ActionEvent e) -> addImage());
		fileChooser= new FileChooser();
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
		
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
	
	private void addImage() {
		if(objectTypes.getSelectionModel().isEmpty()) {
			//Add in error window here
			return;
		}else {
			File file = fileChooser.showOpenDialog(this.getScene().getWindow());
			if(file != null) {
				String tabName = tabPane.getTabs().get(objectTypes.getSelectionModel().getSelectedIndex()).getId();
				createResource(file, tabName);
			}
		}
	}

	
	protected abstract void createResource(File file, String tabName);

}
