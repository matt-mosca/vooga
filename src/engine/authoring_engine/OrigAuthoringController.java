package engine.authoring_engine;

import java.util.Map;
import engine.GameController;
import sprites.Sprite;
import sprites.SpriteFactory;
import util.SerializationUtils;

/**
 * Top-level authoring controller, gateway of front end GameAuthoringEnv to back
 * end logic and IO
 * 
 * @author radithya
 * @deprecated
 *
 */
public class OrigAuthoringController extends GameController {

	private AuthoringStateManager authoringStateManager;

	public OrigAuthoringController() {
		super();
		authoringStateManager = new AuthoringStateManager(getIOController(), new SpriteFactory());
	}

	@Override
	protected AuthoringStateManager getStateManager() {
		return authoringStateManager;
	}

	@Override
	public boolean isAuthoring() {
		return true;
	}

	/**
	 * Define a new element type
	 * 
	 * @param name
	 *            the name of new element to be created
	 * @param properties
	 *            map of properties for the new element, of the form {"image_url":
	 *            <url>, "hp" : <hp>, ...}
	 */
	public Sprite createElement(String name, Map<String, String> properties) {
		// return getStateManager().defineElement(name, properties);
		return null;
	}

	/**
	 * Add a previously defined element type to the game (level's) inventory BUT NOT
	 * on map
	 * 
	 * @param name
	 *            the identifier for this previously created type
	 * @param level
	 *            level of the game this element is being added for
	 * @throws IllegalArgumentException
	 *             if level does not exist
	 * @return a unique ID for the element
	 */
	public Sprite addElement(String name, int level) throws IllegalArgumentException {
		return getStateManager().addElement(name, level);
	}

	/*
	 *
	 * If we return the sprites to them, why can't they make a call to do this themselves?
	 *
	 * @param spriteId
	 * 			  unique identifier for the sprite to modify
	 * @param level
	 *            level of the game this element is being added for
	 * @param customProperties
	 *            map of properties to override for this specific instance
	 * @throws IllegalArgumentException
	 *             if level does not exist
	 *
	public void updateElement(int spriteId, int level, Map<String, Object> customProperties)
			throws IllegalArgumentException {
		getStateManager().updateElement(spriteId, level, customProperties);
	}*/

	/**
	 * 
	 * @param name
	 *            name of element type
	 * @param level
	 *            level of the game this element is being added for
	 * @param customProperties
	 *            map of properties to override for this specific instance
	 * @throws IllegalArgumentException
	 *             if level does not exist
	 */
	public Sprite updateInventoryElement(String name, int level, Map<String, String> customProperties)
			throws IllegalArgumentException {
		// TODO
		return null;
	}

	/**
	 * Set a top-level game property (e.g. lives, starting resources, etc)
	 * 
	 * @param property
	 *            name
	 * @param value
	 *            string representation of the value
	 */
	public void setGameParam(String property, String value) {
		getStateManager().setGameParam(property, value);
	}

	// TODO - to support multiple clients / interactive editing, need a client-id
	// param (string or int)
	/**
	 * Delete the previously created level
	 * 
	 * @param level
	 *            the level to delete
	 * @throws IllegalArgumentException
	 *             if level does not exist
	 */
	public void deleteLevel(int level) throws IllegalArgumentException {
		getStateManager().deleteLevel(level);
	}

}
