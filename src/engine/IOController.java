package engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.GamePersistence;
import sprites.Sprite;
import util.SerializationUtils;

/**
 * Gateway of authoring and play modules to I/O through engine Represents shared
 * logic for
 * 
 * @author radithya
 *
 */
public class IOController {

	public static final String AUTHORING_GAMES_FOLDER = "authoring/";
	public static final String PLAY_GAMES_FOLDER = "play/";

	private SerializationUtils serializationUtils;
	private GamePersistence gamePersistence;
	// TODO - uncomment when ElementFactory is ready
	// private ElementFactory elementFactory;

	public IOController(SerializationUtils serializationUtils) {
		this.serializationUtils = serializationUtils;
		gamePersistence = new GamePersistence();
	}

	/**
	 * Save game state
	 * 
	 * @param savedGameName
	 *            name for game state to be saved to
	 * @param gameDescription
	 *            description for game
	 * @param currentLevel
	 *            level for which game state is saved
	 * @param status
	 *            top-level status key-value pairs for heads-up display (player) or
	 *            settings (authoring)
	 * @param elements
	 *            set of elements in game to be serialized
	 * @param forAuthoring
	 *            true if for authoring, false if for play - TODO - more flexible
	 *            approach? reflection?
	 */
	public void saveGameState(String savedGameName, String gameDescription, int currentLevel,
			Map<String, String> status, Collection<Sprite> elements, boolean forAuthoring) {
		// Determine correct folder
		String prefix = forAuthoring ? AUTHORING_GAMES_FOLDER : PLAY_GAMES_FOLDER;
		// First extract string from file through io module
		String serializedGameState = serializationUtils.serializeGameData(prefix + gameDescription, currentLevel, status, elements);
		gamePersistence.saveGameState(savedGameName, serializedGameState);
	}

	// TODO - throw custom exception
	/**
	 * Load collection of elements for a previously saved game state
	 * 
	 * @param savedGameName
	 *            the name used to save the game state
	 * @param forAuthoring
	 *            true if for authoring, false if for play - TODO - more flexible
	 *            approach? reflection?
	 * @return a collection of elements which can be saved in the engine and passed
	 *         to the front end
	 * @throws FileNotFoundException
	 */
	public Collection<Sprite> loadGameStateElements(String savedGameName, boolean forAuthoring)
			throws FileNotFoundException {
		// Determine correct folder
		String prefix = forAuthoring ? AUTHORING_GAMES_FOLDER : PLAY_GAMES_FOLDER;
		// First extract string from file through io module
		String serializedGameData = gamePersistence.loadGameState(prefix + savedGameName);
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
	 * @param forAuthoring
	 *            true if for authoring, false if for play - TODO - more flexible
	 *            approach? reflection?
	 * @return map of state keys to values
	 * @throws FileNotFoundException
	 */
	public Map<String, String> loadGameStateSettings(String savedGameName, boolean forAuthoring)
			throws FileNotFoundException {
		// Determine correct folder
		String prefix = forAuthoring ? AUTHORING_GAMES_FOLDER : PLAY_GAMES_FOLDER;
		// First extract string from file through io module
		String serializedGameData = gamePersistence.loadGameState(prefix + savedGameName);
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
		Map<String, String> authoredGameSerializations = getAuthoredGameSerializations();
		Map<String, String> availableGames = new HashMap<>();
		// for each file, extract description through util module
		for (String authoredGameName : authoredGameSerializations.keySet()) {
			availableGames.put(authoredGameName,
					serializationUtils.deserializeGameDescription(authoredGameSerializations.get(authoredGameName)));
		}
		return availableGames;
	}

	/**
	 * 
	 * @param fileName
	 *            chosen file name for saved authoring game state, create if file
	 *            does not exist, overwrite if file exists
	 * @param serializedRepresentation
	 *            of current game
	 */
	public void saveAuthoringGameState(String fileName, String serializedRepresentation) {
		gamePersistence.saveGameState(AUTHORING_GAMES_FOLDER + fileName, serializedRepresentation);
	}

	/**
	 * 
	 * @param fileName
	 *            chosen file name for saved play game state, create if file does
	 *            not exist, overwrite if file exists
	 * @param serializedRepresentation
	 *            of current game
	 */
	public void savePlayGameState(String fileName, String serializedRepresentation) {
		gamePersistence.saveGameState(PLAY_GAMES_FOLDER + fileName, serializedRepresentation);
	}

	// TODO - throw custom exception
	/**
	 * 
	 * @param fileName
	 *            file name for authored game to be loaded
	 */
	public String loadAuthoringGameState(String fileName) throws FileNotFoundException {
		return gamePersistence.loadGameState(AUTHORING_GAMES_FOLDER + fileName);
	}

	// TODO - throw custom exception
	/**
	 * 
	 * @param fileName
	 *            file name for played game to be loaded
	 */
	public String loadPlayGameState(String fileName) throws FileNotFoundException {
		return gamePersistence.loadGameState(PLAY_GAMES_FOLDER + fileName);
	}

	/**
	 * Retrieve a map of authored game names to their description
	 * 
	 * @return map of {game_name : game_description}
	 */
	public Map<String, String> getAuthoredGameSerializations() {
		Map<String, String> authoredGameSerializationMap = new HashMap<>();
		// iterate over file names in authored_games folder, serialize each
		File authoredGamesDirectory = new File(AUTHORING_GAMES_FOLDER);
		File[] authoredGames = authoredGamesDirectory.listFiles();
		if (authoredGames == null) {
			throw new IllegalStateException();
		}
		for (File authoredGame : authoredGames) {
			String authoredGameName = authoredGame.getName();
			// FileNotFoundException would not make sense here as we are iterating through
			// files found in directory, so safe to ignore
			try {
				authoredGameSerializationMap.put(authoredGameName, loadAuthoringGameState(authoredGameName));
			} catch (FileNotFoundException e) {
				continue;
			}
		}
		return authoredGameSerializationMap;
	}

}
