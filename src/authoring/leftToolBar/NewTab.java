package authoring.leftToolBar;

import java.io.File;
import java.net.MalformedURLException;

import interfaces.ClickableInterface;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class NewTab extends ScrollPane{
	private final String PROMPT = "Choose Type";
	private final String ADD_PROMPT = "Click to add image";
	
	private ClickableInterface clickable;
	private ComboBox<String> objectTypes;
	private Button addImage;
	private FileChooser fileChooser;
	private VBox items;
	private TabPane tabPane;
	
	public NewTab(ClickableInterface clickable, TabPane tabs) {
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
			//Currently a problem adding to list, also missing deciding which tab to add to
			File file = fileChooser.showOpenDialog(this.getScene().getWindow());
			SimpleTab tab = (SimpleTab) tabPane.getTabs().get(0).getContent();
			System.out.println(file.getPath());
//			tab.addBackgroundItem(1, file.getPath());
		}
	}

}
