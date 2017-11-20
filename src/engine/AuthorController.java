package engine;

import sprites.Sprite;

import java.io.FileNotFoundException;
import java.util.Map;

/**
 * Controls the model for a game being authored. Allows the view to modify and retrieve information about the model.
 *
 * @author Ben Schwennesen
 */
public interface AuthorController {

    /**
     * Save the current state of the current level a game being authored.
     *
     * @param saveName the name to assign to the save file
     */
    void saveGameState(String saveName);

    /**
     * Load the detailed state of a game for a particular level, including high-level information and elements present.
     *
     * @param saveName the name used to save the game authoring data
     * @param level    the level of the game which should be loaded
     * @throws FileNotFoundException if the save name does not refer to an existing file
     */
    void loadGameState(String saveName, int level) throws FileNotFoundException;

    /**
     * Export a fully authored game, including all levels, into an executable file.
     */
    void exportGame();

    /**
     * Create a new level for the game being authored. Saves the state of the current level being authored when the
     * transition occurs.
     *
     * @param level the number associated with the new level
     */
    void createNewLevel(int level);

    /**
     * Delete the previously created level.
     *
     * @param level the level to delete
     * @throws IllegalArgumentException if level does not exist
     */
    void deleteLevel(int level) throws IllegalArgumentException;

    /**
     * Generate a new type of element for the game being authored. The element will be created by the model based on
     * its properties, assuming defaults where necessary.
     *
     * @param elementName the template name assigned to this element, for future reuse of the properties
     * @param properties  a map containing the properties of the element to be created
     * @return a unique identifier for the sprite abstraction representing the game element
     */
    int createElement(String elementName, Map<String, String> properties);

    /**
     * Get a map of properties for a particular game element, so as to allow for their displaying in a modification
     * area of the display.
     *
     * @param elementId the unique identifier for the game element
     * @return a map of properties for the element with this identifier
     *
     * @throws IllegalArgumentException if the element ID does not refer to a created element
     */
    Map<String, String> getElementProperties(int elementId) throws IllegalArgumentException;

    /**
     * Set a particular property for a particular game element.
     *
     * @param elementId     the unique identifier for the game element
     * @param propertyName  the name of the property to modify
     * @param propertyValue the value to which the property should be set, as a string
     *
     * @throws IllegalArgumentException if the element ID does not refer to a created element
     */
    void setElementProperty(int elementId, String propertyName, String propertyValue) throws IllegalArgumentException;

    /**
     * Set the name of the game being authored.
     *
     * @param gameName the name of the game
     */
    void setGameName(String gameName);

    /**
     * Set the description of a game being authored.
     *
     * @param gameDescription the description authored for the game
     */
    void setGameDescription(String gameDescription);

    /**
     * Set a top-level game status property (e.g. lives, starting resources, etc)
     *
     * @param property name of the property to set
     * @param value    string representation of the property's new value
     */
    void setStatusProperty(String property, String value);
}
