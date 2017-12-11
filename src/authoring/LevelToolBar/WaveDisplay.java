package authoring.LevelToolBar;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class WaveDisplay extends TabPane {
	
	private LevelToolBar level;
	
	public WaveDisplay(LevelToolBar level) {
		this.level = level;
	}

	public void addTabs(int numberOfWaves) {
		this.getTabs().removeAll(this.getTabs());
		for (int i = 0; i < numberOfWaves; i++) {
			Tab tab = new Tab();
			tab.setText("wave" + String.valueOf(i+1));
			this.getTabs().add(tab);
			tab.setOnSelectionChanged(e -> level.updateImages());
		}
	}
	
	public int getCurrTab() {
		return this.getSelectionModel().getSelectedIndex() + 1;
	}
}
