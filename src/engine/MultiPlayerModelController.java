package engine;

import java.io.File;
import java.util.Set;

/**
 * Interface to capture any additional metadata related to multiplayer
 * environment, wraps around PlayModelController
 * 
 * @author radithya
 *
 */
public interface MultiPlayerModelController extends PlayModelController {

	/**
	 * Initialize a game room for the game of the given name, retrieving a string
	 * identifier for this game room
	 * 
	 * @param gameName
	 *            name of the game to load
	 * @return unique string identifier (among currently active game rooms) for the
	 *         newly created game room
	 */
	String createGameRoom(String gameName);

	/**
	 * Join the currently active game room identified by the given game room name,
	 * with the given username
	 * 
	 * @param gameRoomName
	 *            name of a currently active game room
	 * @param userName
	 *            username to use
	 * @return true if successfully joined, false otherwise (game room not active,
	 *         userName taken)
	 */
	boolean joinGameRoom(String gameRoomName, String userName);

	/**
	 * Retrieve set of names of currently active game rooms that can be joined
	 * 
	 * @return set of string identifiers for currently active game rooms
	 */
	Set<String> getGameRooms();

	/**
	 * Retrieve set of usernames for the game
	 * 
	 * @param gameRoomName
	 *            name of game room
	 * @return set of usernames
	 */
	Set<String> getPlayerNames(String gameRoomName);

	/**
	 * Saving not allowed for multiplayer games
	 */
	@Override
	void saveGameState(File fileToSaveTo) throws UnsupportedOperationException;

}
