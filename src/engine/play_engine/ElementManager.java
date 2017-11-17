package engine.play_engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import engine.behavior.collision.CollisionVisitable;
import engine.behavior.collision.CollisionVisitor;
import engine.behavior.movement.MovementStrategy;
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
	public ElementManager() {// ElementFactory elementFactory) {
		// TODO - Uncomment when elementFactory is ready
		// this.elementFactory = elementFactory;
		gameElements = new ArrayList<>();
	}

	// Guaranteed to return only active elements (i.e. not dead ones)
	Collection<Sprite> getCurrentElements() {
		// Filter to return only active elements?
		gameElements.removeIf(gameElement -> !gameElement.isActive());
		return gameElements;
	}

	void setCurrentElements(Collection<Sprite> newElements) {
		gameElements = new ArrayList<>(newElements);
	}

	// TODO
	Sprite placeElement(String elementName, double x, double y) {
		// Use ElementFactory to construct Sprite from elementName with these
		// coordinates
		// Add created Sprite to gameElements
		return null; // TEMP
	}

	void update() {
		for (int elementIndex = 0; elementIndex < gameElements.size(); elementIndex++) {
			Sprite element = gameElements.get(elementIndex);
			CollisionVisitor colliderBehaviorForElement = element.getCollisionVisitor();
			CollisionVisitable collidableBehaviorForElement = element.getCollisionVisitable();
			MovementStrategy movementStrategyForElement = element.getMovementStrategy();
			// Handle blocked element
			if (colliderBehaviorForElement.isBlocked()) {
				movementStrategyForElement.handleBlock();
				colliderBehaviorForElement.unBlock();
			}
			element.move();
			for (int otherElementIndex = elementIndex + 1; otherElementIndex < gameElements
					.size(); otherElementIndex++) {
				Sprite otherElement = gameElements.get(otherElementIndex);
				if (collidesWith(element, otherElement)) {
					CollisionVisitor colliderBehaviorForOtherElement = otherElement.getCollisionVisitor();
					CollisionVisitable collidableBehaviorForOtherElement = otherElement.getCollisionVisitable();
					// Handle effects of collision on element
					collidableBehaviorForOtherElement.accept(colliderBehaviorForElement);
					// Handle effects of collision on otherElement
					collidableBehaviorForElement.accept(colliderBehaviorForOtherElement);
					if (!colliderBehaviorForElement.isAlive()) {
						// Will facilitate removal of element
						element.deactivate();
					}
					if (!colliderBehaviorForOtherElement.isAlive()) {
						// Will facilitate removal of element
						otherElement.deactivate();
					}
				}
			}
		}
	}
	
	// TEMP - SIMPLIFIED CHECKING OF COLLISIONS, JUST BY GRID POSITION
	private boolean collidesWith(Sprite element, Sprite otherElement) {
		// TODO
		return false; // TEMP
	}

}
