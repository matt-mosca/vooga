package engine.authoring_engine;

import engine.AbstractGameController;
import engine.AuthoringModelController;
import packaging.Packager;
import sprites.Sprite;
import sprites.SpriteFactory;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Controls the model for a game being authored. Allows the view to modify and retrieve information about the model.
 *
 * TODO (for Ben S)
 *      - move sprite map/id into sprite factory or other object (?)
 *      - implement object creation in factory via string properties
 *              + this will entail all behavior object constructors having same parameters
 *                  because reflection won't work otherwise
 *                  (eg) MortalCollider needs same constructor params as ImmortalCollider
 *      - custom error throwing
 * @author radithya
 * @author Ben Schwennesen
 */
public class AuthoringController extends AbstractGameController implements AuthoringModelController {

    private Packager packager;

    // TODO - move these into own object? Or have them in the sprite factory?
    private AtomicInteger spriteIdCounter;
    private Map<Integer, Sprite> spriteIdMap;

    private SpriteFactory spriteFactory;

    public AuthoringController() {
        super();
        packager = new Packager();
        spriteIdCounter = new AtomicInteger();
        spriteIdMap = new HashMap<>();
        spriteFactory = new SpriteFactory();
    }

    @Override
    public void exportGame() {
        spriteFactory.exportSpriteTemplates();
        packager.generateJar(getGameName());
    }

    @Override
    public void defineElement(String elementName, Map<String, String> properties) {
        spriteFactory.defineElement(elementName, properties);
    }

    @Override
    public int placeElement(String elementName, double xCoordinate, double yCoordinate) {
        Sprite sprite = spriteFactory.generateSprite(elementName);
        sprite.setX(xCoordinate);
        sprite.setY(yCoordinate);
        spriteIdMap.put(spriteIdCounter.incrementAndGet(), sprite);
        cacheGeneratedSprite(sprite);
        return spriteIdCounter.get();
    }

    @Override
    public void moveElement(int elementId, double xCoordinate, double yCoordinate) throws IllegalArgumentException {
        Sprite sprite = getElement(elementId);
        sprite.setX(xCoordinate);
        sprite.setY(yCoordinate);
    }

    @Override
    public void updateElementProperties(int elementId, Map<String, String> properties) throws IllegalArgumentException {
        Sprite sprite = getElement(elementId);
        // TODO - implement
    }

    @Override
    public void deleteElement(int elementId) throws IllegalArgumentException {
        Sprite removedSprite = spriteIdMap.remove(elementId);
        getLevelSprites().get(getCurrentLevel()).remove(removedSprite);
    }

    @Override
    public Map<String, String> getElementProperties(int elementId) throws IllegalArgumentException {
        Sprite sprite = getElement(elementId);
        // TODO - implement
        return null;
    }

    private Sprite getElement(int elementId) throws IllegalArgumentException {
        if (!spriteIdMap.containsKey(elementId)) {
            throw new IllegalArgumentException();
        }
        return spriteIdMap.get(elementId);
    }

    @Override
    public void createNewLevel(int level) {
        setLevel(level);
    }

    @Override
    public void loadGameState(String saveName, int level) throws FileNotFoundException {
        loadGameStateElements(saveName, level);
        loadGameStateSettings(saveName, level);
        loadGameConditions(saveName, level);
    }

    @Override
    public void setStatusProperty(String property, String value) {
    		getLevelStatuses().get(getCurrentLevel()).put(property, value);
    }

    // TODO - to support multiple clients / interactive editing, need a client-id param (string or int)
    @Override
    public void deleteLevel(int level) throws IllegalArgumentException {
        getLevelStatuses().remove(level);
        getLevelSprites().remove(level);
        getLevelConditions().remove(level);
    }
    
	@Override
	protected void assertValidLevel(int level) throws IllegalArgumentException {
		if (level < 0 || level > getLevelSprites().size()) {
			throw new IllegalArgumentException();
			// TODO - customize exception ?
		}		
	}

}
