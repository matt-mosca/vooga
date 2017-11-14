package authoring_engine;

import util.SerializationUtils;

/**
 * Top-level authoring controller, gateway of front end GameAuthoringEnv to back
 * end logic and IO
 * 
 * @author radithya
 *
 */

public class AuthoringController {

	// Only the AuthoringController initializes these common modules, passing them
	// down the hierarchy
	private SerializationUtils serializationUtils;
	private AuthoringStateManager authoringStateManager;
	private AuthoringIOController authoringIOController;

	public AuthoringController() {
		serializationUtils = new SerializationUtils();
		authoringIOController = new AuthoringIOController(serializationUtils);
		authoringStateManager = new AuthoringStateManager(authoringIOController);
	}

	// TODO - interface methods
}
