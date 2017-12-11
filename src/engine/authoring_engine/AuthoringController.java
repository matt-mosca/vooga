package engine.authoring_engine;

import engine.AbstractGameController;
import engine.AuthoringModelController;
import engine.game_elements.GameElement;
import javafx.geometry.Point2D;
import networking.protocol.PlayerServer.NewSprite;
import networking.protocol.PlayerServer.SpriteUpdate;
import exporting.Packager;
import exporting.Publisher;

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
 * Controls the model for a game being authored. Allows the view to modify and
 * retrieve information about the model.
 *
 * @author radithya
 * @author Ben Schwennesen
 */
public class AuthoringController extends AbstractGameController implements AuthoringModelController {

	private Packager packager;
	private Publisher publisher;

	private final String WAVE = "wave_";
	private final String WAVE_DELIMITER = "_";

	// TODO - move elsewhere
	private final String PLAYER_ID = "playerId";

	private Map<String, Set<Integer>> templateToIdMap;
	private AtomicInteger gameWaveCounter;

	public AuthoringController() {
		super();
		packager = new Packager();
		templateToIdMap = new HashMap<>();
		gameWaveCounter = new AtomicInteger(0);
	}

	@Override
	public void exportGame() {
		getSpriteTemplateIoHandler().exportSpriteTemplates(getGameName(),
				getGameElementFactory().getAllDefinedTemplateProperties());
		try {
			if (publisher == null) {
				publisher = new Publisher();
			}
			String pathToExportedJar = packager.generateJar(getGameName());
			String urlForSharedJar = publisher.uploadExportedJar(pathToExportedJar);
			// TODO - push the link to Facebook
		} catch (IOException failedToExportOrPublishException) {
			// TODO - handle
			// this one does have a custom message set already (getMessage()) but
			// it's hard coded as a final string
		}
	}

	public void setGameDescription(String gameDescription) {
		getLevelDescriptions().set(getCurrentLevel(), gameDescription);
	}

	@Override
	public void setVictoryCondition(String conditionIdentifier) {
		getLevelConditions().get(getCurrentLevel()).put(VICTORY, conditionIdentifier);
	}

	@Override
	public void setDefeatCondition(String conditionIdentifier) {
		getLevelConditions().get(getCurrentLevel()).put(DEFEAT, conditionIdentifier);
	}

	@Override
	public void defineElement(String elementName, Map<String, Object> properties) {
		getGameElementFactory().defineElement(elementName, properties);
	}

	@Override
	public void defineElementUpgrade(String elementName, int upgradeLevel, Map<String, Object> upgradeProperties)
			throws IllegalArgumentException {
		if (!getGameElementFactory().getAllDefinedTemplateProperties().containsKey(elementName)) {
			throw new IllegalArgumentException();
		}
		getGameElementUpgrader().defineUpgrade(elementName, upgradeLevel, upgradeProperties);
	}

	@Override
	public void updateElementDefinition(String elementName, Map<String, Object> properties, boolean retroactive)
			throws IllegalArgumentException {
		getGameElementFactory().updateElementDefinition(elementName, properties);
		if (retroactive) {
			updateElementsRetroactively(elementName, properties);
		}
	}

	@Override
	public void deleteElementDefinition(String elementName) throws IllegalArgumentException {
		getGameElementFactory().deleteElementDefinition(elementName);
	}

	@Override
	public SpriteUpdate moveElement(int elementId, double xCoordinate, double yCoordinate)
			throws IllegalArgumentException {
		GameElement gameElement = getElement(elementId);
		gameElement.setX(xCoordinate);
		gameElement.setY(yCoordinate);
		return getServerMessageUtils().packageUpdatedSprite(gameElement, elementId);
	}

	@Override
	public void updateElementProperties(int elementId, Map<String, Object> propertiesToUpdate)
			throws IllegalArgumentException {
		updateElementPropertiesById(elementId, propertiesToUpdate);
	}

	@Override
	public void deleteElement(int elementId) throws IllegalArgumentException {
		GameElement removedGameElement = getSpriteIdMap().remove(elementId);
		getLevelSprites().get(getCurrentLevel()).remove(removedGameElement);
	}

	@Override
	public void addElementToInventory(String elementName) {
		getLevelInventories().get(getCurrentLevel()).add(elementName);
	}

	private GameElement getElement(int elementId) throws IllegalArgumentException {
		if (!getSpriteIdMap().containsKey(elementId)) {
			throw new IllegalArgumentException();
		}
		return getSpriteIdMap().get(elementId);
	}

	@Override
	public void setStatusProperty(String property, Double value) {
		getLevelStatuses().get(getCurrentLevel()).put(property, value);
	}

	@Override
	public void setResourceEndowments(Map<String, Double> resourceEndowments) {
		getLevelBanks().get(getCurrentLevel()).setResourceEndowments(resourceEndowments);
	}

	@Override
	public void setResourceEndowment(String resourceName, double newResourceEndowment) {
		getLevelBanks().get(getCurrentLevel()).setResourceEndowment(resourceName, newResourceEndowment);
	}

	@Override
	public void setUnitCost(String elementName, Map<String, Double> unitCosts) {
		getLevelBanks().get(getCurrentLevel()).setUnitCost(elementName, unitCosts);
	}

	@Override
	public void deleteLevel(int level) throws IllegalArgumentException {
		getLevelStatuses().remove(level);
		getLevelSprites().remove(level);
		getLevelConditions().remove(level);
		getLevelDescriptions().remove(level);
	}

	@Override
	public Map<String, List<String>> getElementBaseConfigurationOptions() {
		return getGameElementFactory().getElementBaseConfigurationOptions();
	}

	@Override
	public int createWaveProperties(Map<String, Object> waveProperties, Collection<String> elementNamesToSpawn,
			Point2D spawningPoint) throws ReflectiveOperationException {
		String waveName = getNameForWave();
		defineElement(waveName, waveProperties);
		System.out.println(waveName);
		System.out.println(waveProperties.toString());
		System.out.println(elementNamesToSpawn);
		int spriteId = placeElement(waveName, spawningPoint, elementNamesToSpawn);
		// save this to level waves
		getLevelWaves().get(getCurrentLevel()).add(getSpriteIdMap().get(spriteId));
		return gameWaveCounter.get();
	}

	@Override
	public void editWaveProperties(int waveId, Map<String, Object> updatedProperties,
			Collection<String> newElementNamesToSpawn, Point2D newSpawningPoint) throws ReflectiveOperationException {
		String waveName = getNameForWaveNumber(getCurrentLevel(), waveId);
		// Overwrite the template
		defineElement(waveName, updatedProperties);
		deleteOutdatedWave(waveId);
		// Place the new wave
		int newSpriteId = placeElement(waveName, newSpawningPoint, newElementNamesToSpawn);
		GameElement newWave = getSpriteIdMap().get(newSpriteId);
		getLevelWaves().get(getCurrentLevel()).set(waveId, newWave);
	}

	@Override
	public Map<String, Object> getWaveProperties(int waveNum) {
		return getTemplateProperties(getNameForWaveNumber(getCurrentLevel(), waveNum));
	}

	@Override
	public Map<String, Class> getAuxiliaryElementConfigurationOptions(Map<String, String> baseConfigurationChoices) {
		return getGameElementFactory().getAuxiliaryElementProperties(baseConfigurationChoices);
	}

	@Override
	public Collection<String> getPossibleVictoryConditions() {
		return getGameConditionsReader().getPossibleVictoryConditions();
	}

	@Override
	public Collection<String> getPossibleDefeatConditions() {
		return getGameConditionsReader().getPossibleDefeatConditions();
	}

	@Override
	public Map<String, Collection<Integer>> getCurrentVictoryConditions() {
		return getCurrentConditions(VICTORY);
	}

	@Override
	public Map<String, Collection<Integer>> getCurrentDefeatConditions() {
		return getCurrentConditions(DEFEAT);
	}

	@Override
	public Map<String, List<Map<String, Object>>> getAllDefinedElementUpgrades() {
		return getGameElementUpgrader().getSpriteUpgradesForEachTemplate();
	}

	@Override
	public int cacheAndCreateIdentifier(String elementTemplateName, GameElement gameElement) {
		int spriteId = super.cacheAndCreateIdentifier(elementTemplateName, gameElement);
		Set<Integer> idsForTemplate = templateToIdMap.getOrDefault(elementTemplateName, new HashSet<>());
		idsForTemplate.add(spriteId);
		templateToIdMap.put(elementTemplateName, idsForTemplate);
		return spriteId;
	}

	@Override
	protected void assertValidLevel(int level) throws IllegalArgumentException {
		if (level <= 0 || level > getLevelSprites().size()) {
			throw new IllegalArgumentException();
			// TODO - customize exception ?
		}
	}

	private void updateElementsRetroactively(String elementName, Map<String, Object> propertiesToUpdate) {
		Set<Integer> idsForTemplate = templateToIdMap.getOrDefault(elementName, new HashSet<>());
		for (int elementId : idsForTemplate) {
			updateElementPropertiesById(elementId, propertiesToUpdate);
		}
	}

	private void updateElementPropertiesById(int elementId, Map<String, Object> propertiesToUpdate) {
		// TODO - can't use old method
	}

	private void deleteOutdatedWave(int waveId) {
		GameElement oldWave = getLevelWaves().get(getCurrentLevel()).get(waveId);
		// Remove the old placed wave
		getSpriteIdMap().remove(getIdFromSprite(oldWave));
	}

	private String getNameForWave() {
		return getNameForWaveNumber(getCurrentLevel(), gameWaveCounter.incrementAndGet());
	}

	private String getNameForWaveNumber(int level, int num) {
		return WAVE + WAVE_DELIMITER + Integer.toString(level) + WAVE_DELIMITER + Integer.toString(num);
	}

	private Map<String, Collection<Integer>> getCurrentConditions(String conditionType) {
		Map<String, Collection<Integer>> conditionsToLevels = new HashMap<>();
		List<Map<String, String>> levelConditions = getLevelConditions();
		List<String> levelSettingsForConditionType = levelConditions.stream()
				.map(conditionMap -> conditionMap.get(conditionType)).collect(Collectors.toList());
		for (int level = 1; level <= getLevelsForCurrentGame(); level++) {
			String condition = levelSettingsForConditionType.get(level);
			Collection<Integer> levelsWithCondition = conditionsToLevels.getOrDefault(condition, new ArrayList<>());
			levelsWithCondition.add(level);
			conditionsToLevels.put(condition, levelsWithCondition);
		}
		return conditionsToLevels;
	}
	
	private int getLevelsForCurrentGame() {
		return getNumLevelsForGame(getGameName(), true);
	}

	public static void main(String[] args) {
		AuthoringController tester = new AuthoringController();
		Map<String, Object> propMap = new HashMap<>();
		// Just test that serialization works
		propMap.put("hi", "1");
		propMap.put("attacks", new Double(2));
		propMap.put("hello", 3.0);
		try {
			tester.createWaveProperties(propMap, new ArrayList<>(), new Point2D(0, 0));
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}
}
