package engine.behavior.firing;

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

}
