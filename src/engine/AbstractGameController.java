package engine;

import engine.authoring_engine.AuthoringController;
import engine.game_elements.GameElement;
import engine.game_elements.GameElementFactory;
import javafx.geometry.Point2D;
import networking.protocol.PlayerServer.ElementCost;
import networking.protocol.PlayerServer.Inventory;
import networking.protocol.PlayerServer.LevelInitialized;
import networking.protocol.PlayerServer.NewSprite;
import networking.protocol.PlayerServer.TemplateProperties;
import engine.game_elements.GameElementUpgrader;
import util.GameConditionsReader;
import util.io.SerializationUtils;
import util.io.SpriteTemplateIoHandler;
import util.protocol.ServerMessageUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Encapsulates the shared fields and behavior between authoring and playing
 * controllers.
 *
 * @author radithya
 * @author Ben Schwennesen
 */
public abstract class AbstractGameController implements AbstractGameModelController {

	protected static final int DEFAULT_MAX_LEVELS = 1;
	protected static final String VICTORY = "victory";
	protected static final String DEFEAT = "defeat";

	private final int ASSUMED_PLAYER_ID = -1;

	private SpriteTemplateIoHandler spriteTemplateIoHandler;
	private SpriteQueryHandler spriteQueryHandler;

	private String gameName;
	private IOController ioController;
	private GameConditionsReader gameConditionsReader;
	private ServerMessageUtils serverMessageUtils;

	private List<Map<String, Double>> levelStatuses = new ArrayList<>();
	private List<List<GameElement>> levelSpritesCache = new ArrayList<>();
	private List<Map<String, String>> levelConditions = new ArrayList<>();
	private List<String> levelDescriptions = new ArrayList<>();
	private List<Bank> levelBanks = new ArrayList<>();
	private List<Set<String>> levelInventories = new ArrayList<>();
	private List<List<GameElement>> levelWaves = new ArrayList<>();

	// TODO - move these into own object? Or have them in the sprite factory?
	private AtomicInteger spriteIdCounter;
	private Map<Integer, GameElement> spriteIdMap;

	private GameElementFactory gameElementFactory;
	private GameElementUpgrader gameElementUpgrader;

	// this should be from a properties file? or handled in some better way?
	private final String DEFAULT_GAME_NAME = "untitled";

	private int currentLevel;
	private SerializationUtils serializationUtils;

	public AbstractGameController() {
		this.serializationUtils = new SerializationUtils();
		ioController = new IOController(serializationUtils);
		gameConditionsReader = new GameConditionsReader();
		serverMessageUtils = new ServerMessageUtils();
		initialize();
		gameName = DEFAULT_GAME_NAME;
		spriteIdCounter = new AtomicInteger();
		spriteIdMap = new HashMap<>();
		gameElementFactory = new GameElementFactory(serializationUtils);
		gameElementUpgrader = new GameElementUpgrader(gameElementFactory);
		spriteTemplateIoHandler = new SpriteTemplateIoHandler(serializationUtils);
		spriteQueryHandler = new SpriteQueryHandler();
	}

	/**
	 * Save the current state of the current level a game being played or authored.
	 *
	 * @param saveName
	 *            the name to assign to the save file
	 */
	@Override
	public void saveGameState(File saveName) {
		// Note : saveName overrides previously set gameName if different - need to
		// handle this?
		// Serialize separately for every level
		Map<Integer, String> serializedLevelsData = new HashMap<>();
		for (int level = 1; level < getLevelStatuses().size(); level++) {
			serializedLevelsData.put(level,
					getIoController().getLevelSerialization(level, getLevelDescriptions().get(level),
							getLevelConditions().get(level), getLevelBanks().get(level), getLevelStatuses().get(level),
							levelSpritesCache.get(level), levelInventories.get(level), levelWaves.get(level)));
		}
		// Serialize map of level to per-level serialized data
		getIoController().saveGameStateForMultipleLevels(saveName, serializedLevelsData, isAuthoring());
		spriteTemplateIoHandler.exportSpriteTemplates(saveName.getName(),
				gameElementFactory.getAllDefinedTemplateProperties());
		spriteTemplateIoHandler.exportSpriteUpgrades(saveName.getName(),
				gameElementUpgrader.getSpriteUpgradesForEachTemplate());
	}

	/**
	 * Load the detailed state of the original authored game for a particular level,
	 * including high-level information and elements present.
	 *
	 * @param saveName
	 *            the name used to save the game authoring data
	 * @param level
	 *            the level of the game which should be loaded
	 * @throws IOException
	 *             if the save name does not refer to existing files
	 */
	@Override
	public LevelInitialized loadOriginalGameState(String saveName, int level) throws IOException {
		// TODO - remove old level
		for (int levelToLoad = currentLevel; levelToLoad <= level; levelToLoad++) {
			loadLevelData(saveName, levelToLoad, true);
		}
		gameName = saveName;
		gameElementFactory.loadSpriteTemplates(spriteTemplateIoHandler.loadElementTemplates(gameName));
		gameElementUpgrader.loadSpriteUpgrades(spriteTemplateIoHandler.loadElementUpgrades(gameName));
		return packageInitialState();
	}

	public Inventory packageInventory() {
		return getServerMessageUtils().packageInventory(getInventory());
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	@Override
	public int getNumLevelsForGame(String gameName, boolean forOriginalGame) {
		try {
			// Want to load as author to get total number of levels for actual game
			return getIoController().getNumberOfLevelsForGame(gameName, forOriginalGame);
		} catch (FileNotFoundException e) {
			return 0;
		}
	}

	@Override
	public Map<String, Object> getTemplateProperties(String elementName) throws IllegalArgumentException {
		return getGameElementFactory().getTemplateProperties(elementName);
	}

	@Override
	public Map<String, Map<String, Object>> getAllDefinedTemplateProperties() {
		return getGameElementFactory().getAllDefinedTemplateProperties();
	}

	@Override
	public NewSprite placeElement(String elementTemplateName, Point2D startCoordinates) throws ReflectiveOperationException {
		Map<String, Object> auxiliarySpriteConstructionObjects = spriteQueryHandler
				.getAuxiliarySpriteConstructionObjectMap(ASSUMED_PLAYER_ID, startCoordinates,
						levelSpritesCache.get(currentLevel));
		auxiliarySpriteConstructionObjects.put("startPoint", startCoordinates);
		GameElement gameElement = gameElementFactory.generateElement(elementTemplateName,
				auxiliarySpriteConstructionObjects);
		gameElementUpgrader.registerNewSprite(elementTemplateName, gameElement);
		int spriteId = cacheAndCreateIdentifier(elementTemplateName, gameElement);
		return serverMessageUtils.packageNewSprite(gameElement, spriteId);
	}

	@Override
	public int getCurrentLevel() {
		return currentLevel;
	}
	
	@Override
	public Set<String> getInventory() {
		return getLevelInventories().get(getCurrentLevel());
	}

	/**
	 * Get resources left for current level
	 * 
	 * @deprecated
	 * @return map of resource name to quantity left
	 */
	@Override
	public Map<String, Double> getStatus() {
		return getLevelStatuses().get(getCurrentLevel());
	}

	@Override
	public Map<String, Double> getResourceEndowments() {
		return getLevelBanks().get(getCurrentLevel()).getResourceEndowments();
	}

	@Override
	public Map<String, Map<String, Double>> getElementCosts() {
		return getLevelBanks().get(getCurrentLevel()).getUnitCosts();
	}

	/**
	 * Fetch all available game names and their corresponding descriptions
	 * 
	 * @return map where keys are game names and values are game descriptions
	 */
	@Override
	public Map<String, String> getAvailableGames() throws IllegalStateException {
		return ioController.getAvailableGames();
	}
	
	/**
	 * Create a new level for the game being authored. Saves the state of the
	 * current level being authored when the transition occurs.
	 *
	 * @param level
	 *            the number associated with the new level
	 */
	public void setLevel(int level) {
		assertValidLevel(level);
		currentLevel = level;
		if (level == getLevelSprites().size()) {
			initializeLevel();
		}
	}

	public TemplateProperties packageTemplateProperties(String templateName) {
		return getServerMessageUtils().packageTemplateProperties(templateName,
				serializationUtils.serializeElementTemplate(getTemplateProperties(templateName)));
	}

	public Collection<TemplateProperties> packageAllTemplateProperties() {
		Map<String, Map<String, String>> serializedTemplates = new HashMap<>();
		Map<String, Map<String, Object>> templates = getAllDefinedTemplateProperties();
		for (String templateName : templates.keySet()) {
			serializedTemplates.put(templateName,
					serializationUtils.serializeElementTemplate(templates.get(templateName)));
		}
		return getServerMessageUtils().packageAllTemplateProperties(serializedTemplates);
	}

	public Collection<ElementCost> packageAllElementCosts() {
		return getServerMessageUtils().packageAllElementCosts(getElementCosts());
	}

	public Collection<NewSprite> packageLevelElements(int level) {
		return getServerMessageUtils().packageNewSprites(getFilteredSpriteIdMap(getLevelSprites().get(level)));
	}

	protected int placeElement(String elementTemplateName, Point2D startCoordinates, Collection<?>... auxiliaryArgs)
			throws ReflectiveOperationException {
		Map<String, Object> auxiliarySpriteConstructionObjects = spriteQueryHandler
				.getAuxiliarySpriteConstructionObjectMap(ASSUMED_PLAYER_ID, startCoordinates,
						levelSpritesCache.get(currentLevel));
		for (Collection<?> auxiliaryArg : auxiliaryArgs) {
			auxiliarySpriteConstructionObjects.put(auxiliaryArg.getClass().getName(), auxiliaryArg);
		}
		auxiliarySpriteConstructionObjects.put("startPoint", startCoordinates);
		GameElement gameElement = gameElementFactory.generateElement(elementTemplateName,
				auxiliarySpriteConstructionObjects);
		return cacheAndCreateIdentifier(elementTemplateName, gameElement);
	}

	protected void cacheGeneratedSprite(GameElement gameElement) {
		List<GameElement> levelGameElements = levelSpritesCache.get(currentLevel);
		levelGameElements.add(gameElement);
	}

	protected IOController getIoController() {
		return ioController;
	}

	protected GameConditionsReader getGameConditionsReader() {
		return gameConditionsReader;
	}

	protected ServerMessageUtils getServerMessageUtils() {
		return serverMessageUtils;
	}

	protected SpriteTemplateIoHandler getSpriteTemplateIoHandler() {
		return spriteTemplateIoHandler;
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

	protected List<List<GameElement>> getLevelSprites() {
		return levelSpritesCache;
	}

	protected List<Set<String>> getLevelInventories() {
		return levelInventories;
	}

	protected List<Bank> getLevelBanks() {
		return levelBanks;
	}
	
	protected List<List<GameElement>> getLevelWaves() {
		return levelWaves;
	}

	protected void loadLevelData(String saveName, int level, boolean originalGame) throws FileNotFoundException {
		loadGameStateElementsForLevel(saveName, level, originalGame);
		loadGameStateSettingsForLevel(saveName, level, originalGame);
		loadGameConditionsForLevel(saveName, level);
		loadGameDescriptionForLevel(saveName, level);
		loadGameBankForLevel(saveName, level, originalGame);
		loadGameInventoryElementsForLevel(saveName, level, originalGame);
		loadGameWavesForLevel(saveName, level);
	}

	protected GameElementFactory getGameElementFactory() {
		return gameElementFactory;
	}

	protected GameElementUpgrader getGameElementUpgrader() {
		return gameElementUpgrader;
	}

	protected SpriteQueryHandler getSpriteQueryHandler() {
		return spriteQueryHandler;
	}

	protected Map<Integer, GameElement> getSpriteIdMap() {
		return spriteIdMap;
	}

	protected int cacheAndCreateIdentifier(String elementTemplateName, GameElement gameElement) {
		spriteIdMap.put(spriteIdCounter.incrementAndGet(), gameElement);
		cacheGeneratedSprite(gameElement);
		return spriteIdCounter.get();
	}

	protected int cacheAndCreateIdentifier(GameElement gameElement) {
		spriteIdMap.put(spriteIdCounter.incrementAndGet(), gameElement);
		cacheGeneratedSprite(gameElement);
		return spriteIdCounter.get();
	}

	protected int getIdFromSprite(GameElement gameElement) throws IllegalArgumentException {
		Map<Integer, GameElement> spriteIdMap = getSpriteIdMap();
		for (Integer id : spriteIdMap.keySet()) {
			if (spriteIdMap.get(id) == gameElement) {
				return id;
			}
		}
		throw new IllegalArgumentException();
	}

	protected Collection<Integer> getIdsCollectionFromSpriteCollection(Collection<GameElement> gameElements) {
		return gameElements.stream().mapToInt(this::getIdFromSprite).boxed().collect(Collectors.toSet());
	}

	protected abstract void assertValidLevel(int level) throws IllegalArgumentException;

	private Collection<GameElement> loadGameStateElementsForLevel(String savedGameName, int level, boolean originalGame)
			throws FileNotFoundException {
		assertValidLevel(level);
		List<GameElement> loadedGameElements = ioController.loadGameStateElements(savedGameName, level, originalGame);
		for (GameElement loadedGameElement : loadedGameElements) {
			spriteIdMap.put(spriteIdCounter.getAndIncrement(), loadedGameElement);
			loadedGameElement.setX(loadedGameElement.getX());
			loadedGameElement.setY(loadedGameElement.getY());
		}
		addOrSetLevelData(levelSpritesCache, loadedGameElements, level);
		return loadedGameElements;
	}

	protected Map<Integer, GameElement> getFilteredSpriteIdMap(Collection<GameElement> spritesToFilter) {
		return getSpriteIdMap().entrySet().stream()
				.filter(spriteEntry -> spritesToFilter.contains(spriteEntry.getValue()))
				.collect(Collectors.toMap(spriteEntry -> spriteEntry.getKey(), spriteEntry -> spriteEntry.getValue()));
	}

	protected LevelInitialized packageInitialState() {
		return LevelInitialized.newBuilder()
				.setSpritesAndStatus(serverMessageUtils.packageUpdates(getSpriteIdMap(), new HashMap<>(),
						new HashMap<>(), false, false, false, false, getResourceEndowments()))
				.setInventory(packageInventory()).build();
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
	
	private void loadGameWavesForLevel(String savedGameName, int level) throws FileNotFoundException {
		assertValidLevel(level);
		addOrSetLevelData(levelWaves, ioController.loadGameWaves(savedGameName, level), level);
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
		getLevelWaves().add(new ArrayList<>());
		initializeLevelConditions();
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
