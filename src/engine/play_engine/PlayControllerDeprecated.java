package engine.play_engine;

import java.util.Map;

import engine.GameController;
import sprites.SpriteFactory;


/**
 * Top-level play controller, gateway of front end GamePlayer to back
 * end logic and IO
 * 
 * @deprecated
 * @author radithya
 *
 */
public class PlayControllerDeprecated extends GameController {

	// TODO - Initialize an ElementFactory instance when its ready
	private PlayStateManager stateManager;
	private SpriteFactory spriteFactory;
	
	public PlayControllerDeprecated() {
		super();
		stateManager = new PlayStateManager(getIOController(), spriteFactory);
	}
	
	@Override
	public PlayStateManager getStateManager() {
		return stateManager;
	}
	
	@Override
	public boolean isAuthoring() {
		return PlayConstants.IS_AUTHORING;
	}
	
	/**
	 * Run the game loop for the given number of cycles
	 * 
	 * @param cycles
	 *            the number of cycles
	 */
	public void update() {
		getStateManager().update();
	}

	/**
	 * Get lives left
	 * 
	 * @return number of lives left
	 */
	public int getLives() {
		return getStateManager().getLives(); // TEMP
	}

	/**
	 * Retrieve the amount of each resource left
	 * 
	 * @return map of resource name to amount left
	 */
	public Map<String, Integer> getResources() {
		return getStateManager().getResources();
	}

	/**
	 * Query the current level of the game
	 * 
	 * @return the integer corresponding to the game's current level
	 */
	public int getCurrentLevel() {
		return getStateManager().getCurrentLevel();
	}
	
	/**
	 * Query whether the game is currently in play
	 * 
	 * @return true if in play, false if over / paused
	 */
	public boolean isInPlay() {
		return getStateManager().isInPlay();
	}

	/**
	 * Query whether game has been won
	 * 
	 * @return true if won, false otherwise
	 */
	public boolean isWon() {
		return getStateManager().isWon();
	}

	/**
	 * Query whether game has been lost
	 * 
	 * @return true if lost, false otherwise
	 */
	public boolean isLost() {
		return getStateManager().isLost();
	}

	/**
	 * Query whether the current level has been cleared (if so, game will be paused
	 * until resume() is called )
	 * 
	 * @return true if current level is cleared and game is paused, false otherwise
	 */
	public boolean isLevelCleared() {
		return getStateManager().isLevelCleared();
	}

	/**
	 * Pause the game
	 */
	public void pause() {
		getStateManager().pause();
	}

	/**
	 * Resume the game
	 */
	public void resume() {
		getStateManager().resume();
	}


}
