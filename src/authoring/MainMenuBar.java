package authoring;

import java.util.Optional;

import engine.authoring_engine.AuthoringController;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import splashScreen.ScreenDisplay;

public class MainMenuBar extends MenuBar{
	private final String RENAME = "Rename Game:";
	private final String SAVE = "Save As:";
	
	private Menu file;
	private Menu edit;
	private AuthoringController myController;
	
	public MainMenuBar(AuthoringController controller, ScreenDisplay display) {
		this.myController = controller;
		this.prefWidthProperty().bind(display.getScene().widthProperty());
		
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
		
		saveAsMenuItem.setOnAction(ActionEvent -> saveGame());
		exportMenuItem.setOnAction(ActionEvent -> exportGame());
		renameMenuItem.setOnAction(ActionEvent -> renameGame());
		file.getItems().addAll(saveAsMenuItem, exportMenuItem, renameMenuItem);
	}
	
	private Optional<String> launchInput(String prompt) {
		TextInputDialog input = new TextInputDialog();
		input.setContentText(prompt);
		input.setGraphic(null);
		input.setTitle(null);
		input.setHeaderText(null);
		return input.showAndWait();
	}
	
	private void saveGame() {
		Optional<String> saveName = launchInput(SAVE);
		if(saveName.isPresent()) {
			myController.saveGameState(saveName.get());
		}
	}
	
	private void exportGame() {
		myController.exportGame();
	}
	
	private void renameGame() {
		Optional<String> newName = launchInput(RENAME);
		if(newName.isPresent()) {
			myController.setGameName(newName.get());
		}
	}
}
