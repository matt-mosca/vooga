package engine.play_engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import engine.SpriteQueryHandler;
import engine.game_elements.GameElement;
import javafx.geometry.Point2D;
import engine.game_elements.GameElementFactory;
import factory.AudioClipFactory;

/**
 * Single-source of truth for elements and their behavior when in-play
 * 
 * @author radithya
 *
 */
public class ElementManager {

	private GameElementFactory gameElementFactory;
	// Use list to enforce an ordering of elements to facilitate consideration of
	// every element pair only once
	private List<GameElement> activeElements;
	private List<GameElement> newElements;
	private List<GameElement> updatedElements;
	private List<GameElement> deadElements;
	
	private AudioClipFactory audioClipFactory;

	private SpriteQueryHandler spriteQueryHandler;

	// TODO
	// Reference to GridManager

	/**
	 * Handles the collision-checking and GameElement-specific collision-handling
	 * logic Implements the 'Behavior' interface from the api/doc in the
	 * DESIGN_PLAN.md
	 */
	public ElementManager(GameElementFactory gameElementFactory, SpriteQueryHandler spriteQueryHandler) {
		this.gameElementFactory = gameElementFactory;
		this.spriteQueryHandler = spriteQueryHandler;
		newElements = new ArrayList<>();
		updatedElements = new ArrayList<>();
		deadElements = new ArrayList<>();
		activeElements = new ArrayList<>();
	}

	// Guaranteed to return only active elements (i.e. not dead ones)
	Collection<GameElement> getCurrentElements() {
		// Filter to return only active elements?
		activeElements.removeIf(gameElement -> !gameElement.isAlive());
		return activeElements;
	}

	void setCurrentElements(List<GameElement> newElements) {
		activeElements = newElements;
	}

	void update() {
		for (int elementIndex = 0; elementIndex < activeElements.size(); elementIndex++) {
			GameElement element = activeElements.get(elementIndex);
			element.move();
			handleElementFiring(element);
			processAllCollisionsForElement(elementIndex, element);
		}
		activeElements.forEach(element -> {
			if (!element.isAlive()) {
				deadElements.add(element);
			} else {
				updatedElements.add(element);
			}
		});
		activeElements.removeAll(deadElements);
		activeElements.addAll(newElements);
	}

	List<GameElement> getNewlyGeneratedElements() {
		return newElements;
	}

	List<GameElement> getUpdatedElements() {
		return updatedElements;
	}
	
	List<GameElement> getDeadElements() {
		return deadElements;
	}

	void clearNewElements() {
		newElements.clear();
	}

	void clearUpdatedElements() {
		updatedElements.clear();
	}
	
	void clearDeadElements() {
		deadElements.clear();
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

	boolean allElementsFulfillCondition(Predicate<GameElement> condition) {
		for (GameElement element : activeElements) {
			if (!condition.test(element)) {
				return false;
			}
		}
		return true;
	}

	private void processAllCollisionsForElement(int elementIndex, GameElement element) {
		for (int otherIndex = elementIndex + 1; otherIndex < activeElements.size(); otherIndex++) {
			GameElement otherElement = activeElements.get(otherIndex);
			if (element.collidesWith(otherElement)) {
				element.processCollision(otherElement);
				otherElement.processCollision(element);
				playAudio(element.getCollisionAudio());
				playAudio(otherElement.getCollisionAudio());
			}
		}
	}

	private void handleElementFiring(GameElement element) {
		Point2D nearestTargetLocation;
		List<GameElement> exclusionOfSelf = new ArrayList<>(activeElements);
		exclusionOfSelf.remove(element);
		GameElement nearestEnemyElement = spriteQueryHandler.getNearestEnemy(
				element.getPlayerId(), new Point2D(element.getX(), element.getY()), exclusionOfSelf);
		if(nearestEnemyElement == null) {
			nearestTargetLocation = new Point2D(0,0);
		} else {
			nearestTargetLocation = new Point2D(nearestEnemyElement.getX(),nearestEnemyElement.getY());
		}
		//@ TODO Fix should fire to take in nearest point
		if (element.shouldFire()) {
			String elementTemplateName = element.fire();
			playAudio(element.getFiringAudio());
			System.out.println(elementTemplateName);
			// Use player id of firing element rather than projectile? This allows greater
			// flexibility
			Map<String, Object> auxiliaryObjects = spriteQueryHandler.getAuxiliarySpriteConstructionObjectMap(
					nearestEnemyElement);
			GameElement projectileGameElement = gameElementFactory.generateSprite(elementTemplateName,
					new Point2D(element.getX(), element.getY()), auxiliaryObjects);
			newElements.add(projectileGameElement);
		}

	}
	
	private void playAudio(String audioUrl) {
		if(audioUrl != null)
		{
			//audioClipFactory = new AudioClipFactory(audioUrl);
			audioClipFactory = new AudioClipFactory();
			audioClipFactory.getAudioClip().play();
		}
	}


}
