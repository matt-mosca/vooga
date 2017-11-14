package engine.play_engine;

import java.util.Collection;
import java.util.HashSet;

import sprites.Sprite;

/**
 * Single-source of truth for elements and their behavior when in-play
 * 
 * @author radithya
 *
 */
public class ElementManager {

	// TODO - Uncomment when ElementFactory is ready
	// private ElementFactory elementFactory;

	private Collection<Sprite> gameElements;

	/**
	 * Handles the collision-checking and Sprite-specific collision-handling logic
	 * Implements the 'Behavior' interface from the api/doc in the DESIGN_PLAN.md
	 */
	public ElementManager() {// ElementFactory elementFactory) {
		// TODO - Uncomment when elementFactory is ready
		// this.elementFactory = elementFactory;
		gameElements = new HashSet<>();
	}

	// Guaranteed to return only active elements (i.e. not dead ones)
	Collection<Sprite> getCurrentElements() {
		// Filter to return only active elements?
		gameElements.removeIf(gameElement -> !gameElement.isActive());
		return gameElements;
	}

	void setCurrentElements(Collection<Sprite> newElements) {
		gameElements = newElements;
	}

	// TODO
	Sprite placeElement(String elementName, double x, double y) {
		// Use ElementFactory to construct Sprite from elementName with these
		// coordinates
		// Add created Sprite to gameElements
		return null; // TEMP
	}

	// TODO - collision checking logic, using VisitorPattern
	boolean checkCollisions() {
		return false; // TEMP
	}

	// TODO - Call Sprite methods based on their custom behavior, either based on
	// their overridden sub-class methods or through reading data files
	void handleCollisions() {

	}

}
