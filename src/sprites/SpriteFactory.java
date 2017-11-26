package sprites;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Generates spite objects for displaying during authoring and gameplay.
 *
 * @author Ben Schwennesen
 */
public class SpriteFactory {

	private final String PROPERTIES_COMMENT = "Programmatically generated sprite template file";
	private final String TEMPLATE_FILE_OUTPUT_PATH = "data/sprite-templates/";
	private final String PROPERTIES_EXTENSION = ".properties";

	private Map<String, Map<String, String>> spriteTemplates = new HashMap<>();

	/**
	 * Define a new template with specified properties. Should not be used to update
	 * existing templates, the updateElementDefinition method should be used for
	 * that
	 *
	 * @param spriteTemplateName
	 *            - the name of the sprite template
	 * @param properties
	 *            - a map of properties for sprites using this template
	 * @throws IllegalArgumentException
	 *             if the template already exists
	 */
	public void defineElement(String spriteTemplateName, Map<String, String> properties)
			throws IllegalArgumentException {
		if (spriteTemplates.containsKey(spriteTemplateName)) {
			// TODO - custom exception?
			throw new IllegalArgumentException();
		}
		spriteTemplates.put(spriteTemplateName, properties);
	}

	/**
	 * Update an existing template by overwriting the specified properties to their
	 * new specified values. Should not be used to create a new template, the
	 * defineElement method should be used for that.
	 * 
	 * @param spriteTemplateName
	 *            - the name of the sprite template to update
	 * @param propertiesToUpdate
	 *            - map of property keys to update and their new values
	 * @throws IllegalArgumentException
	 *             if the template does not already exist
	 */
	public void updateElementDefinition(String spriteTemplateName, Map<String, String> propertiesToUpdate)
			throws IllegalArgumentException {
		if (!spriteTemplates.containsKey(spriteTemplateName)) {
			// TODO - custom exception?
			throw new IllegalArgumentException();
		}
		Map<String, String> outdatedTemplateProperties = spriteTemplates.get(spriteTemplateName);
		for (String propertyToUpdate : propertiesToUpdate.keySet()) {
			outdatedTemplateProperties.put(propertyToUpdate, propertiesToUpdate.get(propertyToUpdate));
		}
	}

	/**
	 * Delete a previously defined template
	 * 
	 * @param spriteTemplateName
	 *            the name of the template to delete
	 * @throws IllegalArgumentException
	 *             if the template does not already exist
	 */
	public void deleteElementDefinition(String spriteTemplateName) throws IllegalArgumentException {
		if (!spriteTemplates.containsKey(spriteTemplateName)) {
			// TODO - custom exception?
			throw new IllegalArgumentException();
		}
		spriteTemplates.remove(spriteTemplateName);
	}

	/**
	 * Generate a sprite from an existing template which specifies its properties.
	 *
	 * @param spriteTemplateName
	 *            - the name of the sprite template
	 * @return a sprite object with properties set to those specified in the
	 *         template
	 */
	public Sprite generateSprite(String spriteTemplateName) {
		Map<String, String> properties = spriteTemplates.getOrDefault(spriteTemplateName, new HashMap<>());
		Sprite sprite = new Sprite(properties);
		return sprite;
	}

	public Map<String, String> getTemplateProperties(String spriteTemplateName) throws IllegalArgumentException {
		if (!spriteTemplates.containsKey(spriteTemplateName)) {
			// TODO - custom exception?
			throw new IllegalArgumentException();
		}
		return spriteTemplates.get(spriteTemplateName);
	}

	/**
	 * Export all the stored sprite templates for an authored game to properties
	 * files.
	 */
	public void exportSpriteTemplates() {
		for (String templateName : spriteTemplates.keySet()) {
			Properties templateProperties = new Properties();
			Map<String, String> templatePropertiesMap = spriteTemplates.get(templateName);
			templatePropertiesMap.forEach(templateProperties::setProperty);
			File exportFile = new File(TEMPLATE_FILE_OUTPUT_PATH + templateName + PROPERTIES_EXTENSION);
			writeTemplateToFile(templateProperties, exportFile);
		}
	}

	private void writeTemplateToFile(Properties templateProperties, File exportFile) {
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(exportFile);
			templateProperties.store(fileOut, PROPERTIES_COMMENT);
		} catch (IOException e) {
			// TODO - throw custom exception
		} finally {
			if (fileOut != null) {
				try {
					fileOut.close();
				} catch (IOException e) {
					// TODO - throw custom exception
				}
			}
		}
	}
}
