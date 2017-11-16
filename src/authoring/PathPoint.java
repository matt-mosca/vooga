package authoring;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class PathPoint extends Circle{
	private List<PathPoint> nextPoints;
	
	protected PathPoint(double x, double y) {
		this.setCenterX(x);
		this.setCenterY(y);
		this.setRadius(5);
		this.setFill(Color.RED);
		nextPoints = new ArrayList<>();
		
		initializeHandlers();
	}
	
	private void initializeHandlers() {
		this.addEventHandler(MouseEvent.MOUSE_DRAGGED, e->dragPoint(e));
	}

	protected Line setConnectingLine(PathPoint next) {
		nextPoints.add(next);
		Line line = new Line();
		line.startXProperty().bind(this.centerXProperty());
		line.startYProperty().bind(this.centerYProperty());
		line.endXProperty().bind(next.centerXProperty());
		line.endYProperty().bind(next.centerYProperty());
		line.setFill(Color.RED);
		return line;
	}
	
	private void dragPoint(MouseEvent e) {
		this.setCenterX(e.getX());
		this.setCenterY(e.getY());
		e.consume();
	}
	
}
