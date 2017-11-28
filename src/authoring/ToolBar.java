package authoring;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;


//This is meant to be the Super class for the different toolbars that we have in authoring

public class ToolBar extends VBox{
	
	private TableView<ObjectProperties> table = new TableView<ObjectProperties>();
	private ObjectProperties[] dataArray;
	private ObservableList<ObjectProperties> data;
	
	public ToolBar(AuthorInterface author) {
		// TODO Auto-generated constructor stub
	}

}
