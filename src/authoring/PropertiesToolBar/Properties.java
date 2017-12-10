package authoring.PropertiesToolBar;

public class Properties {
	private String myProperty;
	private String myValue;

	public Properties(String rowName, String value) {
		myProperty = rowName;
		myValue = value;
	}
	
	
	public String getMyProperty() {
		return myProperty;
	}

	public void setMyProperty(String myName) {
		this.myProperty = myName;
	}

	public String getMyValue() {
		return myValue;
	}

	public void setMyValue(String myValue) {
		this.myValue = myValue;
	}
	
}