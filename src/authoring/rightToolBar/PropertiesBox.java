package authoring.rightToolBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import authoring.path.Path;
import engine.authoring_engine.AuthoringController;
import interfaces.Droppable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import splashScreen.ScreenDisplay;

public class PropertiesBox extends VBox {
	private Map<String, String> propertiesMap;
	private String[] propertyArr;
	private TableView<Properties> table;
	private ObservableList<Properties> data;
	private TableColumn<Properties, String> propertiesColumn;
	private TableColumn<Properties, String> valuesColumn;
	private ImageView currSprite;
	private AddToWaveButton myWaveAdder;
	private Droppable myDroppable;

	
	public PropertiesBox(Droppable droppable, ImageView mySprite, AuthoringController author) {
		currSprite = mySprite;
		myDroppable = droppable;
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
		valuesColumn.setOnEditStart(
				 new EventHandler<CellEditEvent<Properties, String>>() {

					@Override
					public void handle(CellEditEvent<Properties, String> t) {
						if(t.getRowValue().getMyProperty().equals("PathList")) {
							((Properties) t.getTableView().getItems().get(
					                t.getTablePosition().getRow())
					                ).setMyValue(launchPathSelection());
							//backend integration for updating path RIGHT HERE
						}
					}  
				 }       
			);
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
	
	private String launchPathSelection() {
		Dialog<String> pathChooser = new Dialog<>();
		pathChooser.setTitle("Path Selection");
		pathChooser.setContentText("Choose a path for the object to follow");
		
		BorderPane pane = new BorderPane();
		pane.setPrefSize(150, 100);
		ComboBox<Rectangle> colorChooser = new ComboBox<>();
		colorChooser.setPrefSize(150, 30);
		Map<Path, Color> paths = myDroppable.getPaths();
		for(Path path:paths.keySet()) {
			Rectangle r = new Rectangle(colorChooser.getPrefWidth()-30, colorChooser.getPrefHeight());
			r.setFill(paths.get(path));
			colorChooser.getItems().add(r);
		}
		pane.setCenter(colorChooser);
		pathChooser.getDialogPane().setContent(pane);
		pathChooser.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		
		Optional<String> result = pathChooser.showAndWait();
		
		if(result.isPresent()) {
			return colorChooser.getSelectionModel().getSelectedItem().getFill().toString();
		}else {
			return "NO PATH SELECTED";
		}
		
	}
	
}