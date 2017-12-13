package engine;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javafx.geometry.Point2D;
import networking.protocol.PlayerServer.LevelInitialized;
import networking.protocol.PlayerServer.NewSprite;

/**
 * Represents the common back-end methods available across play and authoring
 * 
 * @author radithya
 *
 */
public interface AbstractGameModelController {

	/**
	 * Save the current state of the current level a game being played or authored.
	 *
	 * @param saveName
	 *            the name to assign to the save file
	 */
	public void saveGameState(File saveName);

	/**
	 * Load the detailed state of the original authored game for a particular level,
	 * including high-level information and elements present.
	 *
	 * @param saveName
	 *            the name used to save the game authoring data
	 * @param level
	 *            the level of the game which should be loaded
	 * @throws IOException
	 *             if the save name does not refer to existing files
	 */
	LevelInitialized loadOriginalGameState(String saveName, int level) throws IOException;

	Map<String, Object> getTemplateProperties(String elementName) throws IllegalArgumentException;

	Map<String, Map<String, Object>> getAllDefinedTemplateProperties();

	/**
	 * Place a game element into the game area.
	 *
	 * @param elementTemplateName the name of the element template to use for the instance being places
	 * @param startCoordinates    the desired starting location of the element
	 * @return a message containing the necessary information for the frontend to keep track of the element
	 */
	NewSprite placeElement(String elementTemplateName, Point2D startCoordinates) throws ReflectiveOperationException;

	int getNumLevelsForGame(String gameName, boolean forOriginalGame);
	
	int getCurrentLevel();
	
	int getLevelHealth(int level);
	
	Set<String> getInventory();

	/**
	 * Get resources left for current level
	 * 
	 * @deprecated
	 * @return map of resource name to quantity left
	 */
	Map<String, Double> getStatus();

	Map<String, Double> getResourceEndowments();

	Map<String, Map<String, Double>> getElementCosts();
	
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
	Collection<NewSprite> getLevelSprites(int level) throws IllegalArgumentException;

	/**
	 * Fetch all available game names and their corresponding descriptions
	 * 
	 * @return map where keys are game names and values are game descriptions
	 */
	Map<String, String> getAvailableGames() throws IllegalStateException;
	
}
