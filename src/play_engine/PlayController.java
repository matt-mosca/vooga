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

}
