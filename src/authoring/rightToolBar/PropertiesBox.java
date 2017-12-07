package authoring.rightToolBar;

import java.util.HashMap;
import java.util.Map;

import engine.authoring_engine.AuthoringController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class PropertiesBox extends VBox {
	private Map<String, String> propertiesMap;
	private String[] propertyArr;
	private TableView<Properties> table;
	private ObservableList<Properties> data;
	private TableColumn<Properties, String> propertiesColumn;
	private TableColumn<Properties, String> valuesColumn;
	private ImageView currSprite;
	private AddToWaveButton myWaveAdder;

	
	public PropertiesBox(ImageView mySprite, AuthoringController author) {
		currSprite = mySprite;
		propertiesMap = author.getTemplateProperties(mySprite.getId());
		table = new TableView<Properties>();
		table.setEditable(true);
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
		table.setPrefHeight(250);
		this.setPrefHeight(250);
		this.setLayoutX(50);
		
		valuesColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		valuesColumn.setOnEditCommit(
			    new EventHandler<CellEditEvent<Properties, String>>() {
			        @Override
			        
			        public void handle(CellEditEvent<Properties, String> t) {
			            ((Properties) t.getTableView().getItems().get(
			                t.getTablePosition().getRow())
			                ).setMyValue(t.getNewValue());
			            Map<String, String> newPropertiesMap = new HashMap<String, String>();
			            newPropertiesMap.put(t.getRowValue().getMyProperty(), t.getNewValue());
			            author.updateElementDefinition(mySprite.getId(), newPropertiesMap, true);
			        }
			    }
			);
	}
	
	public ImageView getCurrSprite() {
		return currSprite;
	}
	
}