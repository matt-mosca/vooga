package engine.behavior.collision;

/**
 * Represents volatile 'explosion' behavior - simply explodes / self-destructs
 * upon colliding with (any?) other element
 * Can be used by projectiles
 * @author radithya
 *
 */
public abstract class VolatileCollider implements CollisionVisitor {
	
	// Can be sub-classed by different kinds of projectiles
	
	@Override
	public void visit(ImperviousCollisionVisitable visitable) {
		explode();
	}

	@Override
	public void visit(DamageDealingCollisionVisitable visitable) {
		explode();
	}

	@Override
	public void visit(NoopCollisionVisitable visitable) {
		explode();
	}
	
	protected abstract void explode();

}
