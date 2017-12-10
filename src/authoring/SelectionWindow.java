package authoring;

import java.util.List;
import java.util.Map;

import authoring.PropertiesToolBar.SpriteImage;
import engine.authoring_engine.AuthoringController;
import display.interfaces.PropertiesInterface;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SelectionWindow extends Stage {
	
	private Button yesButton;
	private Button noButton;
	private TextField enterName;
	private VBox newProject;
	private Scene newScene;
	private AuthorInterface author;
	private AuthoringController controller;
	
	public SelectionWindow(SpriteImage imageView, AuthorInterface author, AuthoringController controller) {
		this.author = author;
		this.controller = controller;
		newProject = new VBox();
		newScene = new Scene(newProject, 400, 400);
		this.setScene(newScene);
		
		noButtonPressed();
		this.show();
		createTextField();
		createComboBoxes();
		yesButton = new Button("Commit");
		noButton = new Button("Back");
		yesButton.setLayoutX(1000);
		noButton.setLayoutX(1050);
		yesButton.setLayoutY(20);
		noButton.setLayoutY(20);
		
		newProject.getChildren().add(yesButton);
		newProject.getChildren().add(noButton);
		yesButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->yesButtonPressed(imageView));
		noButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->noButtonPressed());
	}
	
	private void createTextField() {
		enterName = new TextField();
		enterName.setPromptText("Enter name");
		enterName.setLayoutX(1000);
		enterName.setLayoutY(50);
		newProject.getChildren().add(enterName);
	}
	
	private void yesButtonPressed(SpriteImage imageView) {
		imageView.setName(enterName.getText());
		author.imageSelected(imageView);
		noButtonPressed();
	}
	
	private void noButtonPressed() {
		this.hide();
	}
	
	private void createComboBoxes() {
		Map<String, List<String>> baseOptions = controller.getElementBaseConfigurationOptions();
		for (String s : baseOptions.keySet()) {
			BaseComboBox newComboBox = new BaseComboBox(s, baseOptions.get(s), author);
			newComboBox.setLayoutY(200);
			newProject.getChildren().add(newComboBox);
		}
	}

}
