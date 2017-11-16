package engine.authoring_engine;

import java.util.Collection;
import java.util.Map;
import engine.GameController;
import engine.IOController;
import engine.StateManager;
import sprites.Sprite;
import util.SerializationUtils;

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
		authoringStateManager = new AuthoringStateManager(getIOController());
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
		return getStateManager().createElement(name, properties);
	}

	/**
	 * Add a previously defined element type to the game (level's) inventory BUT NOT
	 * on map
	 * 
	 * @param name
	 *            the identifier for this previously created type
	 * @param level
	 *            level of the game this element is being added for
	 */
	public Sprite addElement(String name, int level) {
		return getStateManager().addElement(name, level);
	}

	/**
	 * 
	 * @param name
	 *            name of element type
	 * @param x
	 *            xCoordinate of previously created element
	 * @param y
	 *            yCoordinate of previously created element
	 * @param level
	 *            level of the game this element is being added for
	 * @param customProperties
	 *            map of properties to override for this specific instance
	 */
	public Sprite updateElement(String name, double x, double y, int level, Map<String, String> customProperties) {
		return getStateManager().updateElement(name, x, y, level, customProperties);
	}
	
	/**
	 * 
	 * @param name
	 *            name of element type
	 * @param x
	 *            xCoordinate of previously created element
	 * @param y
	 *            yCoordinate of previously created element
	 * @param level
	 *            level of the game this element is being added for
	 * @param customProperties
	 *            map of properties to override for this specific instance
	 */
	public Sprite updateInventoryElement(String name, int level, Map<String, String> customProperties) {
		//TODO
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

}
