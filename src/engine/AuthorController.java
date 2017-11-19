package engine;

import sprites.Sprite;

import java.io.FileNotFoundException;

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
     * Generate a new type of element for the game being authored. The element will be created by the model based on
     * its serialized properties.
     *
     * @param elementName          the template name assigned to this element, for future reuse of the properties
     * @param serializedProperties a serialization of the element's properties, assuming default when necessary
     * @return a sprite abstraction representing the game element
     */
    Sprite createElement(String elementName, String serializedProperties);

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
     * Create a new level for the game being authored. Saves the state of the current level being authored when the
     * transition occurs.
     *
     * @param level the number associated with the new level
     */
    void createNewLevel(int level);
}
