package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 * Exports the game elements defined in a game to properties files.
 *
 * @author Ben Schwennesen
 */
public class SpriteTemplateIoHandler {

    private final String PROPERTIES_COMMENT = "Programmatically generated sprite template file";
    private final String TEMPLATE_FILE_OUTPUT_PATH = "data/sprite-templates/";
    private final String PROPERTIES_EXTENSION = ".properties";
    private final String UPGRADE_INDICATOR = "_upgrade_";
    private final String DOT = ".";

    /**
     * Export all the stored sprite templates for an authored game to properties files.
     *
     * @param gameName        the name of the authored game
     * @param spriteTemplates the sprite templates defined in the game
     */
    public void exportSpriteTemplates(String gameName, Map<String, Map<String, String>> spriteTemplates) {
        String directoryPath = createDirectoryPath(gameName);
        createDirectoryIfNonExistent(directoryPath);
        for (String templateName : spriteTemplates.keySet()) {
            Properties templateProperties = new Properties();
            Map<String, String> templatePropertiesMap = spriteTemplates.get(templateName);
            System.out.println(templatePropertiesMap);
            templatePropertiesMap.forEach(templateProperties::setProperty);
            String fileName = templateName + PROPERTIES_EXTENSION;
            File exportFile = new File(directoryPath + File.separator + fileName);
            writeTemplateToFile(templateProperties, exportFile);
        }
    }

    /**
     * Export game element upgrade properties for an authored game.
     *
     * @param gameName       the name of the authored game
     * @param spriteUpgrades the sprite upgrades defined in the game, associated with sprite templates
     */
    public void exportSpriteUpgrades(String gameName, Map<String, List<Map<String, String>>> spriteUpgrades) {
        Map<String, Map<String, String>> upgradeTemplates = new HashMap<>();
        for (String templateName : spriteUpgrades.keySet()) {
            List<Map<String, String>> upgradesForTemplate = spriteUpgrades.get(templateName);
            for (int upgradeLevel = 0; upgradeLevel < upgradesForTemplate.size(); upgradeLevel++) {
                String upgradeFileName = templateName + UPGRADE_INDICATOR + upgradeLevel;
                upgradeTemplates.put(upgradeFileName, upgradesForTemplate.get(upgradeLevel));
            }
        }
        exportSpriteTemplates(gameName, upgradeTemplates);
    }

    /**
     * Import all the stored sprite templates for an authored game.
     *
     * @param gameName the name of the authored game
     * @return the sprite templates defined in the game in a map
     * @throws IOException if data files for the game are corrupted
     */
    public Map<String, Map<String, String>> loadSpriteTemplates(String gameName) throws IOException {
        Map<String, Map<String, String>> spriteTemplates = loadSpriteProperties(gameName, false);
        return spriteTemplates;
    }


    /**
     * Import all the stores sprite upgrades for all sprite templates for an authored game.
     *
     * @param gameName the name of the authored game
     * @return the upgrades defined for each sprite template defined in the game, in a map
     * @throws IOException if data files for the game are corrupted
     */
    public Map<String, List<Map<String, String>>> loadSpriteUpgrades(String gameName) throws IOException {
        Map<String, Map<String, String>> spriteTemplates = new TreeMap<>(loadSpriteProperties(gameName, true));
        Map<String, List<Map<String, String>>> spriteUpgrades = new HashMap<>();
        for (String upgradeTemplateName : spriteTemplates.keySet()) {
            String templateName = upgradeTemplateName.substring(0, upgradeTemplateName.indexOf(UPGRADE_INDICATOR));
            List<Map<String, String>> templateUpgrades = spriteUpgrades.getOrDefault(templateName, new ArrayList<>());
            // TreeMap ensures correct ordering
            templateUpgrades.add(spriteTemplates.get(upgradeTemplateName));
        }
        return spriteUpgrades;
    }


    private Map<String, Map<String, String>> loadSpriteProperties(String gameName, boolean loadUpgrades)
            throws IOException {
        Map<String, Map<String, String>> spriteTemplates = new TreeMap<>();
        String directoryPath =  createDirectoryPath(gameName);
        File propertiesDirectory = new File(directoryPath);
        File[] spritePropertiesFiles = propertiesDirectory.listFiles();
        if (spritePropertiesFiles != null) {
            for (File spritePropertiesFile : spritePropertiesFiles) {
                if (spritePropertiesFile.getPath().endsWith(PROPERTIES_EXTENSION)
                        && loadUpgrades == spritePropertiesFile.getPath().contains(UPGRADE_INDICATOR)) {
                    loadTemplateProperties(spriteTemplates, spritePropertiesFile);
                }
            }
        }
        return spriteTemplates;
    }


    private String createDirectoryPath(String gameName) {
        if (gameName.contains(DOT)) {
            return TEMPLATE_FILE_OUTPUT_PATH + gameName.substring(0, gameName.indexOf(DOT)) + File.separator;
        } else {
            return  TEMPLATE_FILE_OUTPUT_PATH + gameName + File.separator;
        }
    }

    private void loadTemplateProperties(Map<String, Map<String, String>> spriteTemplates, File spritePropertiesFile)
            throws IOException {
        Properties spriteProperties = new Properties();
        spriteProperties.load(new FileInputStream(spritePropertiesFile));
        Map<String, String> spritePropertiesMap = new HashMap<>();
        spriteProperties.stringPropertyNames().forEach(propertyName ->
                spritePropertiesMap.put(propertyName, spriteProperties.getProperty(propertyName)));
        String templateName = spritePropertiesFile.getName().replace(PROPERTIES_EXTENSION, "");
        spriteTemplates.put(templateName, spritePropertiesMap);
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
