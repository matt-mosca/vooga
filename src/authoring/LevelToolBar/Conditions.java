package authoring.LevelToolBar;

import java.util.Collection;

public class Conditions {
	private String myCondition;
	private Collection<Integer> myLevels;
	
	public Conditions(String condition, Collection<Integer> levels) {
		setMyLevels(levels);
		setMyCondition(condition);
	}

	public String getMyCondition() {
		return myCondition;
	}

	public void setMyCondition(String myCondition) {
		this.myCondition = myCondition;
	}

	public String getMyLevels() {
		return myLevels.toString();
	}

	public void setMyLevels(Collection<Integer> myLevels) {
		this.myLevels = myLevels;
	}
}
