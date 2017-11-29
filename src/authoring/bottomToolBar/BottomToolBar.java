package authoring.bottomToolBar;

import java.util.ArrayList;
import java.util.List;

import interfaces.CreationInterface;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

public class BottomToolBar extends VBox {
	private TabPane myTabPane;
	private List<LevelTab> myLevels;
	private final int X_LAYOUT = 260;
	private final int Y_LAYOUT = 500;
	private Button newLevel;
	
	public BottomToolBar (CreationInterface created) {
		this.setLayoutX(X_LAYOUT);
		this.setLayoutY(Y_LAYOUT);
		myLevels = new ArrayList<>();
		newLevel =  new Button();
		newLevel.setOnAction(e->addLevel());
		myTabPane = new TabPane();
		addLevel();
	}

	private void addLevel() {
		LevelTab newLv = new LevelTab(myLevels.size()+1);	
	}
}
