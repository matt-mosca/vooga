package engine.behavior.collision;

/**
 * Represents immortal collider behavior - does not take any damage, always
 * alive
 * Could be used by immortal units - like in GodMode
 * @author radithya
 *
 */
public class ImmortalCollider extends GenericCollider {

	@Override
	public void visit(DamageDealingCollisionVisitable visitable) {
		// Immortal colliders don't take any damage
	}

	@Override
	public boolean isAlive() {
		return true; // ImmortalColliders (Obstacles, etc) are always alive
	}

}
