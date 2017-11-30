package engine;

import engine.authoring_engine.AuthoringController;
import engine.behavior.movement.TrackingPoint;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import sprites.Sprite;
import sprites.SpriteFactory;
import util.GameConditionsReader;
import util.SerializationUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Encapsulates the shared fields and behavior between authoring and playing
 * controllers.
 *
 * @author radithya
 * @author Ben Schwennesen
 */
public abstract class AbstractGameController {

	public static final int DEFAULT_MAX_LEVELS = 1;
	public static final String VICTORY = "victory";
	public static final String DEFEAT = "defeat";

	private String gameName;
	private IOController ioController;
	private GameConditionsReader gameConditionsReader;

	// @Ben : Use list instead of map to facilitate 'fall-through' behavior for
	// deletion? i.e. when level 3 is deleted, level 4 should become level 3, level
	// 5 should become level 4, etc.

	private List<Map<String, Double>> levelStatuses = new ArrayList<>();
	private List<List<Sprite>> levelSpritesCache = new ArrayList<>();
	private List<Map<String, String>> levelConditions = new ArrayList<>();
	private List<String> levelDescriptions = new ArrayList<>();
	private List<Bank> levelBanks = new ArrayList<>();
	private List<Set<String>> levelInventories = new ArrayList<>();

	// TODO - move these into own object? Or have them in the sprite factory?
	private AtomicInteger spriteIdCounter;
	private Map<Integer, Sprite> spriteIdMap;

	private SpriteFactory spriteFactory;

	// this should be from a properties file? or handled in some better way?
	private final String DEFAULT_GAME_NAME = "untitled";

	private int currentLevel;

	public AbstractGameController() {
		SerializationUtils serializationUtils = new SerializationUtils();
		ioController = new IOController(serializationUtils);
		gameConditionsReader = new GameConditionsReader();
		initialize();
		gameName = DEFAULT_GAME_NAME;
		spriteIdCounter = new AtomicInteger();
		spriteIdMap = new HashMap<>();
		spriteFactory = new SpriteFactory();
	}

	/**
	 * Save the current state of the current level a game being played or authored.
	 *
	 * @param saveName
	 *            the name to assign to the save file
	 */
	public void saveGameState(File saveName) {
		// Note : saveName overrides previously set gameName if different - need to
		// handle this?
		// Serialize separately for every level
		Map<Integer, String> serializedLevelsData = new HashMap<>();
		for (int level = 1; level < getLevelStatuses().size(); level++) {
			serializedLevelsData.put(level,
					getIoController().getLevelSerialization(level, getLevelDescriptions().get(level),
							getLevelConditions().get(level), getLevelBanks().get(level), getLevelStatuses().get(level),
							levelSpritesCache.get(level), levelInventories.get(level)));
		}
		// Serialize map of level to per-level serialized data
		getIoController().saveGameStateForMultipleLevels(saveName, serializedLevelsData, isAuthoring());
	}

	/**
	 * Load the detailed state of the original authored game for a particular level,
	 * including high-level information and elements present.
	 *
	 * @param saveName
	 *            the name used to save the game authoring data
	 * @param level
	 *            the level of the game which should be loaded
	 * @throws FileNotFoundException
	 *             if the save name does not refer to an existing file
	 */
	public void loadOriginalGameState(String saveName, int level) throws FileNotFoundException {
		for (int levelToLoad = currentLevel; levelToLoad <= level; levelToLoad++) {
			loadLevelData(saveName, levelToLoad, true);
		}
		gameName = saveName;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public int getNumLevelsForGame(String gameName, boolean forOriginalGame) {
		try {
			// Want to load as author to get total number of levels for actual game
			return getIoController().getNumberOfLevelsForGame(gameName, forOriginalGame);
		} catch (FileNotFoundException e) {
			return 0;
		}
	}

	public Map<String, Map<String, String>> getAllDefinedTemplateProperties() {
		return getSpriteFactory().getAllDefinedTemplateProperties();
	}

	public int placeElement(String elementTemplateName, Point2D startCoordinates) {
		Map<String, Object> auxiliarySpriteConstructionObjects = getAuxiliarySpriteConstructionObjectMap(
				elementTemplateName, startCoordinates);
		Sprite sprite = spriteFactory.generateSprite(elementTemplateName, startCoordinates,
				auxiliarySpriteConstructionObjects);
		return cacheAndCreateIdentifier(elementTemplateName, sprite);
	}

	public Set<String> getInventory() {
		return getLevelInventories().get(getCurrentLevel());
	}
	
	public ImageView getRepresentationFromSpriteId(int spriteId) {
		return spriteIdMap.get(spriteId).getGraphicalRepresentation();
	}

	/**
	 * Get resources left for current level
	 * 
	 * @return map of resource name to quantity left
	 */
	public Map<String, Double> getStatus() {
		return getLevelStatuses().get(getCurrentLevel());
	}

	public Map<String, Double> getResourceEndowments() {
		return getLevelBanks().get(getCurrentLevel()).getResourceEndowments();
	}

	public Map<String, Map<String, Double>> getElementCosts() {
		return getLevelBanks().get(getCurrentLevel()).getUnitCosts();
	}

	/**
	 * Fetch all available game names and their corresponding descriptions
	 * 
	 * @return map where keys are game names and values are game descriptions
	 */
	public Map<String, String> getAvailableGames() {
		return ioController.getAvailableGames();
	}

	protected int placeElement(String elementTemplateName, Point2D startCoordinates, Collection<?>... auxiliaryArgs) {
		Map<String, Object> auxiliarySpriteConstructionObjects = getAuxiliarySpriteConstructionObjectMap(
				elementTemplateName, startCoordinates);
		for (Collection<?> auxiliaryArg : auxiliaryArgs) {
			auxiliarySpriteConstructionObjects.put(auxiliaryArg.getClass().getName(), auxiliaryArg);
		}
		Sprite sprite = spriteFactory.generateSprite(elementTemplateName, startCoordinates,
				auxiliarySpriteConstructionObjects);
		return cacheAndCreateIdentifier(elementTemplateName, sprite);
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
		if (level == getLevelSprites().size()) {
			initializeLevel();
		}
	}

	protected IOController getIoController() {
		return ioController;
	}
	
	protected GameConditionsReader getGameConditionsReader() {
		return gameConditionsReader;
	}

	protected List<Map<String, Double>> getLevelStatuses() {
		return levelStatuses;
	}

	protected List<Map<String, String>> getLevelConditions() {
		return levelConditions;
	}

	protected List<String> getLevelDescriptions() {
		return levelDescriptions;
	}

	protected List<List<Sprite>> getLevelSprites() {
		return levelSpritesCache;
	}

	protected List<Set<String>> getLevelInventories() {
		return levelInventories;
	}

	protected List<Bank> getLevelBanks() {
		return levelBanks;
	}
	
	protected int getCurrentLevel() {
		return currentLevel;
	}

	protected void loadLevelData(String saveName, int level, boolean originalGame) throws FileNotFoundException {
		loadGameStateElementsForLevel(saveName, level, originalGame);
		loadGameStateSettingsForLevel(saveName, level, originalGame);
		loadGameConditionsForLevel(saveName, level);
		loadGameDescriptionForLevel(saveName, level);
		loadGameBankForLevel(saveName, level, originalGame);
		loadGameInventoryElementsForLevel(saveName, level, originalGame);
	}

	protected SpriteFactory getSpriteFactory() {
		return spriteFactory;
	}

	protected Map<Integer, Sprite> getSpriteIdMap() {
		return spriteIdMap;
	}

	protected int cacheAndCreateIdentifier(String elementTemplateName, Sprite sprite) {
		spriteIdMap.put(spriteIdCounter.incrementAndGet(), sprite);
		cacheGeneratedSprite(sprite);
		return spriteIdCounter.get();
	}

	protected int getIdFromSprite(Sprite sprite) throws IllegalArgumentException {
		Map<Integer, Sprite> spriteIdMap = getSpriteIdMap();
		for (Integer id : spriteIdMap.keySet()) {
			if (spriteIdMap.get(id) == sprite) {
				return id;
			}
		}
		throw new IllegalArgumentException();
	}
	
	protected abstract void assertValidLevel(int level) throws IllegalArgumentException;

	private Collection<Sprite> loadGameStateElementsForLevel(String savedGameName, int level, boolean originalGame)
			throws FileNotFoundException {
		assertValidLevel(level);
		List<Sprite> loadedSprites = ioController.loadGameStateElements(savedGameName, level, originalGame);
		addOrSetLevelData(levelSpritesCache, loadedSprites, level);
		return loadedSprites;
	}

	private void loadGameStateSettingsForLevel(String savedGameName, int level, boolean originalGame)
			throws FileNotFoundException {
		assertValidLevel(level);
		Map<String, Double> loadedSettings = ioController.loadGameStateSettings(savedGameName, level, originalGame);
		addOrSetLevelData(levelStatuses, loadedSettings, level);
	}

	private void loadGameConditionsForLevel(String savedGameName, int level) throws FileNotFoundException {
		assertValidLevel(level);
		Map<String, String> loadedLevelConditions = ioController.loadGameConditions(savedGameName, level);
		addOrSetLevelData(levelConditions, loadedLevelConditions, level);
	}

	private void loadGameInventoryElementsForLevel(String savedGameName, int level, boolean originalGame)
			throws FileNotFoundException {
		assertValidLevel(level);
		Set<String> loadedInventories = ioController.loadGameInventories(savedGameName, level, originalGame);
		addOrSetLevelData(levelInventories, loadedInventories, level);
	}

	private void loadGameDescriptionForLevel(String savedGameName, int level) throws FileNotFoundException {
		assertValidLevel(level);
		addOrSetLevelData(levelDescriptions, ioController.loadGameDescription(savedGameName, level), level);
	}

	private void loadGameBankForLevel(String savedGameName, int level, boolean originalGame)
			throws FileNotFoundException {
		assertValidLevel(level);
		addOrSetLevelData(levelBanks, ioController.loadGameBank(savedGameName, level, originalGame), level);
	}

	private boolean isAuthoring() {
		// TODO - remove the forAuthoring param from ioController method so we don't
		// have to do this
		return this.getClass().equals(AuthoringController.class);
	}

	private <T> void addOrSetLevelData(List<T> allLevelData, T levelData, int level) {
		if (level == allLevelData.size()) {
			allLevelData.add(levelData);
		} else {
			allLevelData.set(level, levelData);
		}
	}

	private void initialize() {
		// To adjust for 1-indexing
		initializeLevel();
		setLevel(1);
	}
	
	private void initializeLevel() {
		getLevelStatuses().add(new HashMap<>());
		getLevelSprites().add(new ArrayList<>());
		getLevelInventories().add(new HashSet<>());
		getLevelDescriptions().add(new String());
		getLevelBanks().add(currentLevel > 0 ? getLevelBanks().get(currentLevel - 1).fromBank() : new Bank());
		initializeLevelConditions();
	}

	private int getNearestSpriteIdToPoint(Point2D coordinates) {
		double nearestDistance = Double.MAX_VALUE;
		int nearestSpriteId = -1;
		List<Sprite> spritesForLevel = getLevelSprites().get(getCurrentLevel());
		for (Sprite sprite : spritesForLevel) {
			double distanceToSprite = new Point2D(sprite.getX(), sprite.getY()).distance(coordinates);
			if (distanceToSprite < nearestDistance) {
				nearestDistance = distanceToSprite;
				nearestSpriteId = getIdFromSprite(sprite);
			}
		}
		return nearestSpriteId;
	}

	private Map<String, Object> getAuxiliarySpriteConstructionObjectMap(String elementTemplateName,
			Point2D startCoordinates) {
		int idOfSpriteToTrack = getNearestSpriteIdToPoint(startCoordinates);
		TrackingPoint targetLocation = spriteIdMap.get(idOfSpriteToTrack).getPositionForTracking();
		Point2D targetPoint = new Point2D(targetLocation.getCurrentX(), targetLocation.getCurrentY());
		Map<String, Object> auxiliarySpriteConstructionObjects = new HashMap<>();
		auxiliarySpriteConstructionObjects.put(targetLocation.getClass().getName(), targetLocation);
		auxiliarySpriteConstructionObjects.put(targetPoint.getClass().getName(), targetPoint);
		return auxiliarySpriteConstructionObjects;
	}
	
	private void initializeLevelConditions() {
		getLevelConditions().add(new HashMap<>());
		getLevelConditions().get(getCurrentLevel()).put(VICTORY, getDefaultVictoryCondition());
		getLevelConditions().get(getCurrentLevel()).put(DEFEAT, getDefaultDefeatCondition());
	}
	
	private String getDefaultVictoryCondition() {
		return new ArrayList<>(gameConditionsReader.getPossibleVictoryConditions()).get(0);
	}
	
	private String getDefaultDefeatCondition() {
		return new ArrayList<>(gameConditionsReader.getPossibleDefeatConditions()).get(0);
	}

}
