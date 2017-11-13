package play_engine;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Map;

import sprites.Sprite;
import util.SerializationUtils;

/**
 * Top-level play controller, gateway of front end GamePlayer to back
 * end logic and IO
 * 
 * @author radithya
 *
 */

public class PlayController {

	private SerializationUtils serializationUtils;
	private PlayIOController playIOController;
	private GameStateManager gameStateManager;
	// TODO - Initialize an ElementFactory instance when its ready
	// ElementFactory elementFactory;
	
	public PlayController() {
		serializationUtils = new SerializationUtils();
		playIOController = new PlayIOController(serializationUtils);
		gameStateManager = new GameStateManager(playIOController);//, elementFactory);
	}
	
	// TODO - interface methods
	/**
	 * Save state of currently played game - assumes only 1 game in play for a given
	 * engine at a time?
	 */
	public void saveGameState(String savedGameName) {
		gameStateManager.saveGameState(savedGameName);
	}

	// TODO - throw custom exception
	/**
	 * Load collection of elements for a previously saved game state
	 * 
	 * @param savedGameName
	 *            the name used to save the game state
	 * @return a collection of elements which can be saved in the engine and passed
	 *         to the front end
	 */
	public Collection<Sprite> loadGameStateElements(String savedGameName) throws FileNotFoundException {
		Collection<Sprite> loadedSprites = playIOController.loadGameStateElements(savedGameName);
		gameStateManager.setCurrentElements(loadedSprites);
		return loadedSprites;
	}

	// TODO - throw custom exception
	/**
	 * Load top-level game status settings (lives left, resources left, etc.) for a
	 * previously saved game state
	 * 
	 * @param savedGameName
	 *            the name used to save the game state
	 * @return map of state keys to values
	 */
	public Map<String, String> loadGameStateSettings(String savedGameName) throws FileNotFoundException {
		Map<String, String> loadedSettings = playIOController.loadGameStateSettings(savedGameName);
		gameStateManager.setStatus(loadedSettings);
		return loadedSettings;
	}
	
	/**
	 * Fetch all available game names and their corresponding descriptions
	 * 
	 * @return map where keys are game names and values are game descriptions
	 */
	public Map<String, String> getAvailableGames() {
		return playIOController.getAvailableGames();
	}

	
	/**
	 * Run the game loop for the given number of cycles
	 * 
	 * @param cycles
	 *            the number of cycles
	 */
	public void update() {
		gameStateManager.update();
	}

	/**
	 * Get lives left
	 * 
	 * @return number of lives left
	 */
	public int getLives() {
		return gameStateManager.getLives(); // TEMP
	}

	/**
	 * Retrieve the amount of each resource left
	 * 
	 * @return map of resource name to amount left
	 */
	public Map<String, Integer> getResources() {
		return gameStateManager.getResources();
	}

	/**
	 * Query the current level of the game
	 * 
	 * @return the integer corresponding to the game's current level
	 */
	public int getCurrentLevel() {
		return gameStateManager.getCurrentLevel();
	}
	
	/**
	 * Query current play status (lives, kills, resources, all top-level metrics)
	 * @return map of parameter name to value
	 */
	public Map<String, String> getStatus() {
		return gameStateManager.getStatus();
	}

	/**
	 * Query whether the game is currently in play
	 * 
	 * @return true if in play, false if over / paused
	 */
	public boolean isInPlay() {
		return gameStateManager.isInPlay();
	}

	/**
	 * Query whether game has been won
	 * 
	 * @return true if won, false otherwise
	 */
	public boolean isWon() {
		return gameStateManager.isWon();
	}

	/**
	 * Query whether game has been lost
	 * 
	 * @return true if lost, false otherwise
	 */
	public boolean isLost() {
		return gameStateManager.isLost();
	}

	/**
	 * Query whether the current level has been cleared (if so, game will be paused
	 * until resume() is called )
	 * 
	 * @return true if current level is cleared and game is paused, false otherwise
	 */
	public boolean isLevelCleared() {
		return gameStateManager.isLevelCleared();
	}

	/**
	 * Pause the game
	 */
	public void pause() {
		gameStateManager.pause();
	}

	/**
	 * Resume the game
	 */
	public void resume() {
		gameStateManager.resume();
	}

	/**
	 * Called to get current collection of events
	 * 
	 * @return current collection of elements
	 */
	public Collection<Sprite> getCurrentElements() {
		return gameStateManager.getCurrentElements();
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
		return gameStateManager.placeElement(elementName, x, y);
	}

}
