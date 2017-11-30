package engine.authoring_engine;

import authoring.path.PathList;
import authoring.path.PathPoint;
import engine.AbstractGameController;
import engine.AuthoringModelController;
import javafx.geometry.Point2D;
import packaging.Packager;
import sprites.Sprite;
import util.GameConditionsReader;
import util.SpriteTemplateExporter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Controls the model for a game being authored. Allows the view to modify and
 * retrieve information about the model.
 * 
 * @author radithya
 * @author Ben Schwennesen
 */
public class AuthoringController extends AbstractGameController implements AuthoringModelController {

	private Packager packager;
	private SpriteTemplateExporter spriteExporter;
	//Making a hard-coded map just so we can test in the front end with author and player
	//We'll fix it soon 
	
	private final String WAVE = "wave_";

	private Map<String, Set<Integer>> templateToIdMap;
	private AtomicInteger gameWaveCounter;

	public AuthoringController() {
		super();
		packager = new Packager();
		templateToIdMap = new HashMap<>();
		spriteExporter = new SpriteTemplateExporter();
		gameWaveCounter = new AtomicInteger(0);
	}
	
	@Override
	public void exportGame() {
		spriteExporter.exportSpriteTemplates(getGameName(), getSpriteFactory().getAllDefinedTemplateProperties());
		packager.generateJar(getGameName());
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
		getSpriteFactory().defineElement(elementName, properties);
	}

	@Override
	public void updateElementDefinition(String elementName, Map<String, String> properties, boolean retroactive)
			throws IllegalArgumentException {
		getSpriteFactory().updateElementDefinition(elementName, properties);
		if (retroactive) {
			updateElementsRetroactively(elementName, properties);
		}
	}

	@Override
	public void deleteElementDefinition(String elementName) throws IllegalArgumentException {
		getSpriteFactory().deleteElementDefinition(elementName);
	}

	@Override
	public int placePathFollowingElement(String elementName, PathList pathList) {
		Point2D startPoint = pathList.next();
		return placeElement(elementName, startPoint, Arrays.asList(pathList));
	}

	@Override
	public void moveElement(int elementId, double xCoordinate, double yCoordinate) throws IllegalArgumentException {
		Sprite sprite = getElement(elementId);
		sprite.setX(xCoordinate);
		sprite.setY(yCoordinate);
	}

	@Override
	public void updateElementProperties(int elementId, Map<String, String> propertiesToUpdate)
			throws IllegalArgumentException {
		updateElementPropertiesById(elementId, propertiesToUpdate);
	}

	@Override
	public void deleteElement(int elementId) throws IllegalArgumentException {
		Sprite removedSprite = getSpriteIdMap().remove(elementId);
		getLevelSprites().get(getCurrentLevel()).remove(removedSprite);
	}
	
	@Override
	public void addElementToInventory(String elementName) {
		getLevelInventories().get(getCurrentLevel()).add(elementName);
	}

	@Override
	public Map<String, String> getElementProperties(int elementId) throws IllegalArgumentException {
		Sprite sprite = getElement(elementId);
		// TODO - implement
		return null;
	}

	@Override
	public Map<String, String> getTemplateProperties(String elementName) throws IllegalArgumentException {
		return getSpriteFactory().getTemplateProperties(elementName);
	}
	
	private Sprite getElement(int elementId) throws IllegalArgumentException {
		if (!getSpriteIdMap().containsKey(elementId)) {
			throw new IllegalArgumentException();
		}
		return getSpriteIdMap().get(elementId);
	}

	@Override
	public void createNewLevel(int level) {
		setLevel(level);
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
		return getSpriteFactory().getElementBaseConfigurationOptions();
	}

	@Override
	public void setWaveProperties(Map<String, String> waveProperties, Collection<String> elementNamesToSpawn,
			Point2D spawningPoint) {
		String waveName = getNameForWave();
		defineElement(waveName, waveProperties);
		placeElement(waveName, spawningPoint, elementNamesToSpawn);
	}

	@Override
	public Map<String, Class> getAuxiliaryElementConfigurationOptions(Map<String, String> baseConfigurationChoices) {
		return getSpriteFactory().getAuxiliaryElementProperties(baseConfigurationChoices);
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
	public int cacheAndCreateIdentifier(String elementTemplateName, Sprite sprite) {
		int spriteId = super.cacheAndCreateIdentifier(elementTemplateName, sprite);
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

	private String getNameForWave() {
		return WAVE + Integer.toString(gameWaveCounter.incrementAndGet());
	}
			
}
