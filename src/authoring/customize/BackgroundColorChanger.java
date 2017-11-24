package authoring.customize;

import interfaces.CustomizeInterface;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

/**
 * Creates a button to change the background
 * 
 * @author Matt
 */
public class BackgroundColorChanger extends ComboBox<String> {
	
	private static final int Y_POS = 500;
	private static final int WIDTH = 200;
	private static final String PROMPT_TEXT = "Choose a background color";
	private static final String[] COLORS = {"white", "blue", "orange", "yellow", "green", "purple", "grey", "red"};
	
	public BackgroundColorChanger(CustomizeInterface customize) {
		this.setPrefWidth(WIDTH);
		this.setLayoutY(Y_POS);
		this.setPromptText(PROMPT_TEXT);
		ObservableList<String> colorList = FXCollections.observableArrayList(COLORS);
		ChangeListener<String> propertyHandler = (obs, old, cur) -> customize.changeBackground(cur);
		this.getSelectionModel().selectedItemProperty().addListener(propertyHandler);
		this.setEditable(true);
		this.setVisibleRowCount(3);
		this.setItems(colorList);
	}
}
