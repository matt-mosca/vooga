package authoring.LevelToolBar;

import engine.authoring_engine.AuthoringController;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ResourceTable {
	private AuthoringController myController;
	private TableView<Resource> myTable;
	private TableColumn<Resource, String> myResource;
	private TableColumn<Resource, String> myAmount;
	private int myLv;
	
	public ResourceTable(AuthoringController controller, int lv) {
		myController = controller;
		myLv = lv;	
		
		
	}

	private void update() {
		
	}

	private void makeTable() {
		
	}
}
