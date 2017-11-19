package authoring;

import java.util.ResourceBundle;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class PathLine extends Line{
	private final static String WIDTH = "Path_Width";
	private final static String INACTIVE = "Path_Color";
	private final static String ACTIVE = "Path_Active_Color";
	
	private ResourceBundle gameResources;
	private PathPoint start;
	private PathPoint end;
	private LineDirection direction;
	private boolean active = false;
	private Color activeColor;
	private Color inactiveColor;
	private int width;
	
	public PathLine(PathPoint start, PathPoint end) {
		initializeProperties();
		
		this.start = start;
		this.end = end;
		this.startXProperty().bind(start.centerXProperty());
		this.startYProperty().bind(start.centerYProperty());
		this.endXProperty().bind(end.centerXProperty());
		this.endYProperty().bind(end.centerYProperty());
		this.setStroke(inactiveColor);
		this.setStrokeWidth(width);
		
		direction = new LineDirection(start, end, this);
	}
	
	private void initializeProperties() {
		gameResources = ResourceBundle.getBundle("authoring/resources/GameArea");
		width = Integer.parseInt(gameResources.getString(WIDTH));
		activeColor = Color.web(gameResources.getString(ACTIVE));
		inactiveColor = Color.web(gameResources.getString(INACTIVE));
	}

	protected void toggleActive() {
		if(!active) {
			this.setStroke(activeColor);
		}else {
			this.setStroke(inactiveColor);
		}
		active = !active;
	}
	
	protected void removeLineFromPoints() {
		start.getNextLines().remove(end);
		end.getPrevLines().remove(start);
	}
	
	protected void changeDirection() {
		removeLineFromPoints();
		
		PathPoint temp = start;
		start = end;
		end = temp;
		
		direction.drawShape();
		start.getNextLines().put(end, this);
		end.getPrevLines().put(start, this);
	}
	
	protected boolean isActive() {
		return active;
	}
	
	protected LineDirection getDirectionComponent() {
		return direction;
	}
	
	protected PathPoint getStartPoint() {
		return start;
	}
	
	protected PathPoint getEndPoint() {
		return end;
	}
}
