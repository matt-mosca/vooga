package toolbars;

import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

public abstract class ToolBar extends VBox{
	
	protected void makeTabsUnclosable(TabPane tabPane) {
		for(int i = 0; i < tabPane.getTabs().size(); i++) {
			tabPane.getTabs().get(i).setClosable(false);
		}
	}
	
	protected abstract void createAndAddTabs();

}
