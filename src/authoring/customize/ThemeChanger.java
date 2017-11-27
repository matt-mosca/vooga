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
public class ThemeChanger extends ComboBox<String> {
	
	private static final int Y_POS = 10;
	private static final int WIDTH = 200;
	private static final String PROMPT_TEXT = "Choose a theme";
	public static final String STANDARD = "Standard";
	public static final String DARK = "Dark";
	public static final String FOREST = "Forest";
	public static final String SKY = "Sky";
	public static final String GOLD = "Gold";
	public static final String MIDNIGHT = "Midnight";
	
	public ThemeChanger(CustomizeInterface customize) {
		this.setPrefWidth(WIDTH);
		this.setLayoutY(Y_POS);
		this.setPromptText(PROMPT_TEXT);
		String[] themes = {STANDARD, DARK, FOREST, SKY, GOLD, MIDNIGHT};
		ObservableList<String> colorList = FXCollections.observableArrayList(themes);
		ChangeListener<String> propertyHandler = (obs, old, cur) -> customize.changeTheme(cur);
		this.getSelectionModel().selectedItemProperty().addListener(propertyHandler);
		this.setEditable(true);
		this.setVisibleRowCount(3);
		this.setItems(colorList);
	}
}
