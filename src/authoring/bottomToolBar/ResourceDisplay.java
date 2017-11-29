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
		
		update();
	}

	private void changeResourceVal() {
		this.getChildren().clear();
		TextField name = new TextField("Enter the name of your resource.");
		TextField value = new TextField("How much of this resource would you like in this level?");
		Button enter = new Button("add!");
		enter.setOnAction(e->{
			if (myController.getResourceEndowments().containsKey(name.getText())) {
				Double d = myController.getResourceEndowments().get(name.getText());
				d = Double.parseDouble(value.getText());
			}
			else {
				resourceEndowments.put(name.getText(), Double.parseDouble(value.getText()));
			}
		});
		this.getChildren().clear();
		update();
	}

	private void update() {
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
