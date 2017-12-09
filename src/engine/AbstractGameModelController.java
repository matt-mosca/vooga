package engine;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

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
	public void loadOriginalGameState(String saveName, int level) throws IOException;

	public Map<String, String> getTemplateProperties(String elementName) throws IllegalArgumentException;
	
	public Map<String, Map<String, String>> getAllDefinedTemplateProperties();

	public int placeElement(String elementTemplateName, Point2D startCoordinates);

	public Set<String> getInventory();
	
	@Deprecated
	public ImageView getRepresentationFromSpriteId(int spriteId);

	/**
	 * Get resources left for current level
	 * @deprecated
	 * @return map of resource name to quantity left
	 */
	public Map<String, Double> getStatus();

	public Map<String, Double> getResourceEndowments();

	public Map<String, Map<String, Double>> getElementCosts();

	/**
	 * Fetch all available game names and their corresponding descriptions
	 * 
	 * @return map where keys are game names and values are game descriptions
	 */
	public Map<String, String> getAvailableGames() throws IllegalStateException;

}
