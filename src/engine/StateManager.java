package engine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.sun.xml.internal.messaging.saaj.soap.impl.ElementFactory;
import sprites.Sprite;
import sprites.SpriteFactory;

public abstract class StateManager {

	private IOController ioController;
	private SpriteFactory spriteFactory;
	// TODO - Should be customizable per level?
	private String gameDescription;
	private Map<String, String> gameStatus;
	private int currentLevel;

	public StateManager(IOController ioController, SpriteFactory spriteFactory) {
		this.ioController = ioController;
		this.spriteFactory = spriteFactory;
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

	protected SpriteFactory getSpriteFactory() {
		return spriteFactory;
	}
	
	protected abstract void assertValidLevel(int level) throws IllegalArgumentException;
	
}
