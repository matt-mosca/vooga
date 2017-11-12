package authoring_engine;

import io.GamePersistence;
import util.SerializationUtils;

/**
 * Handles authoring requests from front end GameAuthoringEnv, delegating the
 * processing and saving of valid requests end work to utils and io modules
 * 
 * @author radithya
 *
 */
public class AuthoringIOController {

	private SerializationUtils serializationUtils;
	private GamePersistence gamePersistence;
	
	
	public AuthoringIOController(SerializationUtils serializationUtils) {
		this.serializationUtils = serializationUtils;
		gamePersistence = new GamePersistence();
	}
	
	// TODO - authoring_engine interface methods

}
