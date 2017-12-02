package engine.play_engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import engine.SpriteQueryHandler;
import javafx.geometry.Point2D;
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
	private List<Sprite> activeElements;
	private List<Sprite> newElements;
	private List<Sprite> deadElements;

	private SpriteQueryHandler spriteQueryHandler;

	// TODO
	// Reference to GridManager

	/**
	 * Handles the collision-checking and Sprite-specific collision-handling logic
	 * Implements the 'Behavior' interface from the api/doc in the DESIGN_PLAN.md
	 */
	public ElementManager(SpriteFactory spriteFactory, SpriteQueryHandler spriteQueryHandler) {
		this.spriteFactory = spriteFactory;
		this.spriteQueryHandler = spriteQueryHandler;
		newElements = new ArrayList<>();
		deadElements = new ArrayList<>();
	}

	// Guaranteed to return only active elements (i.e. not dead ones)
	Collection<Sprite> getCurrentElements() {
		// Filter to return only active elements?
		activeElements.removeIf(gameElement -> !gameElement.isAlive());
		return activeElements;
	}

	void setCurrentElements(List<Sprite> newElements) {
		activeElements = newElements;
	}

	void update() {
		for (int elementIndex = 0; elementIndex < activeElements.size(); elementIndex++) {
			Sprite element = activeElements.get(elementIndex);
			element.move();
			handleElementFiring(element);
			processAllCollisionsForElement(elementIndex, element);
		}
		activeElements.forEach(element -> {
			if (!element.isAlive() || (element.reachedTarget() && element.shouldRemoveUponCompletion())) {
				deadElements.add(element);
			}
		});
		activeElements.removeAll(deadElements);
		activeElements.addAll(newElements);
	}

	List<Sprite> getDeadElements() {
		return deadElements;
	}

	void clearDeadElements() {
		deadElements.clear();
	}

	List<Sprite> getNewlyGeneratedElements() {
		return newElements;
	}
	
	void clearNewElements() {
		newElements.clear();
	}

	boolean allEnemiesDead() {
		return allElementsFulfillCondition(element -> !element.isEnemy() || !element.isAlive());
	}

	boolean allAlliesDead() {
		return allElementsFulfillCondition(element -> !element.isAlly() || !element.isAlive());
	}

	boolean enemyReachedTarget() {
		return allElementsFulfillCondition(element -> !element.isEnemy() || !element.reachedTarget());
	}

	boolean allElementsFulfillCondition(Predicate<Sprite> condition) {
		for (Sprite element : activeElements) {
			if (!condition.test(element)) {
				return false;
			}
		}
		return true;
	}

	private void processAllCollisionsForElement(int elementIndex, Sprite element) {
		for (int otherIndex = elementIndex + 1; otherIndex < activeElements.size(); otherIndex++) {
			Sprite otherElement = activeElements.get(otherIndex);
			if (element.collidesWith(otherElement)) {
				element.processCollision(otherElement);
				otherElement.processCollision(element);
			}
		}
	}
	
	private void handleElementFiring(Sprite element) {
		if (element.shouldFire()) {
			String elementTemplateName = element.fire();
			List<Sprite> exclusionOfSelf = new ArrayList<>(activeElements);
			exclusionOfSelf.remove(element);
			// Use player id of firing element rather than projectile? This allows greater
			// flexibility
			Map<String, Object> auxiliaryObjects = spriteQueryHandler.getAuxiliarySpriteConstructionObjectMap(
					element.getPlayerId(), new Point2D(element.getX(), element.getY()), exclusionOfSelf);
			Sprite projectileSprite = spriteFactory.generateSprite(elementTemplateName,
					new Point2D(element.getX(), element.getY()), auxiliaryObjects);
			newElements.add(projectileSprite);
		}

	}
	
}
