package authoring;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class BottomToolBar extends ToolBar{
	private TableView<ObjectProperties> table = new TableView<ObjectProperties>();
	private ObjectProperties[] dataArray;
	private ObservableList<ObjectProperties> data;
	private TableColumn<ObjectProperties, String> firstCol;
	private TableColumn<ObjectProperties, String> lastCol;
	
	public BottomToolBar(AuthorInterface author) {
		super(author);
		this.setLayoutY(500);
		this.setLayoutX(260);
		
		
		
	}
	
}
