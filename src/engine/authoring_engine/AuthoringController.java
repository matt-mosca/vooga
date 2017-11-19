package engine.authoring_engine;

import java.util.Map;
import engine.GameController;
import sprites.Sprite;
import sprites.SpriteFactory;

/**
 * Top-level authoring controller, gateway of front end GameAuthoringEnv to back
 * end logic and IO
 * 
 * @author radithya
 *
 */

public class AuthoringController extends GameController {

	private AuthoringStateManager authoringStateManager;

	public AuthoringController() {
		super();
		authoringStateManager = new AuthoringStateManager(getIOController(), new SpriteFactory());
	}

	@Override
	protected AuthoringStateManager getStateManager() {
		return authoringStateManager;
	}

	@Override
	public boolean isAuthoring() {
		return AuthoringConstants.IS_AUTHORING;
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
		// return getStateManager().createElement(name, properties);
		return null;
		// TODO - this should return an integer id for the frontend to use to access it in the future
		// (prevents exposing the Sprite objects to the frontend)
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

	/**
	 * @param spriteId
	 * 			  unique identifier for the sprite to modify
	 * @param customProperties
	 *            map of properties to override for this element
	 * @throws IllegalArgumentException
	 *             if level does not exist
	 */
	public void updateElement(int spriteId, Map<String, Object> customProperties)
			throws IllegalArgumentException {
		getStateManager().updateElement(spriteId, customProperties);
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
