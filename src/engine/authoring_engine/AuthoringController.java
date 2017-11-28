package engine.authoring_engine;

import engine.AbstractGameController;
import engine.AuthoringModelController;
import packaging.Packager;
import sprites.Sprite;
import util.GameConditionsReader;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Controls the model for a game being authored. Allows the view to modify and
 * retrieve information about the model.
 *
 * TODO (for Ben S) - move sprite map/id into sprite factory or other object (?)
 * - implement object creation in factory via string properties + this will
 * entail all behavior object constructors having same parameters because
 * reflection won't work otherwise (eg) MortalCollider needs same constructor
 * params as ImmortalCollider - custom error throwing
 * 
 * @author radithya
 * @author Ben Schwennesen
 */
public class AuthoringController extends AbstractGameController implements AuthoringModelController {

	private Packager packager;
	private GameConditionsReader gameConditionsReader;

	private Map<String, Set<Integer>> templateToIdMap;

	public AuthoringController() {
		super();
		packager = new Packager();
		gameConditionsReader = new GameConditionsReader();
	}

	@Override
	public void exportGame() {
		getSpriteFactory().exportSpriteTemplates();
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
	public Map<String, String> getElementProperties(int elementId) throws IllegalArgumentException {
		Sprite sprite = getElement(elementId);
		// TODO - implement
		return null;
	}

	@Override
	public Map<String, String> getTemplateProperties(String elementName) throws IllegalArgumentException {
		return getSpriteFactory().getTemplateProperties(elementName);
	}

	@Override
	public Map<String, Map<String, String>> getAllDefinedTemplateProperties() {
		return getSpriteFactory().getAllDefinedTemplateProperties();
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
	public void setStatusProperty(String property, String value) {
		getLevelStatuses().get(getCurrentLevel()).put(property, value);
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
	public Collection<String> getPossibleVictoryConditions() {
		return gameConditionsReader.getPossibleVictoryConditions();
	}

	@Override
	public Collection<String> getPossibleDefeatConditions() {
		return gameConditionsReader.getPossibleDefeatConditions();
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

}
