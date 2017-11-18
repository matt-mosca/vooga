package engine.play_engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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

	// TODO - Uncomment when ElementFactory is ready
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

	// Why do we need this?
	void setCurrentElements(Collection<Sprite> newElements) {
		gameElements = new ArrayList<>(newElements);
	}

	/*

	MovementStrategy object should be created with the coordinates

	Method might still be necessary but should just do void and put in authoring game grid

	Sprite placeElement(String elementName, double x, double y) {
		// Use SpriteFactory to construct Sprite from elementName with these
		// coordinates
		// Add created Sprite to gameElements
		return null; // TEMP
	}*/

	void update() {
		Iterator<Sprite> activeSprites = gameElements.iterator();
		while(activeSprites.hasNext()) {
			Sprite element = activeSprites.next();
			element.move();
			element.attack();
			Iterator<Sprite> otherActiveSprites = gameElements.iterator();
			while(element.isAlive() && otherActiveSprites.hasNext()) {
				Sprite otherElement = otherActiveSprites.next();
				if (!otherElement.equals(element) && collidesWith(element, otherElement)) {
					element.processCollision(otherElement);
					otherElement.processCollision(element);
					if (!otherElement.isAlive()) {
						otherActiveSprites.remove();
					}
				}
			}
			if (!element.isAlive()) {
				activeSprites.remove();
			}
		}
	}

	// TEMP - SIMPLIFIED CHECKING OF COLLISIONS, JUST BY GRID POSITION
	private boolean collidesWith(Sprite element, Sprite otherElement) {
		// TODO
		return false; // TEMP
	}

}
