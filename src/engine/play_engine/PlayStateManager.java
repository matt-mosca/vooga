package engine.play_engine;

import java.util.Collection;

import engine.IOController;
import engine.StateManager;
import sprites.Sprite;
import sprites.SpriteFactory;

/**
 * Single-source of truth for game state when in-play
 * 
 * @author radithya
 *
 */
public class PlayStateManager extends StateManager {

	private ElementManager elementManager;
	private boolean inPlay;

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
			// Move elements, check and handle collisions
			elementManager.update();
			// TODO - Check top-level victory / defeat / level completion conditions			
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

	// TODO - Check victory conditions from game config
	boolean isWon() {
		return false; // TEMP
	}

	// TODO - Check defeat conditions from game config
	boolean isLost() {
		return false; // TEMP
	}

	// TODO - Check level-clearance conditions from game config
	boolean isLevelCleared() {
		return false; // TEMP
	}

	@Override
	protected void assertValidLevel(int level) throws IllegalArgumentException {
		// Enforce increments by at-most one for player
		if (level > getCurrentLevel() + 1) {
			throw new IllegalArgumentException();
		}
	}

}
