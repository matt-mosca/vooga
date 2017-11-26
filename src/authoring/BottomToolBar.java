package authoring;

import java.util.ArrayList;
import java.util.List;

import factory.ButtonFactory;
import factory.TabFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class BottomToolBar extends ToolBar{
	private TableView<ObjectProperties> table = new TableView<ObjectProperties>();
	private ObjectProperties[] dataArray;
	private ObservableList<ObjectProperties> data;
	private TableColumn<ObjectProperties, String> firstCol;
	private TableColumn<ObjectProperties, String> lastCol;
	private AuthorInterface myAuthor;
	private ButtonFactory buttonMaker;
	private TabFactory tabMaker;
	private TabPane tabPane;
	private List<Tab> tabList;
	private int currLevelCount;
	private Button addButton;
	
	public BottomToolBar(AuthorInterface author) {
		super(author);
		currLevelCount = 1;
		this.setLayoutY(500);
		this.setLayoutX(260);
		table = new TableView<ObjectProperties>();
		data = FXCollections.observableArrayList(dataArray);
	    buttonMaker = new ButtonFactory();
	    tabMaker = new TabFactory();
	    tabList = new ArrayList<Tab>();
	    tabPane = new TabPane();
	    createTab();
	    addButton = buttonMaker.buildDefaultTextButton("Add", e -> addLevel());
//	    modifyButton = buttonMaker.
	    this.getChildren().add(addButton);
	    
	}
	
	private void addLevel() {
		currLevelCount++;
		createTab();
	}
	
	private void createTab() {
		Tab tab = tabMaker.buildTabWithoutContent("Level" + currLevelCount, tabPane);
		
		if (currLevelCount==1) {
			tab.setClosable(false);	
		}
		else {
			tab.setOnClosed(e -> deleteLevel(Integer.parseInt(tab.getText().split(" ")[1]) - 1));
		}
		tabPane.getTabs().add(tab);
	}

	private void deleteLevel(int num) {
		//backend.deleteLevel(int num) TODO
		//ideally this method would just decrement the lvCounter in the backend and will 
		//delete the previous level which existed at num
		
		for (int i = num; i<currLevelCount; i++) {
			tabPane.getTabs().get(i).setText("Level" + i);
		}
		currLevelCount--;
	}
	
}
