package engine.play_engine;

import engine.AbstractGameController;
import engine.IPlayControl;
import sprites.Sprite;

import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Controls the model for a game being played. Allows the view to modify and retrieve information about the model.
 *
 * @author radithya
 * @author Ben Schwennesen
 */
public class PlayController extends AbstractGameController implements IPlayControl {

	public static final int DEFAULT_MAX_LEVELS = 1;
	public static final String VICTORY = "victory";
	public static final String DEFEAT = "defeat";
	
	private ElementManager elementManager;
	private boolean inPlay;
	private boolean isWon;
	private boolean isLost;
	private boolean levelCleared;
	private Method victoryConditionMethod;
	private Method defeatConditionMethod;
	private int maxLevels = DEFAULT_MAX_LEVELS;
    
    public PlayController(){
        super();
        elementManager = new ElementManager();
    }

    @Override
    public void loadGameState(String saveName, int level) throws FileNotFoundException {
    		assertValidLevel(level);
        setMaxLevelsForGame(getNumLevelsForGame());
    		elementManager.setCurrentElements(loadGameStateElements(saveName, level));
        loadGameStateSettings(saveName, level);
        loadGameConditions(saveName, level);
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
			}
			else if (checkDefeatCondition()) {
				registerDefeat();
			}
			else {
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
    public Sprite placeElement(String elementName, double x, double y){
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
		levelCleared = true;
		inPlay = false;
	}
	
	private void setMaxLevelsForGame(int maxLevels) {
		this.maxLevels = maxLevels;
	}

	// TODO - Use reflection to set victoryConditionMethod based on argument
	private void setVictoryCondition(String conditionFunctionIdentifier) {
		
	}

	// TODO - Use reflection to set defeatConditionMethod based on argument
	private void setDefeatCondition(String conditionFunctionIdentifier) {
		
	}


}
