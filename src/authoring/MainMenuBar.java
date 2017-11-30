package authoring;

import java.io.File;
import java.util.Optional;

import authoring.tabs.SaveDialog;
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
	private ScreenDisplay myDisplay;
	private AuthoringController myController;
	
	public MainMenuBar(ScreenDisplay display, AuthoringController controller) {
		this.prefWidthProperty().bind(display.getScene().widthProperty());
		myDisplay = display;
		myController = controller;
		
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
//		Optional<String> saveName = launchInput(SAVE);
//		if(saveName.isPresent()) {
//			myDisplay.save(saveName.get());
//		}
		
		File saveFile = SaveDialog.SaveLocation(getScene());
		if(saveFile != null) {
			myDisplay.save(saveFile);
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
