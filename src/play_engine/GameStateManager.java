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
	
	/**
	 * Run the game loop for the given number of cycles
	 * 
	 * @param cycles
	 *            the number of cycles
	 */
	public void update() {
		// TODO
	}

	/**
	 * Get lives left
	 * 
	 * @return number of lives left
	 */
	public int getLives() {
		// TODO
		return 0; // TEMP
	}

	/**
	 * Retrieve the amount of each resource left
	 * 
	 * @return map of resource name to amount left
	 */
	public Map<String, Integer> getResources() {
		// TODO
		return new HashMap<>(); // TEMP
	}

	/**
	 * Query the current level of the game
	 * 
	 * @return the integer corresponding to the game's current level
	 */
	public int getCurrentLevel() {
		// TODO
		return 1; // TEMP
	}
	
	/**
	 * Query current play status (lives, kills, resources, all top-level metrics)
	 * @return map of parameter name to value
	 */
	public Map<String, String> getStatus() {
		// TODO
		return new HashMap<>();
	}

	/**
	 * Query whether the game is currently in play
	 * 
	 * @return true if in play, false if over / paused
	 */
	public boolean isInPlay() {
		// TODO
		return false; // TEMP
	}

	/**
	 * Query whether game has been won
	 * 
	 * @return true if won, false otherwise
	 */
	public boolean isWon() {
		// TODO
		return false; // TEMP
	}

	/**
	 * Query whether game has been lost
	 * 
	 * @return true if lost, false otherwise
	 */
	public boolean isLost() {
		// TODO
		return false; // TEMP
	}

	/**
	 * Query whether the current level has been cleared (if so, game will be paused
	 * until resume() is called )
	 * 
	 * @return true if current level is cleared and game is paused, false otherwise
	 */
	public boolean isLevelCleared() {
		// TODO
		return false; // TEMP
	}

	/**
	 * Pause the game
	 */
	public void pause() {
		// TODO
	}

	/**
	 * Resume the game
	 */
	public void resume() {
		// TODO
	}

	/**
	 * Called to get current collection of events
	 * 
	 * @return current collection of elements
	 */
	public Collection<Sprite> getCurrentElements() {
		return gameElements;
	}

	/**
	 * Place element of specified name at specified location
	 * 
	 * @param elementName
	 *            name of element which can be used by ElementFactory to construct a
	 *            TowerDefenseElement using properties / json data files
	 * @param x
	 *            xCoordinate where element was placed
	 * @param y
	 *            yCoordinate where element was placed
	 * @return the created element
	 */
	public Sprite placeElement(String elementName, double x, double y) {
		// TODO
		return null; // TEMP
	}
	
	public void saveGameState(String savedGameName) {
		playIOController.saveGameState(savedGameName, gameDescription, gameStatus, gameElements);
	}
	
	void setDescription(String newDescription) {
		gameDescription = newDescription;
	}
	
	void setStatus(Map<String, String> newStatus) {
		gameStatus = newStatus;
	}
	
	void setCurrentElements(Collection<Sprite> newElements) {
		gameElements = newElements;
	}
	
}
