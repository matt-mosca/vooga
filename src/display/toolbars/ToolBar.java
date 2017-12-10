package display.toolbars;

import java.util.Map;

import display.tabs.SimpleTab;
import engine.AbstractGameModelController;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public abstract class ToolBar extends VBox{
	protected TabPane tabPane;
	
	protected void makeTabsUnclosable(TabPane pane) {
		for(int i = 0; i < pane.getTabs().size(); i++) {
			pane.getTabs().get(i).setClosable(false);
		}
	}
	
	public void addToToolbar(ImageView imageView, String tabName, TabPane pane) {
		for(Tab tab:pane.getTabs()) {
			if(tab.getText().equals(tabName)) {
				SimpleTab simpleTab = (SimpleTab) tab.getContent();
				simpleTab.addItem(imageView);
			}
		}
	}
	
	protected void initializeInventory(AbstractGameModelController controller, TabPane pane) {
		Map<String, Map<String, Object>> templates = controller.getAllDefinedTemplateProperties();
		for(String s:controller.getInventory()) {
			ImageView imageView;
			try {
				imageView = new ImageView(new Image(templates.get(s).get("imageUrl").toString()));
				
			}catch(NullPointerException e) {
				imageView = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream(templates.get(s)
						.get("imageUrl").toString())));
			}
			imageView.setFitHeight(70);
			imageView.setFitWidth(60);
			imageView.setId(s);
			imageView.setUserData(templates.get(s).get("imageUrl"));
			addToToolbar(imageView, templates.get(s).get("tabName").toString(), pane);
		}
	}
	
	protected abstract void createAndAddTabs();

}
