package authoring.LevelToolBar;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.sun.j3d.utils.scenegraph.io.retained.Controller;

import authoring.PropertiesToolBar.Properties;
import engine.authoring_engine.AuthoringController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class GameEnderRecorder extends VBox{
	private TableView <Conditions> victoryConditions;
	private TableView<Conditions> defeatConditions;
	private AuthoringController myController;
	
	
	public GameEnderRecorder(AuthoringController controller) {
//		myCompletedLevels=levels;
		myController = controller;
//		update();
		
	}

	public void update() {
		makeTables();
		this.getChildren().addAll(victoryConditions, defeatConditions);
	}
	
	private void makeTables() {
		ObservableList<Conditions> vicConditions = FXCollections.observableArrayList();
		ObservableList<Conditions> lossConditions = FXCollections.observableArrayList();
		Map<String, Collection<Integer>> victory = myController.getCurrentVictoryConditions();
		Map<String, Collection<Integer>> defeat = myController.getCurrentDefeatConditions();
		makeConditions(victory, vicConditions);
		makeConditions(defeat, lossConditions);
		victoryConditions = new TableView<Conditions>();
		defeatConditions = new TableView<Conditions>();
		fillTable(victoryConditions, vicConditions);
		fillTable(defeatConditions, lossConditions);
		
	}

	private void fillTable(TableView<Conditions> table, ObservableList<Conditions> conditions) {
		TableColumn<Conditions, String> condColumn = makeColumn("Conditions", "myCondition");
		TableColumn<Conditions, String> levelsColumn = makeColumn("Levels", "myLevels");
		table.setItems(conditions);
		table.getColumns().addAll(condColumn, levelsColumn);
		
	}

	private TableColumn<Conditions, String> makeColumn(String title, String instanceVar) {
		TableColumn<Conditions, String> column = new TableColumn<>(title);
		column.setPrefWidth(100);
		column.setCellValueFactory(new PropertyValueFactory<>(instanceVar));
		return column;
	}

	private void makeConditions(Map<String, Collection<Integer>> list, ObservableList<Conditions> conditions) {
		for(String s : list.keySet()) {
			Conditions c = new Conditions(s, list.get(s));
			conditions.add(c);
		}
	}
}
