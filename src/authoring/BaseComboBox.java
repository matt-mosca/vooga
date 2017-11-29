package authoring;

import java.util.List;

import interfaces.CustomizeInterface;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

//public class BaseComboBox extends ComboBox<String>{
	
public class BaseComboBox extends Label {

	private static final int Y_POS = 500;
	private static final int WIDTH = 200;
	
	public BaseComboBox(String name, List<String> options, AuthorInterface author) {
//		this.setPrefWidth(WIDTH);
//		this.setLayoutY(Y_POS);
//		this.setPromptText(name);
//		ObservableList<String> optionsList = FXCollections.observableArrayList(options);
//		ChangeListener<String> propertyHandler = (obs, old, cur) -> author.changeColor(cur);
//		this.getSelectionModel().selectedItemProperty().addListener(propertyHandler);
//		this.setVisibleRowCount(3);
//		this.setItems(optionsList);
		this.setLayoutY(300);
		this.setText("test");
	}
	

}
