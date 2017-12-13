package authoring.LevelToolBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import display.factory.TabFactory;
import engine.authoring_engine.AuthoringController;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ResourceDisplay extends VBox{
	private TabPane resourceTabs;
	private List<ResourceTab> resources;
	private Map<String, Double> resourceEndowments;
	private AuthoringController myController;
	private Button editResources;
	private TabFactory tabMaker;
	
	public ResourceDisplay(AuthoringController controller){
		myController = controller;
		resourceEndowments = new HashMap<>();
		
		editResources = new Button("Edit or add a resource.");
		resourceTabs = new TabPane();
		resources = new ArrayList<ResourceTab>();
		editResources.setOnAction(e->changeResourceVal());
		
		this.getChildren().add(editResources);
		this.getChildren().add(resourceTabs);
		createResourceTabs();
		update();
	}

	private void createResourceTabs() {
//		for (int i=0; i<myController.getNumLevelsForGame()) {
//			Tab newTab = tabMaker.buildTabWithoutContent("Level " + Integer.toString(myController.getCurrentLevel()), null, resourceTabs);
//			
//			ResourceTab newLv = new ResourceTab(myController.getCurrentLevel(), myController);
//			newLv.attach(newTab);
//		}
		
	}

	private void changeResourceVal() {
		this.getChildren().clear();
		TextField name = new TextField();
		name.setPromptText("Name");
		TextField value = new TextField();
		value.setPromptText("Value");
		Button enter = new Button("add!");
		enter.setOnAction(e->{
			try {
			if (myController.getResourceEndowments().containsKey(name.getText())) {
				Double d = myController.getResourceEndowments().get(name.getText());
				d = Double.parseDouble(value.getText());
			}
			else {
				resourceEndowments.put(name.getText(), Double.parseDouble(value.getText()));
			}
			this.getChildren().clear();
			this.getChildren().add(editResources);
			try {
				myController.setResourceEndowment(name.getText(), Double.parseDouble(value.getText()));
			} catch(NumberFormatException nfe) {
				System.out.println("you have to type in a number");
			}
			myController.setResourceEndowment(name.getText(), Double.parseDouble(value.getText()));
			update();
		}catch(Exception nfe) {
			Alert a = new Alert(AlertType.ERROR);
			a.setHeaderText("Input Not Valid");
			a.setContentText("You need to input a number!");
			a.showAndWait();
		}});
		
		this.getChildren().addAll(name, value, enter);
		
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
