package engine.authoring_engine;

import engine.AbstractGameController;
import engine.AuthoringModelController;
import engine.behavior.movement.TrackingPoint;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import packaging.Packager;
import sprites.Sprite;
import sprites.SpriteFactory;
import util.GameConditionsReader;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
    private GameConditionsReader gameConditionsReader;
    
    // TODO - move these into own object? Or have them in the sprite factory?
    private AtomicInteger spriteIdCounter;
    private Map<Integer, Sprite> spriteIdMap;
    private Map<String, Set<Integer>> templateToIdMap;

    private SpriteFactory spriteFactory;

    public AuthoringController() {
        super();
        packager = new Packager();
        gameConditionsReader = new GameConditionsReader();
        spriteIdCounter = new AtomicInteger();
        spriteIdMap = new HashMap<>();
        templateToIdMap = new HashMap<>();
        spriteFactory = new SpriteFactory();
    }

    @Override
    public void exportGame() {
        spriteFactory.exportSpriteTemplates();
        packager.generateJar(getGameName());
    }
    
	public void setGameDescription(String gameDescription) {
		getLevelDescriptions().set(getCurrentLevel(), gameDescription);
	}
    
	@Override
	public void setVictoryCondition(String conditionIdentifier) {
		getLevelConditions().get(getCurrentLevel()).put(VICTORY, conditionIdentifier);
	}

	@Override
	public void setDefeatCondition(String conditionIdentifier) {
		getLevelConditions().get(getCurrentLevel()).put(DEFEAT, conditionIdentifier);
	}

    @Override
    public void defineElement(String elementName, Map<String, String> properties) {
        spriteFactory.defineElement(elementName, properties);
    }

	@Override
	public void updateElementDefinition(String elementName, Map<String, String> properties, boolean retroactive)
			throws IllegalArgumentException {
		spriteFactory.updateElementDefinition(elementName, properties);
		if (retroactive) {
			updateElementsRetroactively(elementName, properties);
		}
	}
	
	@Override
	public void deleteElementDefinition(String elementName) throws IllegalArgumentException {
		spriteFactory.deleteElementDefinition(elementName);	
	}
    
    @Override
    public int placeElement(String elementTemplateName, Point2D startCoordinates, ImageView graphicalRepresentation) {
        Sprite sprite = spriteFactory.generateSprite(elementTemplateName, startCoordinates, graphicalRepresentation, new HashMap<>());
        return cacheAndCreateIdentifier(elementTemplateName, sprite);
    }

    @Override
    public int placeTrackingElement(String elementTemplateName, Point2D startCoordinates,
                                    ImageView graphicalRepresentation, int idOfSpriteToTrack) {
        TrackingPoint targetLocation = spriteIdMap.get(idOfSpriteToTrack).getPositionForTracking();
        Map<String, Object> auxiliarySpriteConstructionObjects = new HashMap<>();
        auxiliarySpriteConstructionObjects.put(targetLocation.getClass().getName(), targetLocation);
        Sprite sprite = spriteFactory.generateSprite(elementTemplateName, startCoordinates,
                graphicalRepresentation, auxiliarySpriteConstructionObjects);
        return cacheAndCreateIdentifier(elementTemplateName, sprite);
    }

    private int cacheAndCreateIdentifier(String elementTemplateName, Sprite sprite) {
        spriteIdMap.put(spriteIdCounter.incrementAndGet(), sprite);
        cacheGeneratedSprite(sprite);
        int spriteId = spriteIdCounter.get();
        Set<Integer> idsForTemplate = templateToIdMap.getOrDefault(elementTemplateName, new HashSet<>());
        idsForTemplate.add(spriteId);
        templateToIdMap.put(elementTemplateName, idsForTemplate);
        return spriteId;
    }

    @Override
    public void moveElement(int elementId, double xCoordinate, double yCoordinate) throws IllegalArgumentException {
        Sprite sprite = getElement(elementId);
        sprite.setX(xCoordinate);
        sprite.setY(yCoordinate);
    }

    @Override
    public void updateElementProperties(int elementId, Map<String, String> propertiesToUpdate) throws IllegalArgumentException {
        updateElementPropertiesById(elementId, propertiesToUpdate);
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
    
	@Override
	public Map<String, String> getTemplateProperties(String elementName) throws IllegalArgumentException {
		return spriteFactory.getTemplateProperties(elementName);
	}
	
	@Override
	public Map<String, Map<String, String>> getAllDefinedTemplateProperties() {
		return spriteFactory.getAllDefinedTemplateProperties();
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
    public void setStatusProperty(String property, String value) {
    		getLevelStatuses().get(getCurrentLevel()).put(property, value);
    }

    // TODO - to support multiple clients / interactive editing, need a client-id param (string or int)
    @Override
    public void deleteLevel(int level) throws IllegalArgumentException {
        getLevelStatuses().remove(level);
        getLevelSprites().remove(level);
        getLevelConditions().remove(level);
        getLevelDescriptions().remove(level);
    }
    
	@Override
	public Collection<String> getPossibleVictoryConditions() {
		return gameConditionsReader.getPossibleVictoryConditions(); 
	}

	@Override
	public Collection<String> getPossibleDefeatConditions() {
		return gameConditionsReader.getPossibleDefeatConditions();
	}
    
	@Override
	protected void assertValidLevel(int level) throws IllegalArgumentException {
		if (level <= 0 || level > getLevelSprites().size()) {
			throw new IllegalArgumentException();
			// TODO - customize exception ?
		}		
	}
	
	private void updateElementsRetroactively(String elementName, Map<String, String> propertiesToUpdate) {
		Set<Integer> idsForTemplate = templateToIdMap.getOrDefault(elementName, new HashSet<>());
		for (int elementId : idsForTemplate) {
			updateElementPropertiesById(elementId, propertiesToUpdate);
		}
	}
	
	private void updateElementPropertiesById(int elementId, Map<String, String> propertiesToUpdate) {
		 // TODO - can't use old method
	}

}
