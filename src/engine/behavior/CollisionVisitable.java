package engine.behavior;

/**
 * Visitor Design Pattern for handling collisions
 * @author radithya
 *
 */
public interface CollisionVisitable {

	public void accept (CollisionVisitor v);

}
