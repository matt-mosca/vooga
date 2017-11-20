package authoring;

import javafx.scene.control.ScrollPane;

public class ScrollableArea extends ScrollPane{
	private final int X_OFFSET = 260;
	private final int Y_OFFSET = 50;
	private final int SIZE = 400;
	
	public ScrollableArea(GameArea area) {
		this.setVbarPolicy(ScrollBarPolicy.NEVER);
		this.setHbarPolicy(ScrollBarPolicy.NEVER);
		this.setLayoutX(X_OFFSET);
		this.setLayoutY(Y_OFFSET);
		this.setPrefSize(SIZE, SIZE);
		this.setContent(area);
	}
}
