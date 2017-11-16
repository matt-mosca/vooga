package engine.behavior;

public class MortalCollider implements CollisionVisitor, CollisionVisitable {

	public MortalCollider() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void accept(CollisionVisitor v) {
		v.visit(this);
	}

	@Override
	public void visit(ImperviousCollider collider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(MortalCollider collider) {
		// TODO Auto-generated method stub
		
	}

}
