package engine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import sprites.Sprite;

public abstract class StateManager {

	private IOController ioController;
	// TODO - Uncomment when ElementFactory is ready
	//private ElementFactory elementFactory;
	// TODO - Should be customizable per level?
	private String gameDescription;
	private Map<String, String> gameStatus;
	private int currentLevel;

	public StateManager(IOController ioController) {
		// TODO Auto-generated constructor stub
		this.ioController = ioController;
		// this.elementFactory = elementFactory;
		// These will be set upon loading
		gameDescription = "";
		gameStatus = new HashMap<>();
		currentLevel = 1;
	}
	
	public abstract void saveGameState(String savedGameName);
	
	public abstract Sprite placeElement(String elementName, double x, double y);
	
	public abstract void setCurrentElements(Collection<Sprite> newElements);
	
	public abstract Collection<Sprite> getCurrentElements();
	
	// TODO - should take level as parameter?
	public String getDescription() {
		return gameDescription;
	}
	
	public Map<String, String> getStatus() {
		return gameStatus;
	}
	
	public int getLives() {
		// TODO
		return 0; // TEMP
	}

	public Map<String, Integer> getResources() {
		// TODO
		return new HashMap<>(); // TEMP
	}

	public int getCurrentLevel() {
		return currentLevel;
	}

	public void setStatus(Map<String, String> newStatus) {
		gameStatus = newStatus;
	}
	
	protected void setCurrentLevel(int level) throws IllegalArgumentException {
		assertValidLevel(level);
		currentLevel = level;
	}
	
	protected IOController getIOController() {
		return ioController;
	}
	
	// TODO - Uncomment when ElementFactory is ready
	/*
	protected ElementFactory getElementFactory() {
		return elementFactory;
	}
	*/
	
	protected abstract void assertValidLevel(int level) throws IllegalArgumentException;
	
}
