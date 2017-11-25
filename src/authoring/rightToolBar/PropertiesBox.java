package authoring.rightToolBar;

import java.util.Map;
import java.util.TreeMap;

import interfaces.CreationInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class PropertiesBox extends VBox {
	public static final String[] properties = {"HP", "Strength", "Other"};
	private CreationInterface creation;
	private Map<String, String> propertiesMap;
	private String[] propertyArr;
	private TableView<Properties> table;
	private ObservableList<Properties> data;
	private TableColumn<Properties, String> propertiesColumn;
	private TableColumn<Properties, String> valuesColumn;
	
	public PropertiesBox(CreationInterface creation) {
		this.creation = creation;
		propertyArr = new String[]{"50","10","23"};
		propertiesMap = new TreeMap<String, String>();
		for (int i = 0; i < properties.length; i++) {
			propertiesMap.put(properties[i], propertyArr[i]);
		}
		table = new TableView<Properties>();
		propertiesColumn = new TableColumn<Properties, String>("Properties");
		valuesColumn = new TableColumn<Properties, String>("Values");
		data = FXCollections.observableArrayList();
		for (String s : propertiesMap.keySet()) {
			data.add(new Properties(s, propertiesMap.get(s)));
		}
		propertiesColumn.setCellValueFactory(
				new PropertyValueFactory<Properties, String>("myProperty"));
		valuesColumn.setCellValueFactory(
				new PropertyValueFactory<Properties, String>("myValue"));
		table.setItems(data);
		table.getColumns().addAll(propertiesColumn, valuesColumn);
		this.getChildren().add(table);
	}


	
	
	
}