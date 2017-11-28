package engine;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Map;

/**
 * Controls the model for a game being authored. Allows the view to modify and
 * retrieve information about the model.
 *
 * @author Ben Schwennesen
 */
public interface AuthoringModelController {

	/**
	 * Save the current state of the current level a game being authored.
	 *
	 * @param saveName
	 *            the name to assign to the save file
	 */
	void saveGameState(String saveName);

	/**
	 * Load the detailed state of a game for a particular level, including
	 * high-level information and elements present.
	 *
	 * @param saveName
	 *            the name used to save the game authoring data
	 * @param level
	 *            the level of the game which should be loaded
	 * @throws FileNotFoundException
	 *             if the save name does not refer to an existing file
	 */
	void loadOriginalGameState(String saveName, int level) throws FileNotFoundException;

	/**
	 * Export a fully authored game, including all levels, into an executable file.
	 */
	void exportGame();

	/**
	 * Create a new level for the game being authored. Saves the state of the
	 * current level being authored when the transition occurs.
	 *
	 * @param level
	 *            the number associated with the new level
	 */
	void createNewLevel(int level);

	/**
	 * Delete the previously created level.
	 *
	 * @param level
	 *            the level to delete
	 * @throws IllegalArgumentException
	 *             if level does not exist
	 */
	void deleteLevel(int level) throws IllegalArgumentException;

	/**
	 * Define a new type of element for the game being authored. Elements of this
	 * type will be created by the model based on its properties, assuming defaults
	 * where necessary. This method should not be used for updating properties of an
	 * existing template, the updateElementDefinition method should be used for that
	 * instead.
	 *
	 * @param elementName
	 *            the template name assigned to this element, for future reuse of
	 *            the properties
	 * @param properties
	 *            a map containing the properties of the element to be created
	 * @throws IllegalArgumentException
	 *             if the template already exists.
	 */
	void defineElement(String elementName, Map<String, String> properties) throws IllegalArgumentException;

	/**
	 * Update an existing template by overwriting the specified properties to their
	 * new specified values. Should not be used to create a new template, the
	 * defineElement method should be used for that.
	 * 
	 * @param elementName
	 *            the name of the template to be updated
	 * @param propertiesToUpdate
	 *            the properties to update
	 * @param retroactive
	 *            whether previously created elements of this type must have their
	 *            properties updated
	 * 
	 * @throws IllegalArgumentException
	 *             if the template does not already exist
	 */
	void updateElementDefinition(String elementName, Map<String, String> propertiesToUpdate, boolean retroactive)
			throws IllegalArgumentException;

	/**
	 * Delete a previously defined template
	 * 
	 * @param elementName
	 *            name of the template to delete
	 * @throws IllegalArgumentException
	 *             if the template does not already exist
	 */
	void deleteElementDefinition(String elementName) throws IllegalArgumentException;

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
	 * Move a previously created game element to a new location.
	 *
	 * @param elementId
	 *            elementId the unique identifier for the element
	 * @param xCoordinate
	 *            the new horizontal position of the element within the game
	 * @param yCoordinate
	 *            the new vertical position of the element within the game
	 */
	void moveElement(int elementId, double xCoordinate, double yCoordinate);

	/**
	 * Update the properties of a particular game element, without changing the
	 * definition of its type.
	 *
	 * @param elementId
	 *            the unique identifier for the element
	 * @param propertiesToUpdate
	 *            a map containing the new properties of the element
	 */
	void updateElementProperties(int elementId, Map<String, String> propertiesToUpdate);

	/**
	 * Delete a previously created game element.
	 *
	 * @param elementId
	 *            the unique identifier for the element
	 */
	void deleteElement(int elementId);

	/**
	 * Get a map of properties for a particular game element, so as to allow for
	 * their displaying in a modification area of the display.
	 *
	 * @param elementId
	 *            the unique identifier for the game element
	 * @return a map of properties for the element with this identifier
	 * @throws IllegalArgumentException
	 *             if the element ID does not refer to a created element
	 */
	Map<String, String> getElementProperties(int elementId) throws IllegalArgumentException;

	/**
	 * Get a map of properties for an element template / model, so as to allow for
	 * their displaying in a modification area of the display
	 * 
	 * @param elementName
	 *            the template name for the element
	 * @return a map of properties for the template with this identifier
	 * @throws IllegalArgumentException
	 *             if the element name does not refer to a defined template
	 */
	Map<String, String> getTemplateProperties(String elementName) throws IllegalArgumentException;

	/**
	 * Get map of all defined template names to their properties
	 * 
	 * @return map of template names to properties of each template
	 */
	Map<String, Map<String, String>> getAllDefinedTemplateProperties();

	/**
	 * Set the name of the game being authored.
	 *
	 * @param gameName
	 *            the name of the game
	 */
	void setGameName(String gameName);

	/**
	 * Set the description of a game being authored.
	 *
	 * @param gameDescription
	 *            the description authored for the game
	 */
	void setGameDescription(String gameDescription);

	/**
	 * Set the victory condition for the current level of the game being authored
	 * 
	 * @param conditionIdentifier
	 *            the description of the victory condition, which can be mapped to a
	 *            boolean state function
	 */
	void setVictoryCondition(String conditionIdentifier);

	/**
	 * Set the defeat condition for the current level of the game being authored
	 * 
	 * @param conditionIdentifier
	 *            the description of the defeat condition, which can be mapped to a
	 *            boolean state function
	 */
	void setDefeatCondition(String conditionIdentifier);

	/**
	 * Set a top-level game status property (e.g. lives, starting resources, etc)
	 *
	 * @param property
	 *            name of the property to set
	 * @param value
	 *            string representation of the property's new value
	 */
	void setStatusProperty(String property, String value);

	/**
	 * Retrieve a collection of descriptions of the possible victory conditions
	 * 
	 * @return a collection of strings describing the possible victory conditions
	 *         that can be assigned for a given level
	 */
	Collection<String> getPossibleVictoryConditions();

	/**
	 * Retrieve a collection of descriptions of the possible defeat conditions
	 * 
	 * @return a collection of strings describing the possible defeat conditions
	 *         that can be assigned for a given level
	 */
	Collection<String> getPossibleDefeatConditions();

}
