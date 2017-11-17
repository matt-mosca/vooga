package engine.behavior.collision;

/**
 * Represents volatile 'explosion' behavior - simply explodes / self-destructs
 * upon colliding with (any?) other element
 * Can be used by projectiles
 * @author radithya
 *
 */
public class VolatileCollider implements CollisionVisitor {

	@Override
	public void visit(ImperviousCollisionVisitable visitable) {
		
	}

	@Override
	public void visit(DamageDealingCollisionVisitable visitable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(NoopCollisionVisitable visitable) {
		// TODO Auto-generated method stub

	}
	
	protected void explode()

}
