package engine.play_engine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import engine.IOController;
import engine.StateManager;
import sprites.Sprite;
import sprites.SpriteFactory;

/**
 * Single-source of truth for game state when in-play
 * 
 * @deprecated
 * @author radithya
 *
 */
public class PlayStateManager extends StateManager {

	private ElementManager elementManager;
	private boolean inPlay;
	private boolean isWon;
	private boolean isLost;
	private boolean isLevelCleared;
	private Method victoryConditionMethod;
	private Method defeatConditionMethod;
	private Method levelClearanceConditionMethod;

	public PlayStateManager(IOController playIOController, SpriteFactory spriteFactory) {
		super(playIOController, spriteFactory);
		this.elementManager = new ElementManager();
		inPlay = false;
	}

	@Override
	public void saveGameState(String savedGameName) {
		//getIOController().saveGameState(savedGameName, getDescription(), getCurrentLevel(), getStatus(),
		//		PlayConstants.IS_AUTHORING);
	}

	@Override
	public Sprite placeElement(String elementName, double x, double y) {
		return elementManager.placeElement(elementName, x, y);
		// TODO - this should return an integer id for the frontend to use to access it in the future
		// (prevents exposing the Sprite objects to the frontend)
	}

	@Override
	public void setCurrentElements(Collection<Sprite> newElements) {
		elementManager.setCurrentElements(newElements);
	}

	@Override
	public Collection<Sprite> getCurrentElements() {
		return elementManager.getCurrentElements();
	}
	
	void update() {
		if (inPlay) {
			if (checkVictoryCondition()) {
				registerVictory();
			}
			if (checkDefeatCondition()) {
				registerDefeat();
			}
			if (checkLevelClearanceCondition()) {
				registerLevelCleared();
			}
			// Move elements, check and handle collisions
			elementManager.update();
		}
	}

	boolean isInPlay() {
		return inPlay;
	}

	void pause() {
		inPlay = false;
	}

	void resume() {
		inPlay = true;
	}

	boolean isWon() {
		return isWon;
	}

	boolean isLost() {
		return isLost;
	}

	boolean isLevelCleared() {
		return isLevelCleared;
	}

	@Override
	protected void assertValidLevel(int level) throws IllegalArgumentException {
		// Enforce increments by at-most one for player
		if (level > getCurrentLevel() + 1) {
			throw new IllegalArgumentException();
		}
	}
	
	private boolean checkVictoryCondition() {
		return dispatchBooleanMethod(victoryConditionMethod);
	}
	
	private boolean checkDefeatCondition() {
		return dispatchBooleanMethod(defeatConditionMethod);
	}
	
	private boolean checkLevelClearanceCondition() {
		return dispatchBooleanMethod(levelClearanceConditionMethod);
	}
	
	private boolean dispatchBooleanMethod(Method chosenBooleanMethod) {
		try {
			return (boolean) chosenBooleanMethod.invoke(this, new Object[]{null});			
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
		isLevelCleared = true;
		inPlay = false;
	}

}
