package engine;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Map;

import com.sun.xml.internal.messaging.saaj.soap.impl.ElementFactory;
import sprites.Sprite;
import sprites.SpriteFactory;
import util.SerializationUtils;

public abstract class GameController {

	private SerializationUtils serializationUtils;
	private IOController ioController;

	public GameController() {
		serializationUtils = new SerializationUtils();
		ioController = new IOController(serializationUtils, new SpriteFactory());
	}

	protected abstract StateManager getStateManager();

	// TODO - a not-so-lame way?
	/**
	 * Whether this controller is the authoring environment or player
	 * 
	 * @return true if authoring, false if player
	 */
	public abstract boolean isAuthoring();

	// TODO - interface methods
	/**
	 * Save state of currently played game - assumes only 1 game in play for a given
	 * engine at a time?
	 */
	public void saveGameState(String savedGameName) {
		getStateManager().saveGameState(savedGameName);
	}

	// TODO - throw custom exception
	/**
	 * Load collection of elements for a previously saved game state
	 * 
	 * @param savedGameName
	 *            the name used to save the game state
	 * @param level
	 *            level of the game to load
	 * @return a collection of elements which can be saved in the engine and passed
	 *         to the front end
	 */
	public Collection<Sprite> loadGameStateElements(String savedGameName, int level) throws FileNotFoundException {
		Collection<Sprite> loadedSprites = ioController.loadGameStateElements(savedGameName, level, isAuthoring());
		getStateManager().setCurrentElements(loadedSprites);
		return loadedSprites;
	}

	// TODO - throw custom exception
	/**
	 * Load top-level game status settings (lives left, resources left, etc.) for a
	 * previously saved game state
	 * 
	 * @param savedGameName
	 *            the name used to save the game state
	 * @param level
	 *            level of the game to load
	 * @return map of state keys to values
	 */
	public Map<String, String> loadGameStateSettings(String savedGameName, int level) throws FileNotFoundException {
		Map<String, String> loadedSettings = ioController.loadGameStateSettings(savedGameName, level, isAuthoring());
		getStateManager().setStatus(loadedSettings);
		return loadedSettings;
	}

	/**
	 * Fetch all available game names and their corresponding descriptions
	 * 
	 * @return map where keys are game names and values are game descriptions
	 */
	public Map<String, String> getAvailableGames() {
		return ioController.getAvailableGames();
	}

	/**
	 * Query current play status (lives, kills, resources, all top-level metrics)
	 * 
	 * @return map of parameter name to value
	 */
	public Map<String, String> getStatus() {
		return getStateManager().getStatus();
	}

	/**
	 * Called to get current collection of events
	 * 
	 * @return current collection of elements
	 */
	public Collection<Sprite> getCurrentElements() {
		return getStateManager().getCurrentElements();
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
		return getStateManager().placeElement(elementName, x, y);
	}

	/**
	 * Set the current level to the given level, constraints on valid levels depend
	 * on authoring / play use-case
	 * 
	 * @param level
	 * @throws IllegalArgumentException
	 */
	public void setCurrentLevel(int level) throws IllegalArgumentException {
		getStateManager().setCurrentLevel(level);
	}

	protected IOController getIOController() {
		return ioController;
	}

}
