package authoring.path;

import java.awt.List;
import java.util.HashSet;
import java.util.Set;

import javafx.geometry.Point2D;

public class PathList extends List{
	private PathNode current;
	private PathNode constructor = null;
	private Set<PathPoint> points;
	
	public PathList() {
		points = new HashSet<>();
	}
	
	public Point2D next() {
		double x = current.x;
		double y = current.y;
		current = current.next;
		return new Point2D(x, y);
	}
	
	protected void add(PathPoint point) {
		PathNode node = new PathNode(point.getCenterX(), point.getCenterY(), null);
		if(constructor == null) {
			constructor = node;
			current = constructor;
		}else {
			constructor.next = node;
			constructor = constructor.next;
		}
		points.add(point);
	}
	
	protected boolean contains(PathPoint point) {
		return points.contains(point);
	}
	
	private class PathNode{
		private double x;
		private double y;
		private PathNode next = null;
		
		private PathNode(double x, double y, PathNode next) {
			this.x = x;
			this.y = y;
			this.next = next;
		}
	}
}
