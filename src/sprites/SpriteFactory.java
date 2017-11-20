package sprites;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Generates spite objects for displaying during authoring and gameplay.
 *
 * @author Ben Schwennesen
 */
public class SpriteFactory {

    private final String PROPERTIES_COMMENT = "Programmatically generated sprite template file";
    private final String TEMPLATE_FILE_OUTPUT_PATH = "data/sprite-properties/";
    private final String PROPERTIES_EXTENSION = ".properties";

    private Map<String, Map<String, String>> spriteTemplates = new HashMap<>();

    /**
     * Define a new/updated template with specified properties.
     *
     * @param spriteTemplateName - the name of the sprite template
     * @param properties - a map of properties for sprites using this template
     */
    public void defineElement(String spriteTemplateName, Map<String, String> properties) {
        spriteTemplates.put(spriteTemplateName, properties);
    }

    /**
     * Generate a sprite from an existing template which specifies its properties.
     *
     * @param spriteTemplateName - the name of the sprite template
     * @return a sprite object with properties set to those specified in the template
     */
    public Sprite generateSprite(String spriteTemplateName) {
        Map<String, String> properties = spriteTemplates.getOrDefault(spriteTemplateName, new HashMap<>());
        Sprite sprite = new Sprite(properties);
        return sprite;
    }

    /**
     * Export all the stored sprite templates for an authored game to properties files.
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
