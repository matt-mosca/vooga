package authoring.path;

import java.util.LinkedList;

import javafx.geometry.Point2D;

public class PathList extends LinkedList{
	private PathNode current;
	private PathNode head;
	private PathPoint headpoint;
	private PathNode constructor;
	private LinkedList<PathPoint> points;
	
	public PathList(PathPoint start) {
		points = new LinkedList<>();
		head = new PathNode(start);
		headpoint = start;
		current = head;
		constructor = head;
	}
	
	public Point2D next() {
		double x = current.x;
		double y = current.y;
		current = current.next;
		return new Point2D(x, y);
	}
	
	protected boolean add(PathPoint point) {
		PathNode node = new PathNode(point);
		constructor.next = node;
		constructor = constructor.next;
		if(points.contains(point)) {
			return false;
		}else {
			points.add(point);
			return true;
		}
	}
	
	public PathList clone() {
		PathList copy = new PathList(headpoint);
		for(PathPoint point:points) {
			copy.add(point);
		}
		return copy;
	}
	
	private class PathNode{
		private double x;
		private double y;
		private PathNode next = null;
		
		private PathNode(PathPoint next) {
			this.x = next.getCenterX();
			this.y = next.getCenterY();
			this.next = null;
		}
	}
}
