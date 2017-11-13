package play_engine;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import sprites.Sprite;

/**
 * Single-source of truth for back end data of game in-play AND game-specific logic
 * @author radithya
 *
 */
public class GameStateManager {

	private PlayIOController playIOController;
	// Also need reference to ElementFactory, perhaps passed down from PlayController?
	// ElementFactory elementFactory
	
	private String gameDescription;
	private Map<String, String> gameStatus;
	private Collection<Sprite> gameElements;
	
	public GameStateManager(PlayIOController playIOController) {//, ElementFactory elementFactory) {
		this.playIOController = playIOController;
		//this.elementFactory = elementFactory;
		// These will be set upon loading
		gameDescription = "";
		gameStatus = new HashMap<>();
		gameElements = new HashSet<>();
	}
	
	void update() {
		// TODO
	}

	int getLives() {
		// TODO
		return 0; // TEMP
	}

	Map<String, Integer> getResources() {
		// TODO
		return new HashMap<>(); // TEMP
	}

	int getCurrentLevel() {
		// TODO
		return 1; // TEMP
	}
	
	Map<String, String> getStatus() {
		// TODO
		return new HashMap<>();
	}

	boolean isInPlay() {
		// TODO
		return false; // TEMP
	}

	boolean isWon() {
		// TODO
		return false; // TEMP
	}

	boolean isLost() {
		// TODO
		return false; // TEMP
	}

	boolean isLevelCleared() {
		// TODO
		return false; // TEMP
	}

	void pause() {
		// TODO
	}

	void resume() {
		// TODO
	}

	Collection<Sprite> getCurrentElements() {
		return gameElements;
	}

	Sprite placeElement(String elementName, double x, double y) {
		// TODO
		return null; // TEMP
	}
	
	void saveGameState(String savedGameName) {
		playIOController.saveGameState(savedGameName, gameDescription, gameStatus, gameElements);
	}
	
	void setStatus(Map<String, String> newStatus) {
		gameStatus = newStatus;
	}
	
	void setCurrentElements(Collection<Sprite> newElements) {
		gameElements = newElements;
	}
	
}
