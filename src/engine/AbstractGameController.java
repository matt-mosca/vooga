package engine;

import sprites.SpriteFactory;
import util.SerializationUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulates the shared fields and behavior between authoring and playing controllers.
 *
 * @author Ben Schwennesen
 */
public abstract class AbstractGameController {

    private SpriteFactory spriteFactory;
    private IOController ioController;

    private Map<Integer, Map<String, String>> levelStatuses;

    private int currentLevel;

    AbstractGameController() {
        SerializationUtils serializationUtils = new SerializationUtils();
        spriteFactory = new SpriteFactory();
        ioController = new IOController(serializationUtils, spriteFactory);
        levelStatuses = new HashMap<>();
        setLevel(1);
    }

    protected SpriteFactory getSpriteFactory() {
        return spriteFactory;
    }

    protected IOController getIoController() {
        return ioController;
    }

    /**
     * Create a new level for the game being authored. Saves the state of the current level being authored when the
     * transition occurs.
     *
     * @param level the number associated with the new level
     */
    protected void setLevel(int level) {
        currentLevel = level;
        spriteFactory.setLevel(currentLevel);
    }

    protected Map<Integer, Map<String, String>> getLevelStatuses() {
        return levelStatuses;
    }
}
