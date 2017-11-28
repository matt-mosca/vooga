package authoring;

import engine.authoring_engine.AuthoringController;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class MainMenuBar extends MenuBar{
	private final String RENAME = "Rename Game:";
	private final String SAVE = "Save As:";
	
	private Menu file;
	private Menu edit;
	private AuthoringController myController;
	
	public MainMenuBar(AuthoringController controller) {
		this.prefWidthProperty().bind(this.getScene().widthProperty());
		this.myController = controller;
		
		createFileMenu();
		createEditMenu();
		
		this.getMenus().addAll(file, edit);
	}

	private void createEditMenu() {
		edit = new Menu("Edit");
	}

	private void createFileMenu() {
		file = new Menu("File");
		MenuItem saveAsMenuItem = new MenuItem("Save As");
		MenuItem exportMenuItem = new MenuItem("Export");
		MenuItem renameMenuItem = new MenuItem("Rename");
		
		saveAsMenuItem.setOnAction(ActionEvent -> myController.saveGameState(launchInput(SAVE)));
		exportMenuItem.setOnAction(ActionEvent -> myController.exportGame());
		renameMenuItem.setOnAction(ActionEvent -> myController.setGameName(launchInput(RENAME)));
		file.getItems().addAll(saveAsMenuItem, exportMenuItem, renameMenuItem);
	}
	
	private String launchInput(String prompt) {
		return null;
	}
}
