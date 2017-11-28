package engine;

import sprites.Sprite;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Map;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

/**
 * Controls the model for a game being played. Allows the view to modify and
 * retrieve information about the model.
 *
 * @author Ben Schwennesen
 */
public interface PlayModelController {

	/**
	 * Save the current state of a game being played.
	 *
	 * @param saveName
	 *            the name to assign to the save file
	 */
	void saveGameState(String saveName);

	/**
	 * Load the detailed state of original (authored) game for a particular level,
	 * including high-level information and elements present.
	 *
	 * @param saveName
	 *            the name the save file was assigned
	 * @param level
	 *            the level of the game which should be loaded
	 */
	void loadOriginalGameState(String saveName, int level) throws FileNotFoundException;

	/**
	 * Load state of previously saved play
	 * 
	 * @param savePlayStateName
	 *            name of previously saved play state
	 * @throws FileNotFoundException
	 *             if no such saved play state exists
	 */
	void loadSavedPlayState(String savePlayStateName) throws FileNotFoundException;

	/**
	 * Run one cycle of update
	 */
	void update();

	/**
	 * Pause the game.
	 */
	void pause();

	/**
	 * Resume the (paused) game.
	 */
	void resume();

	/**
	 * Determine whether the game in-progress has been lost.
	 *
	 * @return true if the game has been completed with a loss and false otherwise
	 */
	boolean isLost();

	/**
	 * Determine whether the game in-progress has been won.
	 *
	 * @return true if the game has been completed with a loss and false otherwise
	 */
	boolean isWon();

	/**
	 * Place a game element of previously defined (or default) type within the game.
	 *
	 * @param elementName
	 *            the template name for the element
	 * @param startCoordinates
	 *            the coordinates at which the element should be placed
	 * @param graphicalRepresentation
	 *            the frontend representation of the element
	 * @return a unique identifier for the sprite abstraction representing the game
	 *         element
	 */
	int placeElement(String elementName, Point2D startCoordinates, ImageView graphicalRepresentation);

	/**
	 * Place a game element of previously defined (or default) type within the game.
	 * Special case where the element tracks the movement of another game element.
	 *
	 * @param elementName
	 *            the template name for the element
	 * @param startCoordinates
	 *            the coordinates at which the element should be placed
	 * @param graphicalRepresentation
	 *            the frontend representation of the element
	 * @param idOfElementToTrack
	 *            the unique identifier of the (previously placed) element to track
	 * @return a unique identifier for the sprite abstraction representing the game
	 *         element
	 */
	int placeTrackingElement(String elementName, Point2D startCoordinates, ImageView graphicalRepresentation,
			int idOfElementToTrack);


	/**
	 * Get the high-level status of a game in-progress, notably lives remaining and
	 * resources available.
	 *
	 * @return a map of relevant details to display or modify about the game
	 */
	Map<String, String> getStatus();

	/**
	 * Get the elements of a game (represented as sprites) for a particular level.
	 *
	 * TODO - custom exception?
	 *
	 * @param level
	 *            the level of the game which should be loaded
	 * @return all the game elements (sprites) represented in the level
	 * @throws IllegalArgumentException
	 *             if there is no corresponding level in the current game
	 */
	Collection<Integer> getLevelSprites(int level) throws IllegalArgumentException;
}
