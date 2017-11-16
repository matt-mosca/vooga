package factory;

import javafx.scene.Group;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class TabFactory {
	public Tab buildTab(String name, Group content, TabPane pane) {
		Tab tab = new Tab(name);
		tab.setContent(content);
		return tab;
	}
}
