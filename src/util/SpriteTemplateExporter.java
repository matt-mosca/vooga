package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * Exports the game elements defined in a game to properties files.
 *
 * @author Ben Schwennesen
 */
public class SpriteTemplateExporter {

    private final String PROPERTIES_COMMENT = "Programmatically generated sprite template file";
    private final String TEMPLATE_FILE_OUTPUT_PATH = "data/sprite-templates/";
    private final String PROPERTIES_EXTENSION = ".properties";

    /**
     * Export all the stored sprite templates for an authored game to properties files.
     *
     * @param gameName the name of the authored game
     * @param spriteTemplates the sprite templates defined in the game
     */
    public void exportSpriteTemplates(String gameName, Map<String, Map<String, String>> spriteTemplates) {
        String directoryPath = TEMPLATE_FILE_OUTPUT_PATH + gameName + File.separator;
        createDirectoryIfNonExistent(directoryPath);
        for (String templateName : spriteTemplates.keySet()) {
            Properties templateProperties = new Properties();
            Map<String, String> templatePropertiesMap = spriteTemplates.get(templateName);
            templatePropertiesMap.forEach(templateProperties::setProperty);
            String fileName = templateName + PROPERTIES_EXTENSION;
            File exportFile = new File(directoryPath + File.separator + fileName);
            writeTemplateToFile(templateProperties, exportFile);
        }
    }

    private void createDirectoryIfNonExistent(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    private void writeTemplateToFile(Properties templateProperties, File exportFile) {
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(exportFile);
            templateProperties.store(fileOut, PROPERTIES_COMMENT);
        } catch (IOException e) {
            // TODO - throw custom exception
            e.printStackTrace();
        } finally {
            if (fileOut != null) {
                try {
                    fileOut.close();
                } catch (IOException e) {
                    // TODO - throw custom exception
                    e.printStackTrace();
                }
            }
        }
    }
}
