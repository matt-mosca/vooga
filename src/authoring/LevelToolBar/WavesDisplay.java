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
	
	public WavesDisplay(int numberOfWaves) {
		myWaves = new ArrayList<SpriteDisplayer>();
		myWaves.add(new SpriteDisplayer());
		for (int i = 0; i < numberOfWaves; i++) {
			Tab tab = new Tab();
			tab.setText("wave" + String.valueOf(i));
			tab.setContent(mySpriteDisplay);
			this.getTabs().add(tab);
		}
		
	}	
	
	public void addWave() {
		myWaves.add(new SpriteDisplayer());
	}
	
	public void updateLevel(List<ImageView> newImage, int level) {
		myWaves.get(level - 1).addToScroll(newImage);
	}
}