package authoring.rightToolBar;



import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

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
	public static final String[] properties = {"HP", "Strength", "Other"};
	private CreationInterface creation;
	private Label text;
	private Map<String, String> propertiesMap;
	private TableView<String> propertyTable;
	private ObservableList<String> displayList;
	private String[] propertyArr;
	private TableColumn<String, String> firstCol;
	private TableColumn<String, String> lastCol;
	
	public PropertiesBox(CreationInterface creation) {
		this.creation = creation;
		propertyArr = new String[]{"50","10","23"};
		propertiesMap = new TreeMap<String, String>();
		for (int i = 0; i < properties.length; i++) {
			propertiesMap.put(properties[i], propertyArr[i]);
		}
		text = new Label("properties");
		text.setFont(new Font("Andale Mono", 20));
		text.setStyle("-fx-effect: dropshadow(gaussian, rgba(67,96,156,0.25) , 0,0,2,2 )");
		propertyTable = new TableView<String>();
		// Set up table colomn
		firstCol = new TableColumn<String, String>("Property");
        lastCol = new TableColumn<String, String>("Value");
        propertyTable.getColumns().addAll(firstCol, lastCol);
        displayList =FXCollections.observableArrayList (propertiesMap.keySet());
		init();
//        lastColEditable(creation);
        

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
		firstCol.setUserData(propertiesMap.keySet());
        lastCol.setPrefWidth(189);
        lastCol.setUserData(propertiesMap.values());
        propertyTable.setItems(displayList);
        lastCol.setCellFactory(TextFieldTableCell.forTableColumn());
	}
	
	public void updatePropertiesBox(ObservableList<SpriteProperty> displayList) {
		creation.doSomething();
	}


	
	
	
}