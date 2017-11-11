package authoring;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
 
public class RightToolBar extends VBox {
	
	private TableView<ObjectProperties> table = new TableView<ObjectProperties>();
	private final ObservableList<ObjectProperties> data;
	private Label label;
	private Button addButton;
	final TextField addFirst;
	final TextField addLast;
	TableColumn<ObjectProperties, String> firstCol;
	TableColumn<ObjectProperties, String> lastCol;
	
	public RightToolBar(AuthorInterface author) {
 
	    table = new TableView<ObjectProperties>();
	    data =
	            FXCollections.observableArrayList(
	            new ObjectProperties("Tower 1", "1"),
	            new ObjectProperties("Tower 2", "2"),
	            new ObjectProperties("Tower 3", "3"),
	            new ObjectProperties("Soldier 1", "20"),
	            new ObjectProperties("Solider 2", "50"));
  
        label = new Label("Table");
 
//        table.setEditable(true);
 
        firstCol = new TableColumn<ObjectProperties, String>("Object");
        firstCol.setCellValueFactory(
                new PropertyValueFactory<ObjectProperties, String>("first"));
 
        lastCol = new TableColumn<ObjectProperties, String>("Property 1");
        lastCol.setCellValueFactory(
                new PropertyValueFactory<ObjectProperties, String>("last"));
 
        table.setItems(data);
        table.getColumns().addAll(firstCol, lastCol);
 
        addFirst = new TextField();
        addFirst.setPromptText("First");
        addFirst.setMaxWidth(firstCol.getPrefWidth());
        addLast = new TextField();
        addLast.setMaxWidth(lastCol.getPrefWidth());
        addLast.setPromptText("Last");
        this.setLayoutX(680);
        this.getChildren().add(table);
 
        addButton = new Button("Add");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                data.add(new ObjectProperties(
                        addFirst.getText(),
                        addLast.getText()));
                addFirst.clear();
                addLast.clear();
            }
        });
        this.getChildren().addAll(addFirst, addLast, addButton);
        this.setSpacing(3);
 

    }
 
} 