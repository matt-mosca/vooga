package authoring;

import authoring.rightToolBar.SpriteImage;
import interfaces.PropertiesInterface;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.Main;

public class SelectionWindow extends Stage {
	
	private Button yesButton;
	private Button noButton;
	private Label optionLabel;
	private TextField enterName;
	private VBox newProject;
	private Scene newScene;
	private AuthorInterface author;
	
	public SelectionWindow(SpriteImage imageView, AuthorInterface author) {
		this.author = author;
		newProject = new VBox();
		newScene = new Scene(newProject, 400, 400);
		this.setScene(newScene);
		this.show();
		
		noButtonPressed();
		createTextField();
		optionLabel = new Label("Do you want to add this sprite\nto inventory?");
		yesButton = new Button("Yes");
		noButton = new Button("No");
		yesButton.setLayoutX(1000);
		noButton.setLayoutX(1050);
		optionLabel.setLayoutX(700);
		yesButton.setLayoutY(20);
		noButton.setLayoutY(20);
		optionLabel.setLayoutY(20);
		
		newProject.getChildren().add(yesButton);
		newProject.getChildren().add(noButton);
		newProject.getChildren().add(optionLabel);
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
		newProject.getChildren().removeAll(newProject.getChildren());
	}

}
