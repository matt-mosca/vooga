package engine.behavior.firing;

public abstract class GenericFiringStrategy implements FiringStrategy {

	private String projectileTemplate;
	
	public GenericFiringStrategy(String projectileTemplate) {
		this.projectileTemplate = projectileTemplate;
	}
	
	@Override
	public String fire() {
		return projectileTemplate;
	}

}
