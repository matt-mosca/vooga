package engine.behavior.firing;

import java.awt.geom.Point2D;

/**
 * Support different ways of firing, one of which is chosen by authoring and
 * then passed to Sprite at initialization
 * 
 * @author tyler
 * @author radithya
 *
 */
public interface FiringStrategy {

	/**
	 * Return the template name
	 */
	public String fire();
	
	/**
	 * Whether the projectile should fire in this cycle
	 * @return
	 */
	public boolean shouldFire();

	
	public void setFiringPosition(Point2D.Double firingPosition);
	
	public void setTargetPoint(Point2D.Double targetPoint);
	
	public Point2D.Double getTargetPoint();
}
