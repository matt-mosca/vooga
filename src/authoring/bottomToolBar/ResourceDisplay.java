package authoring.bottomToolBar;

import java.util.HashMap;
import java.util.Map;

import engine.authoring_engine.AuthoringController;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ResourceDisplay extends VBox{
	private Map<String, Double> resourceEndowments;
	private AuthoringController myController;
	private Button editResources;
	
	public ResourceDisplay(AuthoringController controller){
		myController = controller;
		resourceEndowments = new HashMap<>();
		editResources = new Button("Edit or add a resource.");
		editResources.setOnAction(e->changeResourceVal());
		
		this.getChildren().add(editResources);
		update();
	}

	private void changeResourceVal() {
		this.getChildren().clear();
		TextField name = new TextField("Name");
		TextField value = new TextField("Value");
		Button enter = new Button("add!");
		enter.setOnAction(e->{
			if (myController.getResourceEndowments().containsKey(name.getText())) {
				Double d = myController.getResourceEndowments().get(name.getText());
				d = Double.parseDouble(value.getText());
			}
			else {
				resourceEndowments.put(name.getText(), Double.parseDouble(value.getText()));
			}
			this.getChildren().clear();
			this.getChildren().add(editResources);
			update();
		});
		this.getChildren().addAll(name, value, enter);
		
	}

	private void update() {
		myController.setResourceEndowments(resourceEndowments); //hopefully you're doing some modifications 
		//and passing me a differnent map for the next step.
		Map<String, Double> resources = myController.getResourceEndowments();
		Label l = new Label("These are your current resources.");
		this.getChildren().add(l);
		for(String s : resources.keySet()) {
			Label l1 = new Label(s + ": " + resources.get(s));
			this.getChildren().add(l1);
		}
		
	}

	public VBox getRoot() {
		return this;
	}
}
