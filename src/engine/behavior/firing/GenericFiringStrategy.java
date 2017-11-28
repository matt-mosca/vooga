package engine.behavior.firing;

import javafx.geometry.Point2D;

import engine.behavior.ParameterName;

/**
 * Captures whatever is common across all implementations of FiringStrategy
 * 
 * @author radithya
 *
 */
public abstract class GenericFiringStrategy implements FiringStrategy {

	private String projectileTemplate;
	private Point2D position;
	private Point2D targetPoint;

	public GenericFiringStrategy(@ParameterName("projectileTemplate") String projectileTemplate) {
		this.projectileTemplate = projectileTemplate;
	}

	@Override
	public String fire() {
		return projectileTemplate;
	}

	@Override
	public Point2D getTargetPoint() {
		return targetPoint;
	}

	@Override
	public void setFiringPosition(Point2D firingPosition) {
		position = firingPosition;
	}

	@Override
	public void setTargetPoint(Point2D targetPoint) {
		this.targetPoint = targetPoint;
	}

	protected Point2D getPosition() {
		return position;
	}

}
