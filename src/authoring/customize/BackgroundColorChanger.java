package authoring.customize;

import display.interfaces.CustomizeInterface;
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
	
	private final int Y_POS = 500;
	private final int WIDTH = 200;
	private final int VISIBLE_ROW_COUNT = 3;
	private final String PROMPT_TEXT = "Choose a background color";
	private final String[] COLORS = {"white", "blue", "orange", "yellow", "green", "purple", "grey", "red"};
	private final boolean IS_EDITABLE = true;
	
	public BackgroundColorChanger(CustomizeInterface customize) {
		this.setPrefWidth(WIDTH);
		this.setLayoutY(Y_POS);
		this.setPromptText(PROMPT_TEXT);
		ObservableList<String> colorList = FXCollections.observableArrayList(COLORS);
		ChangeListener<String> propertyHandler = (obs, old, cur) -> customize.changeColor(cur);
		this.getSelectionModel().selectedItemProperty().addListener(propertyHandler);
		this.setEditable(IS_EDITABLE);
		this.setVisibleRowCount(VISIBLE_ROW_COUNT);
		this.setItems(colorList);
	}
}
