package engine.authoring_engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import engine.IOController;
import engine.StateManager;
import sprites.Sprite;
import sprites.SpriteFactory;

/**
 * Single-source of truth for game settings and created / added elements when
 * authoring
 * 
 * @author radithya
 * @author Ben Schwennesen
 *
 */
public class AuthoringStateManager extends StateManager {

	private List<Collection<Sprite>> elementsPerLevel;
	private Map<Integer, Sprite> spritesByIds;
	private AtomicInteger uniqueSpriteId;

	public AuthoringStateManager(IOController authoringIOController, SpriteFactory spriteFactory) {
		super(authoringIOController, spriteFactory);
		elementsPerLevel.add(new ArrayList<Sprite>()); // Leave index 0 blank to facilitate 1-indexing from authoring
		uniqueSpriteId = new AtomicInteger();
		spritesByIds = new HashMap<>();
	}

	@Override
	public void saveGameState(String savedGameName) {
		// Serialize separately for every level
		Map<Integer, String> serializedLevelsData = new HashMap<>();
		for (int level = 0; level < elementsPerLevel.size(); level++) {
			serializedLevelsData.put(level, getIOController().getLevelSerialization(level, getDescription(),
					getStatus()));
		}
		// Serialize map of level to per-level serialized data
		getIOController().saveGameStateForMultipleLevels(savedGameName, serializedLevelsData,
				AuthoringConstants.IS_AUTHORING);
	}

	int createElement(String templateName, Map<String, Object> properties) {
		try {
			Sprite sprite = getSpriteFactory().generateSprite(templateName, properties);
			spritesByIds.put(uniqueSpriteId.incrementAndGet(), sprite);
			// (id prevents exposing the Sprite objects to the frontend)
		} catch (ReflectiveOperationException e){
			// TODO - throw custom exception
		}
		return uniqueSpriteId.get();
	}

	// Why is this returning a sprite?
	@Override
	public Sprite placeElement(String elementName, double x, double y) {
		return placeElement(elementName, x, y, getCurrentLevel());
	}

	// why do we need this?
	@Override
	public void setCurrentElements(Collection<Sprite> newElements) {
		elementsPerLevel.set(getCurrentLevel(), newElements);
	}

	@Override
	public Collection<Sprite> getCurrentElements() {
		// No need to check for 'active' from perspective of authoring
		// env ?
		return elementsPerLevel.get(getCurrentLevel());
	}

	Sprite placeElement(String elementName, double x, double y, int level) throws IllegalArgumentException {
		assertValidLevel(level);
		// Use ElementFactory to construct Sprite from elementName with these
		// coordinates, ElementFactory will retrieve info from file since element
		// already exists
		/*
		 * Sprite elementToPlace = addElement(elementName, level); // Add created Sprite
		 * to current level elementToPlace.setActive(); elementToPlace.setX(x);
		 * elementToPlace.setY(y); return elementToPlace;
		 */
		return null; // TEMP
	}

	// TODO
	int addElement(String templateName, int level) throws IllegalArgumentException {
		assertValidLevel(level);
		try {
			getSpriteFactory().setLevel(level);
			Sprite sprite = getSpriteFactory().generateSprite(templateName);
			spritesByIds.put(uniqueSpriteId.incrementAndGet(), sprite);
			// (id prevents exposing the Sprite objects to the frontend)
		} catch (ReflectiveOperationException e){
			// TODO - throw custom exception
		}
		return uniqueSpriteId.get();
	}

	// TODO
	void updateElement(int spriteId, int level, Map<String, Object> customProperties)
			throws IllegalArgumentException {
		assertValidLevel(level);
		if (spritesByIds.containsKey(spriteId)) {
			Sprite spriteToUpdate = spritesByIds.get(spriteId);
			spriteToUpdate.setProperties(customProperties);
		}
	}

	void setGameParam(String property, String value) {
		getStatus().put(property, value);
	}

	void deleteLevel(int level) throws IllegalArgumentException {
		assertValidLevel(level);
		elementsPerLevel.remove(level);
	}

	@Override
	protected void assertValidLevel(int level) throws IllegalArgumentException {
		if (level < 0 || level > elementsPerLevel.size()) {
			throw new IllegalArgumentException();
			// TODO - customize exception ?
		}
	}
}
