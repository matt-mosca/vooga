package engine.behavior.collision;

/**
 * Captures general collision behaviors of most colliders Specific collision
 * behaviors can be achieved by implementing visit(CollisionVisitable) visitable
 * for missing types and overriding where necessary
 * 
 * @author radithya
 *
 */
public abstract class GenericCollider implements CollisionVisitor {

	private boolean blocked;

	@Override
	public void visit(ImperviousCollisionVisitable visitable) {
		setBlocked();
	}

	@Override
	public void visit(NoopCollisionVisitable visitable) {
		// nothing happens when someone collides with a NoopCollisionVisitable
	}

	/**
	 * Whether the collider is 'dead' - mortal colliders, projectiles, etc can be
	 * 'dead'
	 * 
	 * @return
	 */
	public abstract boolean isAlive();

	/**
	 * Whether the unit is blocked by an obstacle Can be used by movement strategy
	 * to recompute path / reverse direction if necessary
	 * 
	 * @return true if unit is blocked, false otherwise
	 */
	public boolean isBlocked() {
		return blocked;
	}

	/**
	 * Will be used by MovementStrategy after recomputing path / reversing direction
	 */
	public void unBlock() {
		blocked = false;
	}

	private void setBlocked() {
		blocked = true;
	}

}
