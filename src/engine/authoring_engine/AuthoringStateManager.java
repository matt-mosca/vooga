package engine.authoring_engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import engine.IOController;
import engine.StateManager;
import sprites.Sprite;

/**
 * Single-source of truth for game settings and created / added elements when
 * authoring
 * 
 * @author radithya
 *
 */
public class AuthoringStateManager extends StateManager {

	private List<Collection<Sprite>> elementsPerLevel;

	public AuthoringStateManager(IOController authoringIOController) {// , ElementFactory elementFactory) {
		super(authoringIOController);// , elementFactory);
		elementsPerLevel.add(new ArrayList<Sprite>()); // Leave index 0 blank to facilitate 1-indexing from authoring
	}

	@Override
	public void saveGameState(String savedGameName) {
		// Serialize separately for every level
		Map<Integer, String> serializedLevelsData = new HashMap<>();
		for (int level = 0; level < elementsPerLevel.size(); level++) {
			serializedLevelsData.put(level, getIOController().getLevelSerialization(getDescription(), getStatus(),
					elementsPerLevel.get(level)));
		}
		// Serialize map of level to per-level serialized data
		getIOController().saveGameStateForMultipleLevels(savedGameName, serializedLevelsData,
				AuthoringConstants.IS_AUTHORING);
	}

	// TODO
	Sprite createElement(String name, Map<String, String> properties) {
		// Assume ElementFactory has 2 element-creation signatures - one with only
		// String name, which will first check a local cache of newly created elements
		// for the properties and if miss, read properties from file and populate cache
		// with it
		// and another which takes the properties directly as parameter and populates
		// the cache with it
		/*
		 * return getElementFactory().createElement(name, properties);
		 */
		// Alternative : Save to file so that ElementFactory can simply read from file
		// as usual? But
		// this could cause problems with unintentional overwriting of user's
		// save-points
		return null; // TEMP
	}

	@Override
	public Sprite placeElement(String elementName, double x, double y) {
		return placeElement(elementName, x, y, getCurrentLevel());
	}

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
	Sprite addElement(String name, int level) throws IllegalArgumentException {
		assertValidLevel(level);
		// Use ElementFactory to construct Sprite from elementName with these
		// coordinates, ElementFactory will retrieve info from file since element
		// already exists
		/*
		 * Sprite elementToPlace = getElementFactory().instantiateElement(elementName);
		 * // Add created Sprite to gameElements currentElements.get(level).add(elementToPlace);
		 * return elementToPlace;
		 */
		return null; // TEMP
	}

	// TODO
	Sprite updateElement(String name, double x, double y, int level, Map<String, String> customProperties)
			throws IllegalArgumentException {
		assertValidLevel(level);
		// find element and customize its properties
		Sprite elementToUpdate = findElementByLevelAndPosition(level, x, y);
		// Pass sprite and new properties to ElementFactory to let it update sprite
		// accordingly by calling Sprite API methods
		/*
		 * elementFactory.updateElement(elementToUpdate, customProperties);
		 */
		return elementToUpdate;
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
		}
	}

	// TODO - more efficient implementation?
	private Sprite findElementByLevelAndPosition(int level, double x, double y) throws IllegalArgumentException {
		assertValidLevel(level);
		Collection<Sprite> elementsForLevel = elementsPerLevel.get(level);
		for (Sprite element : elementsForLevel) {
			if (element.getX() == x && element.getY() == y) {
				return element;
			}
		}
		return null;
	}

}
