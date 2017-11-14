package play_engine;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.GamePersistence;
import sprites.Sprite;
import util.SerializationUtils;

public class PlayIOController {

	private SerializationUtils serializationUtils;
	private GamePersistence playPersistence;

	public PlayIOController(SerializationUtils serializationUtils) {
		this.serializationUtils = serializationUtils;
		playPersistence = new GamePersistence();
	}

	public void saveGameState(String savedGameName, String gameDescription, Map<String, String> status,
			Collection<Sprite> elements) {
		String serializedGameState = serializationUtils.serializeGameData(gameDescription, status, elements);
		playPersistence.savePlayGameState(savedGameName, serializedGameState);
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
		// First extract string from file through io module
		String serializedGameData = playPersistence.loadPlayGameState(savedGameName);
		// deserialize string into map through utils module
		Map<String, List<Sprite>> spritesMap = serializationUtils.deserializeGameSprites(serializedGameData);
		// TODO - retrieve collection of Sprites from map through ElementFactory
		// TODO - return this collection
		return null;// TEMP
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
		// First extract string from file through io module
		String serializedGameData = playPersistence.loadPlayGameState(savedGameName);
		// deserialize string into map through utils module
		return serializationUtils.deserializeGameStatus(serializedGameData);
	}

	/**
	 * Fetch all available game names and their corresponding descriptions
	 * 
	 * @return map where keys are game names and values are game descriptions
	 */
	public Map<String, String> getAvailableGames() {
		// retrieve set of files from authoring folder through io module
		Map<String, String> authoredGameSerializations = playPersistence.getAuthoredGameSerializations();
		Map<String, String> availableGames = new HashMap<>();
		// for each file, extract description through util module
		for (String authoredGameName : authoredGameSerializations.keySet()) {
			availableGames.put(authoredGameName,
					serializationUtils.deserializeGameDescription(authoredGameSerializations.get(authoredGameName)));
		}
		return availableGames;
	}

}
