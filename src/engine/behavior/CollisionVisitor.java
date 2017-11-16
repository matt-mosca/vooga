package engine.behavior;

/**
 * Visitor Design Pattern for handling collisions
 * Handle effects of collision on colliding element through the visit method
 * @author radithya
 *
 */
public interface CollisionVisitor {

	// TODO - Consider different kinds of reactions to collisions and implement Visitable differently for each one of them
	public void visit(ImperviousCollider collider);
	
	public void visit(MortalCollider collider);
	
}
