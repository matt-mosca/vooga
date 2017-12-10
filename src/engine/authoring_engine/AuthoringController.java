package engine.authoring_engine;

import util.path.PathList;
import engine.AbstractGameController;
import engine.AuthoringModelController;
import engine.game_elements.GameElement;
import javafx.geometry.Point2D;
import exporting.Packager;

import java.util.ArrayList;
import java.util.Arrays;
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
	// Making a hard-coded map just so we can test in the front end with author and
	// player
	// We'll fix it soon

	private final String WAVE = "wave_";

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
		// packager.generateJar(getGameName());
		// need to supply more args ^ once testing is done
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
	public void defineElement(String elementName, Map<String, String> properties) {
		getGameElementFactory().defineElement(elementName, properties);
	}

	@Override
	public void defineElementUpgrade(String elementName, int upgradeLevel, Map<String, String> upgradeProperties)
			throws IllegalArgumentException {
		if (!getGameElementFactory().getAllDefinedTemplateProperties().containsKey(elementName)) {
			throw new IllegalArgumentException();
		}
		getGameElementUpgrader().defineUpgrade(elementName, upgradeLevel, upgradeProperties);
	}

	@Override
	public void updateElementDefinition(String elementName, Map<String, String> properties, boolean retroactive)
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
	public int placePathFollowingElement(String elementName, PathList pathList) {
		Point2D startPoint = pathList.next();
		return placeElement(elementName, startPoint, Arrays.asList(pathList));
	}

	@Override
	public void moveElement(int elementId, double xCoordinate, double yCoordinate) throws IllegalArgumentException {
		GameElement gameElement = getElement(elementId);
		gameElement.setX(xCoordinate);
		gameElement.setY(yCoordinate);
	}

	@Override
	public void updateElementProperties(int elementId, Map<String, String> propertiesToUpdate)
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

	@Override
	public Map<String, String> getElementProperties(int elementId) throws IllegalArgumentException {
		GameElement gameElement = getElement(elementId);
		// TODO - implement (or, more likely, eliminate)
		return null;
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

	// TODO - to support multiple clients / interactive editing, need a client-id
	// param (string or int)
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
	public int setWaveProperties(Map<String, ?> waveProperties, Collection<String> elementNamesToSpawn,
			Point2D spawningPoint) {
		String waveName = getNameForWave();
		// Set wave as enemy, overriding (or filling if missing) playerId
		// TODO - remove / refactor for multi-player extension
		Map<String, String> stringifiedWaveProperties = getStringifiedWaveProperties(waveProperties);
		defineElement(waveName, stringifiedWaveProperties);
		int spriteId = placeElement(waveName, spawningPoint, elementNamesToSpawn);
		// save this to level waves
		getLevelWaves().get(getCurrentLevel()).add(getSpriteIdMap().get(spriteId));
		return gameWaveCounter.get();
	}

	public void editWaveProperties(int waveId, Map<String, ?> updatedProperties,
			Collection<String> newElementNamesToSpawn, Point2D newSpawningPoint) {
		Map<String, String> stringifiedWaveProperties = getStringifiedWaveProperties(updatedProperties);
		String waveName = getNameForWaveNumber(waveId);
		// Overwrite the template
		defineElement(waveName, stringifiedWaveProperties);
		deleteOutdatedWave(waveId);
		// Place the new wave
		int newSpriteId = placeElement(waveName, newSpawningPoint, newElementNamesToSpawn);
		GameElement newWave = getSpriteIdMap().get(newSpriteId);
		getLevelWaves().get(getCurrentLevel()).set(waveId, newWave);
	}
	
	public List<Map<String, String>> getWaveProperties(int level) {
		return getLevelWaves().get(getCurrentLevel()).stream().map(wave -> getElementProperties(getIdFromSprite(wave)))
				.collect(Collectors.toList());
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
	public Collection<Integer> getLevelSprites(int level) throws IllegalArgumentException {
		return getIdsCollectionFromSpriteCollection(getLevelSprites().get(getCurrentLevel()));
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

	private void updateElementsRetroactively(String elementName, Map<String, String> propertiesToUpdate) {
		Set<Integer> idsForTemplate = templateToIdMap.getOrDefault(elementName, new HashSet<>());
		for (int elementId : idsForTemplate) {
			updateElementPropertiesById(elementId, propertiesToUpdate);
		}
	}

	private void updateElementPropertiesById(int elementId, Map<String, String> propertiesToUpdate) {
		// TODO - can't use old method
	}

	private void deleteOutdatedWave(int waveId) {
		GameElement oldWave = getLevelWaves().get(getCurrentLevel()).get(waveId);
		// Remove the old placed wave
		getSpriteIdMap().remove(getIdFromSprite(oldWave));
	}
	
	private Map<String, String> getStringifiedWaveProperties(Map<String, ?> waveProperties) {
		Map<String, String> stringifiedWaveProperties = getIoController().getWaveSerialization(waveProperties);
		stringifiedWaveProperties.put(PLAYER_ID, Integer.toString(GameElement.Team.COMPUTER.ordinal()));
		return stringifiedWaveProperties;
	}
	
	private String getNameForWave() {
		return getNameForWaveNumber(gameWaveCounter.incrementAndGet());
	}
	
	private String getNameForWaveNumber(int num) {
		return WAVE + Integer.toString(num);
	}

	public static void main(String[] args) {
		AuthoringController tester = new AuthoringController();
		Map<String, Object> propMap = new HashMap<>();
		// Just test that serialization works
		propMap.put("hi", "1");
		propMap.put("attacks", new Double(2));
		propMap.put("hello", 3.0);
		tester.setWaveProperties(propMap, new ArrayList<>(), new Point2D(0, 0));
	}

}
