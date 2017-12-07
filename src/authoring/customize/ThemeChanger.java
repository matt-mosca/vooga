package authoring.customize;

import authoring.EditDisplay;
import display.interfaces.CustomizeInterface;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import display.splashScreen.ScreenDisplay;

/**
 * Creates a button to change the background
 * 
 * @author Matt
 */
public class ThemeChanger extends ComboBox<String> {
	
	private static final int X_POS = 350;
	private static final int Y_POS = 23;
	private static final int WIDTH = 200;
	private static final String PROMPT_TEXT = "Choose a theme";
	public static final String STANDARD = "Standard";
	public static final String DARK = "Dark";
	public static final String FOREST = "Forest";
	public static final String SKY = "Sky";
	public static final String GOLD = "Gold";
	public static final String MIDNIGHT = "Midnight";
	
	public ThemeChanger(EditDisplay display) {
		this.setPrefWidth(WIDTH);
		this.setLayoutX(X_POS);
		this.setLayoutY(Y_POS);
		this.setPromptText(PROMPT_TEXT);
		String[] themes = {STANDARD, DARK, FOREST, SKY, GOLD, MIDNIGHT};
		ObservableList<String> colorList = FXCollections.observableArrayList(themes);
		ChangeListener<String> propertyHandler = (obs, old, cur) -> display.changeTheme(cur);
		this.getSelectionModel().selectedItemProperty().addListener(propertyHandler);
		this.setEditable(true);
		this.setVisibleRowCount(3);
		this.setItems(colorList);
	}
	
	public String getThemePath(String theme) {
		if(theme.equals(FOREST))
			return "authoring/resources/green.css";
		else if(theme.equals(GOLD))
			return "authoring/resources/gold.css";
		else if(theme.equals(SKY))
			return "authoring/resources/blue.css";
		else if(theme.equals(DARK))
			return "authoring/resources/dark.css";
		else if(theme.equals(MIDNIGHT))
			return "authoring/resources/darkpurple.css";
		else
			return "authoring/resources/standard.css";
	}
}
