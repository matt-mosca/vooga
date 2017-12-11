package authoring.LevelToolBar;

public class Resource {
	private String resourceType;
	private int amount;
	
	public Resource(String type, int amt) {
		setResourceType(type);
		setAmount(amt);
	}
	
	
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	
	
}
