package engine.play_engine;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import engine.IOController;
import engine.StateManager;
import sprites.Sprite;

/**
 * Single-source of truth for game state when in-play
 * 
 * @author radithya
 *
 */
public class PlayStateManager extends StateManager {

	private ElementManager elementManager;

	public PlayStateManager(IOController playIOController) {//, ElementFactory elementFactory) {
		super(playIOController);//, elementFactory);
		this.elementManager = new ElementManager();
	}

	@Override
	public void saveGameState(String savedGameName) {
		getIOController().saveGameState(savedGameName, getDescription(), getCurrentLevel(), getStatus(),
				elementManager.getCurrentElements(), PlayConstants.IS_AUTHORING);
	}

	@Override
	public Sprite placeElement(String elementName, double x, double y) {
		return elementManager.placeElement(elementName, x, y);
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
		// TODO
	}

	boolean isInPlay() {
		// TODO
		return false; // TEMP
	}

	boolean isWon() {
		// TODO
		return false; // TEMP
	}

	boolean isLost() {
		// TODO
		return false; // TEMP
	}

	boolean isLevelCleared() {
		// TODO
		return false; // TEMP
	}

	void pause() {
		// TODO
	}

	void resume() {
		// TODO
	}

}
