package engine;

import sprites.Sprite;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.List;
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
     *
     * @param saveName the name the save file was assigned
     * @param level    the level of the game which should be loaded
     */
    void loadGameState(String saveName, int level) throws FileNotFoundException;

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
     * Place an element into the game. The element will be created using cached properties specified during authoring.
     *
     * @return a sprite abstraction representing the game element
     */
    Sprite placeElement(String elementName, double x, double y);

    /**
     * Get the high-level status of a game in-progress, notably lives remaining and resources available.
     *
     * @return a map of relevant details to display or modify about the game
     */
    Map<String, String> getStatus();

    /**
     * Get the elements of a game (represented as sprites) for a particular level.
     *
     * TODO - custom exception?
     *
     * @param level    the level of the game which should be loaded
     * @return all the game elements (sprites) represented in the level
     * @throws IllegalArgumentException if there is no corresponding level in the current game
     */
    Collection<Sprite> getLevelSprites(int level) throws IllegalArgumentException;
}
