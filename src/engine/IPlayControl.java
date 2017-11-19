package engine;

import sprites.Sprite;

import java.util.Collection;
import java.util.Map;

/**
 * Controls the model for a game being played. Allows the view to modify and retrieve information about the model.
 *
 * @author Ben Schwennesen
 */
public interface IPlayControl {

    /**
     * Save the current state of a game being played.
     *
     * @param saveName the name to assign to the save file
     */
    void saveGameState(String saveName);

    /**
     * Load the detailed state of a game for a particular level, including high-level information and elements present.

     * @param level the level of the game which should be loaded
     */
    void loadGameState(int level);

    /**
     * Place an element into the game. The element will be created using cached properties specified during authoring.
     *
     * @return a sprite abstraction representing the game element
     */
    Sprite placeElement(String elementName, double x, double y);

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
     * Get the high-level status of a game in-progress, notably lives remaining and resources available.
     *
     * @return a map of relevant details to display or modify about the game
     */
    Map<String, String> getStatus();

    /**
     * Get the elements of a game (represented as sprites) for a particular level.
     *
     * @param gameName the name of the game being loaded
     * @param level    the level of the game which should be loaded
     * @return all the game elements (sprites) represented in the level
     */
    Collection<Sprite> getLevelSprites(String gameName, int level);
}
