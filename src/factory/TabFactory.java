package factory;

import javafx.scene.Group;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class TabFactory {
	public Tab buildTab(String name, Group content, TabPane pane) {
		Tab product = new Tab(name);
		product.setContent(content);
		return product;
	}
}
