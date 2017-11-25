package factory;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class TabFactory {
	public Tab buildTab(String name, Node content, TabPane pane) {
		Tab product = new Tab(name);
		product.setContent(content);
		return product;
	}
	
	public Tab buildTabWithoutContent(String name, TabPane pane) {
		Tab product = new Tab(name);
		return product;
	}
}
