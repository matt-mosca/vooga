package authoring.LevelToolBar;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class WavesDisplay extends TabPane {
	
	private SpriteDisplayer mySpriteDisplay;
	private List<SpriteDisplayer> myWaves;
	
	public WavesDisplay() {
		myWaves = new ArrayList<SpriteDisplayer>();
		myWaves.add(new SpriteDisplayer());
		Tab tab = new Tab();
		tab.setText("wave 1");
		tab.setContent(myWaves.get(0));
		this.getTabs().add(tab);
	}	
	
	public void addWave() {
		myWaves.add(new SpriteDisplayer());
	}
	
	public void updateLevel(List<ImageView> newImage, int level) {
		myWaves.get(level - 1).addToScroll(newImage);
	}
}