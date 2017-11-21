package engine;

import engine.authoring_engine.AuthoringController;
import sprites.Sprite;
import sprites.SpriteFactory;
import util.SerializationUtils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Encapsulates the shared fields and behavior between authoring and playing
 * controllers.
 *
 * @author radithya
 * @author Ben Schwennesen
 */
public abstract class AbstractGameController {

	private String gameName;
	private String gameDescription;
	private IOController ioController;

	// @Ben : Use list instead of map to facilitate 'fall-through' behavior for
	// deletion? i.e. when level 3 is deleted, level 4 should become level 3, level
	// 5 should become level 4, etc.

	// private Map<Integer, Map<String, String>> levelStatuses;
	// private Map<Integer, List<Sprite>> levelSpritesCache = new HashMap<>();
	private List<Map<String, String>> levelStatuses = new ArrayList<>();
	private List<List<Sprite>> levelSpritesCache = new ArrayList<>();
	private List<Map<String, String>> levelConditions = new ArrayList<>();

	// this should be from a properties file? or handled in some better way?
	private final String DEFAULT_GAME_NAME = "untitled";

	private int currentLevel;
	private int numLevelsForGame;

	public AbstractGameController() {
		SerializationUtils serializationUtils = new SerializationUtils();
		ioController = new IOController(serializationUtils);
		setLevel(1);
		gameDescription = "";
		gameName = DEFAULT_GAME_NAME;
	}

	/**
	 * Save the current state of the current level a game being played or authored.
	 *
	 * @param saveName
	 *            the name to assign to the save file
	 */
	public void saveGameState(String saveName) {
		// Serialize separately for every level
		Map<Integer, String> serializedLevelsData = new HashMap<>();
		for (int level = 1; level < getLevelStatuses().size(); level++) {
			serializedLevelsData.put(level, getIoController().getLevelSerialization(level, gameDescription,
					getLevelConditions().get(level), getLevelStatuses().get(level), levelSpritesCache.get(level)));
		}
		// Serialize map of level to per-level serialized data
		getIoController().saveGameStateForMultipleLevels(saveName, serializedLevelsData, isAuthoring());
	}

	public List<List<Sprite>> getLevelSprites() {
		return levelSpritesCache;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameDescription(String gameDescription) {
		this.gameDescription = gameDescription;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public int getNumLevelsForGame() {
		try {
			return getIoController().getNumLevelsForGame(getGameName(), isAuthoring());			
		} catch (FileNotFoundException e) {
			return 0;
		}
	}

	/**
	 * Load collection of elements for a previously saved game state for a for all
	 * levels up to and including the stated level
	 *
	 * @param savedGameName
	 *            the name used to save the game state
	 * @param level
	 *            level of the game to load
	 * @return collection of game elements for the desired level
	 */
	protected Collection<Sprite> loadGameStateElements(String savedGameName, int level) throws FileNotFoundException {
		Collection<Sprite> loadedSprites = null;
		for (int levelToLoad = currentLevel; levelToLoad <= level; levelToLoad++) {
			loadedSprites = loadGameStateElementsForLevel(savedGameName, level);
		}
		return loadedSprites;
	}

	// TODO - throw custom exception
	/**
	 * Load top-level game status settings (lives left, resources left, etc.) for a
	 * previously saved game state for all levels up to and including the stated
	 * level
	 *
	 * @param savedGameName
	 *            the name used to save the game state
	 * @param level
	 *            level of the game to load
	 */
	protected void loadGameStateSettings(String savedGameName, int level) throws FileNotFoundException {
		for (int levelToLoad = currentLevel; levelToLoad <= level; levelToLoad++) {
			loadGameStateSettingsForLevel(savedGameName, levelToLoad);
		}
	}

	protected void loadGameConditions(String savedGameName, int level) throws FileNotFoundException {
		for (int levelToLoad = currentLevel; levelToLoad <= level; levelToLoad++) {
			loadGameConditionsForLevel(savedGameName, levelToLoad);
		}
	}

	protected void cacheGeneratedSprite(Sprite sprite) {
		List<Sprite> levelSprites = levelSpritesCache.get(currentLevel);
		levelSprites.add(sprite);
	}

	/**
	 * Create a new level for the game being authored. Saves the state of the
	 * current level being authored when the transition occurs.
	 *
	 * @param level
	 *            the number associated with the new level
	 */
	protected void setLevel(int level) {
		assertValidLevel(level);
		currentLevel = level;
		if (currentLevel > getLevelStatuses().size()) {
			getLevelStatuses().add(new HashMap<>());
			getLevelSprites().add(new ArrayList<>());
			getLevelConditions().add(new HashMap<>());
		}
	}

	protected IOController getIoController() {
		return ioController;
	}

	protected List<Map<String, String>> getLevelStatuses() {
		return levelStatuses;
	}

	protected List<Map<String, String>> getLevelConditions() {
		return levelConditions;
	}

	protected int getCurrentLevel() {
		return currentLevel;
	}

	protected abstract void assertValidLevel(int level) throws IllegalArgumentException;

	private Collection<Sprite> loadGameStateElementsForLevel(String savedGameName, int level)
			throws FileNotFoundException {
		assertValidLevel(level);
		List<Sprite> loadedSprites = ioController.loadGameStateElements(savedGameName, level, isAuthoring());
		levelSpritesCache.set(level, loadedSprites);
		return loadedSprites;
	}

	private void loadGameStateSettingsForLevel(String savedGameName, int level) throws FileNotFoundException {
		assertValidLevel(level);
		Map<String, String> loadedSettings = ioController.loadGameStateSettings(savedGameName, level, isAuthoring());
		levelStatuses.set(level, loadedSettings);
	}

	private void loadGameConditionsForLevel(String savedGameName, int level) throws FileNotFoundException {
		assertValidLevel(level);
		Map<String, String> loadedLevelConditions = ioController.loadGameConditions(savedGameName, level,
				isAuthoring());
		levelConditions.set(level, loadedLevelConditions);
	}

	private boolean isAuthoring() {
		// TODO - remove the forAuthoring param from ioController method so we don't
		// have to do this
		return this.getClass().equals(AuthoringController.class);
	}

}
