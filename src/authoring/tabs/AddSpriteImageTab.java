package authoring.tabs;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import authoring.rightToolBar.SpriteImage;
import interfaces.ClickableInterface;
import javafx.scene.control.TabPane;

public class AddSpriteImageTab extends AddTab{
	private final String PACKAGE = "authoring.rightToolBar.";

	public AddSpriteImageTab(ClickableInterface clickable, TabPane tabs) {
		super(clickable, tabs);
	}

	@Override
	protected void createResource(File file, String tabName) {
		NewSpriteTab activeTab = (NewSpriteTab) tabPane.getTabs().get(objectTypes.getSelectionModel().getSelectedIndex()).getContent();
		
		try {
			Class<?> clazz = Class.forName(PACKAGE + tabName);
			Constructor<?> ctor = clazz.getDeclaredConstructor(String.class);
			SpriteImage object = (SpriteImage) ctor.newInstance(file.toURI().toString());
			activeTab.addImage(object);
		} catch (ClassNotFoundException | SecurityException | InstantiationException |
				IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException e) {
			//Add in error handling here
			e.printStackTrace();
		}
	}

}
