package authoring.rightToolBar;



import interfaces.CreationInterface;
//import GUI.TableViewSample.EditingCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class PropertiesBox extends VBox {
	public static final String[] properties = {"Direction", "X Position","Y Position", "Pen Down", "Pen Color", "Pen Size", "Pen Style"};
	private CreationInterface creation;
	private Label text;
	private TableView propertyTable;
	private ObservableList<SpriteProperty> displayList;
	private static String[] propertyArr = {"0.0","0.0","0.0","true", "red","1","SOLID"};
	private TableColumn firstCol;
	private TableColumn lastCol;
	
	public PropertiesBox(CreationInterface creation) {
		this.creation = creation;
		text = new Label("properties");
		text.setFont(new Font("Andale Mono", 20));
		text.setStyle("-fx-effect: dropshadow(gaussian, rgba(67,96,156,0.25) , 0,0,2,2 )");
		propertyTable = new TableView();
		// Set up table colomn
		firstCol = new TableColumn("Property");
        lastCol = new TableColumn("Value");
        propertyTable.getColumns().addAll(firstCol, lastCol);
        displayList =FXCollections.observableArrayList ();
			for (int i = 0; i < 7; i++) {
	        	displayList.add(new SpriteProperty(properties[i], propertyArr[i]));
	        }
		init();
        lastColEditable(app);
        

		this.getChildren().add(text);
		this.getChildren().add(propertyTable);
		this.setAlignment(Pos.CENTER);
		this.setPrefSize(220, 220);
		propertyTable.setEditable(true);
		this.setLayoutX(500);
		this.setLayoutY(30);
		this.setSpacing(20);
	}


	public void init() {
		firstCol.setPrefWidth(189);
        firstCol.setCellValueFactory(
                new PropertyValueFactory<SpriteProperty, String>("myName"));
        lastCol.setPrefWidth(189);
        lastCol.setCellValueFactory(
                new PropertyValueFactory<SpriteProperty, String>("myValue"));
        propertyTable.setItems(displayList);
        lastCol.setCellFactory(TextFieldTableCell.forTableColumn());
	}


	public void lastColEditable(CreationInterface creation) {
		lastCol.setOnEditCommit(
            new EventHandler<CellEditEvent<SpriteProperty, String>>() {
                @Override
                public void handle(CellEditEvent<SpriteProperty, String> t) {
                    ((SpriteProperty) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setMyName(t.getNewValue());
                    int rowNum = t.getTablePosition().getRow();
                    String val = t.getNewValue();    
                    app.moveX(Double.parseDouble(t.getNewValue()));
                }
            }
        );
	}
	
	
	public void updatePropertiesBox(ObservableList<SpriteProperty> displayList) {
		creation.setDirection(Double.parseDouble(displayList.get(0).getMyValue()));
		creation.moveX(Double.parseDouble(displayList.get(1).getMyValue()));
	}
	
	public void updatePropertiesBox() {
		propertyArr = creation.getInfo();
		displayList.clear();
		for (int i = 0; i < 7; i++) {
        	displayList.add(new SpriteProperty(properties[i], propertyArr[i]));
        }
	}


	
	
	
}