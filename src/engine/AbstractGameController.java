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
 * Encapsulates the shared fields and behavior between authoring and playing controllers.
 *
 * @author Ben Schwennesen
 */
public abstract class AbstractGameController {

    private String gameName;
    private String gameDescription;
    private IOController ioController;

    private Map<Integer, Map<String, String>> levelStatuses;

    private Map<Integer, List<Sprite>> levelSpritesCache = new HashMap<>();

    // this should be from a properties file? or handled in some better way?
    private final String DEFAULT_GAME_NAME = "untitled";

    private int currentLevel;

    public AbstractGameController() {
        SerializationUtils serializationUtils = new SerializationUtils();
        ioController = new IOController(serializationUtils);
        levelStatuses = new HashMap<>();
        setLevel(1);
        gameDescription = "";
        gameName = DEFAULT_GAME_NAME;

    }

    private boolean isAuthoring() {
        // TODO - remove the forAuthoring param from ioController method so we don't have to do this
        return this.getClass().equals(AuthoringController.class);
    }

    /**
     * Save the current state of the current level a game being played or authored.
     *
     * @param saveName the name to assign to the save file
     */
    public void saveGameState(String saveName) {
        // Serialize separately for every level
        Map<Integer, String> serializedLevelsData = new HashMap<>();
        for (int level : getLevelStatuses().keySet()) {
            serializedLevelsData.put(level, getIoController().getLevelSerialization(level, gameDescription,
                    getLevelStatuses().get(level), levelSpritesCache.getOrDefault(level, new ArrayList<>())));
        }
        // Serialize map of level to per-level serialized data
        getIoController().saveGameStateForMultipleLevels(saveName, serializedLevelsData, isAuthoring());

    }

    /**
     * Load collection of elements for a previously saved game state
     *
     * @param savedGameName
     *            the name used to save the game state
     * @param level
     *            level of the game to load
     * @return collection of game elements for the desired level
     */
    protected Collection<Sprite> loadGameStateElements(String savedGameName, int level) throws FileNotFoundException {
        List<Sprite> loadedSprites = ioController.loadGameStateElements(savedGameName, level, isAuthoring());
        levelSpritesCache.put(level, loadedSprites);
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
     */
    protected void loadGameStateSettings(String savedGameName, int level) throws FileNotFoundException {
        Map<String, String> loadedSettings = ioController.loadGameStateSettings(savedGameName, level, isAuthoring());
        levelStatuses.put(level, loadedSettings);
    }

    protected void cacheGeneratedSprite(Sprite sprite) {
        List<Sprite> levelSprites = levelSpritesCache.getOrDefault(currentLevel, new ArrayList<>());
        levelSprites.add(sprite);
        levelSpritesCache.put(currentLevel, levelSprites);
    }

    public Map<Integer, List<Sprite>> getLevelSpritesMap() {
        return levelSpritesCache;
    }

    public String getGameName() {
        return gameName;
    }

    protected IOController getIoController() {
        return ioController;
    }

    protected Map<Integer, Map<String, String>> getLevelStatuses() {
        return levelStatuses;
    }

    protected int getCurrentLevel() {
        return currentLevel;
    }


    /**
     * Create a new level for the game being authored. Saves the state of the current level being authored when the
     * transition occurs.
     *
     * @param level the number associated with the new level
     */
    protected void setLevel(int level) {
        currentLevel = level;
    }

    public void setGameDescription(String gameDescription) {
        this.gameDescription = gameDescription;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
