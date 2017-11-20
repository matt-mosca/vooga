package engine.authoring_engine;

import engine.AbstractGameController;
import engine.AuthorController;
import packaging.Packager;
import sprites.Sprite;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Controls the model for a game being authored. Allows the view to modify and retrieve information about the model.
 *
 * @author Ben Schwennesen
 */
public class AuthoringController extends AbstractGameController implements AuthorController {

    private Packager packager;

    private AtomicInteger spriteIdCounter;

    private Map<Integer, Sprite> spriteIdMap;

    public AuthoringController() {
        super();
        packager = new Packager();
        spriteIdCounter = new AtomicInteger();
        spriteIdMap = new HashMap<>();
    }

    @Override
    public void exportGame() {
        getSpriteFactory().exportSpriteTemplates();
        packager.generateJar(getGameName());
    }

    @Override
    public int createElement(String elementName, Map<String, String> properties) {
        Sprite sprite = getSpriteFactory().generateSprite(elementName, properties);
        spriteIdMap.put(spriteIdCounter.incrementAndGet(), sprite);
        return spriteIdCounter.get();
    }

    // TODO - overloaded method for previously created element type

    @Override
    public Map<String, String> getElementProperties(int elementId) throws IllegalArgumentException {
        Sprite sprite = getElement(elementId);
        // TODO - implement
        return null;
    }

    @Override
    public void setElementProperty(int elementId, String propertyName, String propertyValue)
           throws IllegalArgumentException {
        Sprite sprite = getElement(elementId);
        // TODO - implement
    }

    private Sprite getElement(int elementId) {
        if (!spriteIdMap.containsKey(elementId)) {
            throw new IllegalArgumentException();
        }
        return spriteIdMap.get(elementId);
    }

    @Override
    public void createNewLevel(int level) {
        setLevel(level);
        if (!getLevelStatuses().containsKey(level)) {
            getLevelStatuses().put(level, new HashMap<>());
        }
    }

    @Override
    public void loadGameState(String saveName, int level) throws FileNotFoundException {
        loadGameStateElements(saveName, level);
        loadGameState(saveName, level);
    }

    @Override
    public void setStatusProperty(String property, String value) {
        getLevelStatuses().getOrDefault(getCurrentLevel(), new HashMap<>()).put(property, value);
    }

    // TODO - to support multiple clients / interactive editing, need a client-id param (string or int)
    @Override
    public void deleteLevel(int level) throws IllegalArgumentException {
        getLevelStatuses().remove(level);
        getLevelSpritesMap().remove(level);
    }
}
