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
	private TabFactory tabMaker;
	
	public ResourceDisplay(AuthoringController controller){
		myController = controller;
		resourceEndowments = new HashMap<>();
		
		
		resourceTabs = new TabPane();
		tabMaker = new TabFactory();
		resources = new ArrayList<ResourceTab>();
		changeResourceValApparatus();
		
		createResourceTabs();
		update(myController.getCurrentLevel());
	}

	private void createResourceTabs() {
		for (int i=0; i<myController.getNumLevelsForGame(); i++) {
			System.out.println(Integer.toString(myController.getCurrentLevel()));
			Tab newTab = tabMaker.buildTabWithoutContent("Level " + Integer.toString(i+1), null, resourceTabs);
			ResourceTab newLv = new ResourceTab(i+1, myController);
			newLv.attach(newTab);
			resources.add(newLv);
			final int j = i+1;
			newTab.setOnSelectionChanged(e->update(j));
			newTab.setClosable(false);
			resourceTabs.getTabs().add(newTab);
		}
		
	}

	private void changeResourceValApparatus() {
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
			try {
				myController.setResourceEndowment(name.getText(), Double.parseDouble(value.getText()));
			} catch(NumberFormatException nfe) {
				System.out.println("you have to type in a number");
			}
			myController.setResourceEndowment(name.getText(), Double.parseDouble(value.getText()));
			update(myController.getCurrentLevel());
		}catch(Exception nfe) {
			Alert a = new Alert(AlertType.ERROR);
			a.setHeaderText("Input Not Valid");
			a.setContentText("You need to input a number!");
			a.showAndWait();
		}});
		this.getChildren().add(resourceTabs);
		this.getChildren().addAll(name, value, enter);
		
	}

	private void update(int lv) {
		resources.get(lv-1).update();
	}
	
	void updateCurrentState() {
//		resources.clear();
//		resourceTabs.getTabs().clear();
//		createResourceTabs();
	}

	public VBox getRoot() {
		return this;
	}
}
