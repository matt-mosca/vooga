package engine.play_engine;

import engine.AbstractGameController;
import engine.PlayModelController;
import sprites.Sprite;
import util.GameConditionsReader;

import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

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

	public PlayController() {
		super();
		elementManager = new ElementManager();
		conditionsReader = new GameConditionsReader();
	}

	@Override
	public void loadOriginalGameState(String saveName, int level) throws FileNotFoundException {
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

	@Override
	public void update() {
		if (inPlay) {
			if (checkLevelClearanceCondition()) {
				if (checkVictoryCondition()) {
					registerVictory();
				} else {
					registerLevelCleared();
				}
			} else if (checkDefeatCondition()) {
				registerDefeat();
			} else {
				// Move elements, check and handle collisions
				elementManager.update();
			}
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

	// TODO - IDs instead of returning sprite ?
	@Override
	public Sprite placeElement(String elementName, double x, double y) {
		return elementManager.placeElement(elementName, x, y);
	}

	// TODO - IDs instead of returning sprite ?
	@Override
	public Collection<Sprite> getLevelSprites(int level) throws IllegalArgumentException {
		assertValidLevel(level);
		return getLevelSprites().get(level);
	}

	@Override
	public Map<String, String> getStatus() {
		return getLevelStatuses().get(getCurrentLevel());
	}
	
	boolean isLevelCleared() {
		return levelCleared;
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
			return (boolean) chosenBooleanMethod.invoke(this, new Object[] { });
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
		victoryConditionMethod = getMethodForCondition(conditionFunctionIdentifier);
	}

	private void setDefeatCondition(String conditionFunctionIdentifier) {
		defeatConditionMethod = getMethodForCondition(conditionFunctionIdentifier);
	}

	private Method getMethodForCondition(String conditionFunctionIdentifier) throws IllegalArgumentException {
		String methodName = conditionsReader.getMethodNameForCondition(conditionFunctionIdentifier);
		System.out.println("Method name: " + methodName);
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
		System.out.println("Checking if all enemies are dead");
		return elementManager.allEnemiesDead();
	}

	// TODO - Boolean defeat conditions
	private boolean allAlliesDead() {
		System.out.println("Checking if all allies are dead");
		return elementManager.allAlliesDead();
	}
	
	/* Testing of reflection
	public static void main(String[] args) {
		PlayController tester = new PlayController();
		tester.setVictoryCondition("kill all enemies");
		tester.setDefeatCondition("lose all allies");
		boolean goodResult = tester.checkLevelClearanceCondition();
		boolean badResult = tester.checkDefeatCondition();
		System.out.println("Level cleared? " + Boolean.toString(goodResult));
		System.out.println("Defeated? " + Boolean.toString(badResult));
	}
	*/
	
}
