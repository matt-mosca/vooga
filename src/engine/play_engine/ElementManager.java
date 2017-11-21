package engine.play_engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import sprites.Sprite;
import sprites.SpriteFactory;

/**
 * Single-source of truth for elements and their behavior when in-play
 * 
 * @author radithya
 *
 */
public class ElementManager {

	private SpriteFactory spriteFactory;

	// Use list to enforce an ordering of elements to facilitate consideration of
	// every element pair only once
	private List<Sprite> gameElements;

	// TODO
	// Reference to GridManager
	// call double[] getGridPosition(double x, double y) to get grid position, do
	// collision-checking at coarser granularity

	/**
	 * Handles the collision-checking and Sprite-specific collision-handling logic
	 * Implements the 'Behavior' interface from the api/doc in the DESIGN_PLAN.md
	 */
	public ElementManager() {
		spriteFactory = new SpriteFactory();
		gameElements = new ArrayList<>();
	}

	// Guaranteed to return only active elements (i.e. not dead ones)
	Collection<Sprite> getCurrentElements() {
		// Filter to return only active elements?
		gameElements.removeIf(gameElement -> !gameElement.isAlive());
		return gameElements;
	}

	void setCurrentElements(Collection<Sprite> newElements) {
		gameElements = new ArrayList<>(newElements);
	}

	/*
	 * MovementStrategy object should be created with the coordinates Method might
	 * still be necessary but should just do void and put in authoring game grid
	 */

	Sprite placeElement(String elementName, double x, double y) {
		// Use SpriteFactory to construct Sprite from elementName with these
		// coordinates
		Sprite generatedSprite = spriteFactory.generateSprite(elementName);
		generatedSprite.setX(x);
		generatedSprite.setY(y);
		// Add created Sprite to gameElements
		return generatedSprite;
	}

	void update() {
		for (int elementIndex = 0; elementIndex < gameElements.size(); elementIndex++) {
			Sprite element = gameElements.get(elementIndex);
			element.move();
			element.attack();
			processAllCollisionsForElement(elementIndex, element);
		}
		gameElements.removeIf(element -> !element.isAlive());
	}

	private void processAllCollisionsForElement(int elementIndex, Sprite element) {
		for (int otherIndex = elementIndex + 1; otherIndex < gameElements.size(); otherIndex++) {
			Sprite otherElement = gameElements.get(otherIndex);
			if (collidesWith(element, otherElement)) {
				element.processCollision(otherElement);
				otherElement.processCollision(element);
			}
		}
	}

	private boolean collidesWith(Sprite element, Sprite otherElement) {
		return element.getBounds().intersects(otherElement.getBounds());
	}

	/*
	 * Santo's GridManager is not ready, so might as well skip this for now and use
	 * the exact collision-checking logic above // TEMP - SIMPLIFIED CHECKING OF COLLISIONS,
	 * JUST BY GRID POSITION private boolean collidesWith(Sprite element, Sprite
	 * otherElement) { // TODO return false; // TEMP }
	 */

}
