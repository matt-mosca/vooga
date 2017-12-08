package engine.play_engine;

import engine.AbstractGameController;
import engine.PlayModelController;
import engine.game_elements.GameElement;
import javafx.geometry.Point2D;
import networking.protocol.PlayerServer.Inventory;
import networking.protocol.PlayerServer.NewSprite;
import networking.protocol.PlayerServer.Resource;
import networking.protocol.PlayerServer.ResourceUpdate;
import networking.protocol.PlayerServer.SpriteDeletion;
import networking.protocol.PlayerServer.SpriteUpdate;
import networking.protocol.PlayerServer.StatusUpdate;
import networking.protocol.PlayerServer.TemplateProperties;
import networking.protocol.PlayerServer.TemplateProperty;
import networking.protocol.PlayerServer.Update;
import util.GameConditionsReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Controls the model for a game being played. Allows the view to modify and
 * retrieve information about the model.
 *
 * @author radithya
 * @author Ben Schwennesen
 */
public class PlayController extends AbstractGameController implements PlayModelController {

	// The conditions don't take any arguments, at least for now
	private final Class[] CONDITION_METHODS_PARAMETER_CLASSES = new Class[] {};

	private ElementManager elementManager;
	private GameConditionsReader conditionsReader;
	private boolean inPlay;
	private boolean isWon;
	private boolean isLost;
	private boolean levelCleared;
	private Method victoryConditionMethod;
	private Method defeatConditionMethod;
	private int maxLevels = DEFAULT_MAX_LEVELS;
	private List<Set<Entry<Integer, GameElement>>> savedList;

	public PlayController() {
		super();
		savedList = new ArrayList<Set<Entry<Integer, GameElement>>>();
		elementManager = new ElementManager(getGameElementFactory(), getSpriteQueryHandler());
		conditionsReader = new GameConditionsReader();
		inPlay = true;
	}

	@Override
	public void loadOriginalGameState(String saveName, int level) throws IOException {
		super.loadOriginalGameState(saveName, level);
		updateForLevelChange(saveName, level);
	}

	@Override
	public void loadSavedPlayState(String savePlayStateName) throws FileNotFoundException {
		// Get number of levels in play state
		int lastLevelPlayed = getNumLevelsForGame(savePlayStateName, false);
		// Load levels up to that level, as played (not original)
		for (int level = 1; level <= lastLevelPlayed; level++) {
			setLevel(level);
			loadLevelData(savePlayStateName, level, false);
		}
		updateForLevelChange(savePlayStateName, lastLevelPlayed);
	}

	private void updateForLevelChange(String saveName, int level) {
		setLevel(level);
		setMaxLevelsForGame(getNumLevelsForGame(saveName, true));
		elementManager.setCurrentElements(getLevelSprites().get(level));
		setVictoryCondition(getLevelConditions().get(level).get(VICTORY));
		setDefeatCondition(getLevelConditions().get(level).get(DEFEAT));
	}

	// TODO - Deprecate in favor of public Update update() variant
	@Override
	public void update() {
		if (inPlay) {
			/*
			 * if (checkLevelClearanceCondition()) { if (checkVictoryCondition()) {
			 * registerVictory(); } else { registerLevelCleared(); } } else if
			 * (checkDefeatCondition()) { registerDefeat(); } else { // Move elements, check
			 * and handle collisions elementManager.update(); }
			 */
			savedList.add(getSpriteIdMap().entrySet());
			elementManager.update();
			List<GameElement> deadElements = elementManager.getDeadElements();
			getSpriteIdMap().entrySet().removeIf(entry -> deadElements.contains(entry.getValue()));
			for (GameElement s : elementManager.getNewlyGeneratedElements()) {
				cacheAndCreateIdentifier(s);
			}
			// Package these changes into an Update message
			// Update packagedUpdate = packageUpdates(newlyGeneratedElements,
			// updatedElements, deadElements, statusUpdates, resourceUpdates);
			getSpriteIdMap().entrySet().removeIf(entry -> deadElements.contains(entry.getValue()));
			elementManager.clearDeadElements();
			elementManager.clearNewElements();
			// return packagedUpdate;
		}
	}

	@Override
	public void pause() {
		inPlay = false;
	}

	@Override
	public void resume() {
		inPlay = true;
	}

	@Override
	public boolean isLost() {
		return isLost;
	}

	@Override
	public boolean isWon() {
		return isWon;
	}

	@Override
	public Collection<Integer> getLevelSprites(int level) throws IllegalArgumentException {
		assertValidLevel(level);
		Collection<GameElement> levelGameElements = elementManager.getCurrentElements();
		return getIdsCollectionFromSpriteCollection(levelGameElements);
	}

	@Override
	public int placeElement(String elementTemplateName, Point2D startCoordinates) {
		if (getLevelBanks().get(getCurrentLevel()).purchase(elementTemplateName, 1)) {
			// TODO - keep track of the resources that were changed in this cycle, and only
			// send them to client?
			return super.placeElement(elementTemplateName, startCoordinates);
		}
		// TODO - Custom Exception ?
		throw new IllegalArgumentException();
	}

	@Override
	public void upgradeElement(int elementId) throws IllegalArgumentException {
		if (!getSpriteIdMap().containsKey(elementId)) {
			throw new IllegalArgumentException();
		}
		GameElement gameElement = getSpriteIdMap().get(elementId);
		gameElement = getGameElementUpgrader().upgradeSprite(gameElement);
		getSpriteIdMap().put(elementId, gameElement);
		// I think this will update the reference in the element manager but might need
		// to manually
	}

	public boolean isLevelCleared() {
		return levelCleared;
	}

	// TODO - move to some utils class?
	public Update packageUpdates(Collection<GameElement> newSprites, Collection<GameElement> updatedSprites,
			Collection<GameElement> deadSprites) {
		Update.Builder updateBuilder = Update.newBuilder();
		// Sprite Creations
		newSprites.forEach(newSprite -> updateBuilder.addNewSprites(packageNewSprite(newSprite)));
		// Sprite Updates
		updatedSprites.forEach(updatedSprite -> updateBuilder
				.addSpriteUpdates(SpriteUpdate.newBuilder().setSpriteId(getIdFromSprite(updatedSprite))
						.setNewX(updatedSprite.getX()).setNewY(updatedSprite.getY()).build()));
		// Sprite Deletions
		deadSprites.forEach(deadSprite -> updateBuilder
				.addSpriteDeletions(SpriteDeletion.newBuilder().setSpriteId(getIdFromSprite(deadSprite)).build()));
		// Status Updates
		updateBuilder.setStatusUpdates(getStatusUpdate());
		// Resources - Just send all resources in update for now
		ResourceUpdate.Builder resourceUpdateBuilder = ResourceUpdate.newBuilder();
		Map<String, Double> resourceEndowments = getResourceEndowments();
		resourceEndowments.keySet().forEach(resourceName -> resourceUpdateBuilder.addResources(
				Resource.newBuilder().setName(resourceName).setAmount(resourceEndowments.get(resourceName)).build()));
		return updateBuilder.setResourceUpdates(resourceUpdateBuilder.build()).build();
	}

	public Update packageInitialState() {
		return packageUpdates(getLevelSprites().get(getCurrentLevel()), Collections.emptyList(),
				Collections.emptyList());
	}

	public Update packageStatusUpdate() {
		return Update.newBuilder().setStatusUpdates(getStatusUpdate()).build();
	}

	public Inventory packageInventory() {
		Inventory.Builder inventoryBuilder = Inventory.newBuilder();
		getInventory().forEach(template -> inventoryBuilder.addTemplates(template));
		return inventoryBuilder.build();
	}

	public TemplateProperties packageTemplateProperties(String templateName) {
		TemplateProperties.Builder templatePropertiesBuilder = TemplateProperties.newBuilder();
		Map<String, String> templatePropertiesMap = getTemplateProperties(templateName);
		templatePropertiesMap.keySet()
				.forEach(templateProperty -> templatePropertiesBuilder.addProperty(TemplateProperty.newBuilder()
						.setName(templateProperty).setValue(templatePropertiesMap.get(templateProperty)).build()));
		return templatePropertiesBuilder.build();
	}

	public NewSprite placeAndPackageElement(String templateName, double x, double y) {
		GameElement placedElement = getSpriteIdMap().get(placeElement(templateName, new Point2D(x, y)));
		return packageNewSprite(placedElement);
	}

	@Override
	protected void assertValidLevel(int level) throws IllegalArgumentException {
		// Enforce increments by at-most one for player
		if (level > getCurrentLevel() + 1) {
			throw new IllegalArgumentException();
		}
	}

	private boolean checkVictoryCondition() {
		return levelCleared && getCurrentLevel() == maxLevels;
	}

	private boolean checkDefeatCondition() {
		return dispatchBooleanMethod(defeatConditionMethod);
	}

	private boolean checkLevelClearanceCondition() {
		return dispatchBooleanMethod(victoryConditionMethod);
	}

	private boolean dispatchBooleanMethod(Method chosenBooleanMethod) {
		try {
			return (boolean) chosenBooleanMethod.invoke(this, new Object[] {});
		} catch (ReflectiveOperationException e) {
			return false;
		}
	}

	private void registerVictory() {
		isWon = true;
		inPlay = false;
	}

	private void registerDefeat() {
		isLost = true;
		inPlay = false;
	}

	private void registerLevelCleared() {
		levelCleared = true;
		inPlay = false;
	}

	private void setMaxLevelsForGame(int maxLevels) {
		this.maxLevels = maxLevels;
	}

	private void setVictoryCondition(String conditionFunctionIdentifier) {
		victoryConditionMethod = getMethodForVictoryCondition(conditionFunctionIdentifier);
	}

	private void setDefeatCondition(String conditionFunctionIdentifier) {
		defeatConditionMethod = getMethodForDefeatCondition(conditionFunctionIdentifier);
	}

	private Method getMethodForVictoryCondition(String conditionFunctionIdentifier) throws IllegalArgumentException {
		String methodName = conditionsReader.getMethodNameForVictoryCondition(conditionFunctionIdentifier);
		return getMethodFromMethodName(methodName);
	}

	private Method getMethodForDefeatCondition(String conditionFunctionIdentifier) throws IllegalArgumentException {
		String methodName = conditionsReader.getMethodNameForDefeatCondition(conditionFunctionIdentifier);
		return getMethodFromMethodName(methodName);
	}

	private Method getMethodFromMethodName(String methodName) throws IllegalArgumentException {
		try {
			return this.getClass().getDeclaredMethod(methodName, CONDITION_METHODS_PARAMETER_CLASSES);
		} catch (NoSuchMethodException e) {
			// TODO - custom exception?
			throw new IllegalArgumentException();
		}
	}

	// TODO - Move conditions to separate file?

	// TODO (extension) - for multiplayer, take a playerId parameter in this method
	// and call for every playing playerId in game loop
	private boolean allEnemiesDead() {
		return elementManager.allEnemiesDead();
	}

	// TODO - Boolean defeat conditions
	private boolean allAlliesDead() {
		return elementManager.allAlliesDead();
	}

	private boolean enemyReachedTarget() {
		return elementManager.enemyReachedTarget();
	}

	private StatusUpdate getStatusUpdate() {
		// Just always send status update for now
		return StatusUpdate.newBuilder().setLevelCleared(levelCleared).setIsWon(isWon).setIsLost(isLost)
				.setInPlay(inPlay).build();
	}

	private NewSprite packageNewSprite(GameElement newSprite) {
		return NewSprite.newBuilder().setSpriteId(getIdFromSprite(newSprite)).setImageURL(newSprite.getImageUrl())
				.setImageHeight(newSprite.getGraphicalRepresentation().getFitHeight())
				.setImageWidth(newSprite.getGraphicalRepresentation().getFitWidth()).setSpawnX(newSprite.getX())
				.setSpawnY(newSprite.getY()).build();
	}

	/*
	 * For testing of reflection and streams public static void main(String[] args)
	 * { PlayController tester = new PlayController();
	 * tester.setVictoryCondition("kill all enemies");
	 * tester.setDefeatCondition("lose all allies"); boolean goodResult =
	 * tester.checkLevelClearanceCondition(); boolean badResult =
	 * tester.checkDefeatCondition(); System.out.println("Level cleared? " +
	 * Boolean.toString(goodResult)); System.out.println("Defeated? " +
	 * Boolean.toString(badResult)); for (String s :
	 * tester.conditionsReader.getPossibleVictoryConditions()) {
	 * System.out.println("Victory Condition : " + s); } for (String s :
	 * tester.conditionsReader.getPossibleDefeatConditions()) {
	 * System.out.println("Defeat Condition: " + s); } }
	 */

}
