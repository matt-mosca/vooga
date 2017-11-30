package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Exports the game elements defined in a game to properties files.
 *
 * @author Ben Schwennesen
 */
public class SpriteTemplateIoHandler {

    private final String PROPERTIES_COMMENT = "Programmatically generated sprite template file";
    private final String TEMPLATE_FILE_OUTPUT_PATH = "data/sprite-templates/";
    private final String PROPERTIES_EXTENSION = ".properties";
    private final String DOT = ".";

    /**
     * Export all the stored sprite templates for an authored game to properties files.
     *
     * @param gameName the name of the authored game
     * @param spriteTemplates the sprite templates defined in the game
     */
    public void exportSpriteTemplates(String gameName, Map<String, Map<String, String>> spriteTemplates) {
        String directoryPath;
        if (gameName.contains(DOT)) {
            directoryPath = TEMPLATE_FILE_OUTPUT_PATH + gameName.substring(0, gameName.indexOf(DOT)) + File
                    .separator;
        } else {
            directoryPath =  TEMPLATE_FILE_OUTPUT_PATH + gameName + File.separator;
        }
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

    /**
     * Import all the stored sprite templates for an authored game.
     *
     * @param gameName the name of the authored game
     * @return the sprite templates defined in the game in a map
     */
    public Map<String, Map<String, String>> loadSpriteTemplates(String gameName) throws IOException {
        Map<String, Map<String, String>> spriteTemplates = new HashMap<>();
        String directoryPath;
        if (gameName.contains(DOT)) {
            directoryPath = TEMPLATE_FILE_OUTPUT_PATH + gameName.substring(0, gameName.indexOf(DOT)) + File
                    .separator;
        } else {
            directoryPath =  TEMPLATE_FILE_OUTPUT_PATH + gameName + File.separator;
        }        File propertiesDirectory = new File(directoryPath);
        File[] spritePropertiesFiles = propertiesDirectory.listFiles();
        if (spritePropertiesFiles != null) {
            for (File spritePropertiesFile : spritePropertiesFiles) {
                if (spritePropertiesFile.getPath().endsWith(PROPERTIES_EXTENSION)) {
                    Properties spriteProperties = new Properties();
                    spriteProperties.load(new FileInputStream(spritePropertiesFile));
                    Map<String, String> spritePropertiesMap = new HashMap<>();
                    spriteProperties.stringPropertyNames().forEach(propertyName ->
                            spritePropertiesMap.put(propertyName, spriteProperties.getProperty(propertyName)));
                    String templateName = spritePropertiesFile.getName().replace(PROPERTIES_EXTENSION, "");
                    spriteTemplates.put(templateName, spritePropertiesMap);
                }
            }
        }
        return spriteTemplates;
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
