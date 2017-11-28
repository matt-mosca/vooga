package authoring;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;

public class MainMenuBar extends MenuBar{
	private Menu file;
	private Menu edit;
	
	public MainMenuBar() {
		
		Menu file = new Menu("File");
		Menu edit = new Menu("Edit");
		this.getMenus().addAll(file, edit);
	}
}
