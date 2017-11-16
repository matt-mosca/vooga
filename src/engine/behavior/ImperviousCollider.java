package engine.behavior;

public class ImperviousCollider implements CollisionVisitable, CollisionVisitor {

	public ImperviousCollider() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void accept(CollisionVisitor v) {
		v.visit(this);
	}

	// No-Op, nothing happens to impervious collider upon any of these collisions

	@Override
	public void visit(ImperviousCollider collider) {
	}

	@Override
	public void visit(MortalCollider collider) {
	}

}
