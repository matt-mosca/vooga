package engine.behavior.firing;

import java.awt.geom.Point2D;

import engine.behavior.ParameterName;

public abstract class GenericFiringStrategy implements FiringStrategy {

	private String projectileTemplate;
	private Point2D.Double position;
	private Point2D.Double targetPoint;
	
	
	public GenericFiringStrategy(@ParameterName("projectileTemplate") String projectileTemplate) {
		this.projectileTemplate = projectileTemplate;
	}
	
	@Override
	public String fire() {
		return projectileTemplate;
	}
	
	@Override
	public Point2D.Double getTargetPoint() {
		return targetPoint;
	}
	
	@Override
	public void setFiringPosition(Point2D.Double firingPosition) {
		position = firingPosition;
	}
	
	@Override 
	public void setTargetPoint(Point2D.Double targetPoint) {
		this.targetPoint = targetPoint;
	}
	
	protected Point2D.Double getPosition() {
		return position;
	}
	
}
