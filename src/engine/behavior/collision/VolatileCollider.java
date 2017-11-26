package engine.behavior.collision;

/**
 * Represents volatile 'explosion' behavior - simply explodes / self-destructs
 * upon colliding with (any?) other element
 * Can be used by projectiles
 * @author radithya
 *
 */
public abstract class VolatileCollider extends GenericCollider {
	
	// Can be sub-classed by different kinds of projectiles
	public VolatileCollider(int playerId) {
		super(playerId);
	}

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
