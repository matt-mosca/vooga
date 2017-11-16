package behavior;

/**
 * Support different ways of firing, one of which is chosen by authoring and
 * then passed to Sprite at initialization
 * 
 * @author radithya
 *
 */
public interface FiringStrategy {

	/**
	 * Consider returning a projectile type instead?
	 */
	public void fire();

}
