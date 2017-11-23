package authoring;

import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;

public class ScrollableArea extends ScrollPane{
	private final int X_OFFSET = 260;
	private final int Y_OFFSET = 50;
	private final int SIZE = 400;
	
	private GameArea area;
	private boolean scrollOff;
	
	public ScrollableArea(GameArea area) {
		this.area = area;
		this.setVbarPolicy(ScrollBarPolicy.NEVER);
		this.setHbarPolicy(ScrollBarPolicy.NEVER);
		this.setLayoutX(X_OFFSET);
		this.setLayoutY(Y_OFFSET);
		this.setPrefSize(SIZE, SIZE);
		this.setContent(area);
		this.addEventHandler(ZoomEvent.ZOOM, e->zoom(e));
		this.addEventFilter(ScrollEvent.ANY, e->scroll(e));
	}
	
	private void zoom(ZoomEvent e) {
		double scaleX = e.getZoomFactor()*area.getScaleX();
		double scaleY = e.getZoomFactor()*area.getScaleY();
		
		if(scaleX <= 1 && scaleY <= 1 && (scaleX*area.getHeight() >= SIZE) && (scaleY*area.getWidth() >= SIZE)) {
			area.setScaleX(area.getScaleX()* e.getZoomFactor());
			area.setScaleY(area.getScaleY()* e.getZoomFactor());
			this.setHvalue(1 - area.getScaleX());
			this.setVvalue(1 - area.getScaleY());
		}
	}
	
	private void scroll(ScrollEvent e) {
		if(Math.floor(area.getWidth()*area.getScaleX())<=SIZE) {
			e.consume();
		}
	}
}
