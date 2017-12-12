package authoring.LevelToolBar;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class WaveDisplay extends TabPane {
	
	private TabInterface tabInterface;
	
	public WaveDisplay(TabInterface tabInterface) {
		this.tabInterface = tabInterface;
	}

	public void addTabs(int numberOfWaves) {
		int mySize = this.getTabs().size();
		if (numberOfWaves < mySize) {
			this.getTabs().remove(numberOfWaves, mySize);
		} else {
			for (int i = mySize; i < numberOfWaves; i++) {
				Tab tab = new Tab();
				tab.setText("wave" + String.valueOf(i+1));
				this.getTabs().add(tab);
				tab.setOnSelectionChanged(e -> tabInterface.updateImages());
			}
		}
	}
	
	public int getCurrTab() {
		return this.getSelectionModel().getSelectedIndex() + 1;
	}
}
