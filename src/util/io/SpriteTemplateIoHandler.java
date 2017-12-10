package util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Exports the game elements defined in a game to properties files.
 *
 * @author Ben Schwennesen
 */
public class SpriteTemplateIoHandler {

    private final String PROPERTIES_COMMENT = "Programmatically generated sprite template file";
    private final String TEMPLATE_FILE_OUTPUT_PATH = "authoring/sprite-templates/";
    private final String PROPERTIES_EXTENSION = ".properties";
    private final String UPGRADE_INDICATOR = "_upgrade_";
    private final char DOT = '.';

    private SerializationUtils serializationUtils;

    public SpriteTemplateIoHandler(SerializationUtils serializationUtils) {
        this.serializationUtils = serializationUtils;
    }

    /**
     * Export all the stored sprite templates for an authored game to properties files.
     *
     * @param gameName        the name of the authored game
     * @param spriteTemplates the sprite templates defined in the game
     */
    public void exportSpriteTemplates(String gameName, Map<String, Map<String, Object>> spriteTemplates) {
        String directoryPath = createDirectoryPath(gameName);
        createDirectoryIfNonExistent(directoryPath);
        for (String templateName : spriteTemplates.keySet()) {
            Properties templateProperties = new Properties();
            Map<String, ?> templatePropertiesMap = spriteTemplates.get(templateName);
            Map<String, String> serializedPropertiesMap =
                    serializationUtils.serializeElementTemplate(templatePropertiesMap);
            serializedPropertiesMap.forEach(templateProperties::setProperty);
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
    public void exportSpriteUpgrades(String gameName, Map<String, List<Map<String, Object>>> spriteUpgrades) {
        Map<String, Map<String, Object>> upgradeTemplates = new HashMap<>();
        for (String templateName : spriteUpgrades.keySet()) {
            List<Map<String, Object>> upgradesForTemplate = spriteUpgrades.get(templateName);
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
    public Map<String, Map<String, Object>> loadElementTemplates(String gameName) throws IOException {
        Map<String, Map<String, String>> spriteTemplatesSerializations = loadTemplateSerializations(gameName, false);
        Map<String, Map<String, Object>> spriteTemplates = new HashMap<>();
        for (String templateName : spriteTemplatesSerializations.keySet()) {
            Map<String, String> spriteTemplateSerialization = spriteTemplatesSerializations.get(templateName);
            Map<String, Object> spriteTemplate =
                    serializationUtils.deserializeElementTemplate(spriteTemplateSerialization);
            spriteTemplates.put(templateName, spriteTemplate);
        }
        return spriteTemplates;
    }


    /**
     * Import all the stores sprite upgrades for all sprite templates for an authored game.
     *
     * @param gameName the name of the authored game
     * @return the upgrades defined for each sprite template defined in the game, in a map
     * @throws IOException if data files for the game are corrupted
     */
    public Map<String, List<Map<String, Object>>> loadElementUpgrades(String gameName) throws IOException {
        Map<String, Map<String, String>> serializedTemplates = new TreeMap<>(loadTemplateSerializations(gameName, true));
        Map<String, List<Map<String, Object>>> spriteUpgrades = new HashMap<>();
        for (String upgradeTemplateName : serializedTemplates.keySet()) {
            String templateName = upgradeTemplateName.substring(0, upgradeTemplateName.indexOf(UPGRADE_INDICATOR));
            List<Map<String, Object>> templateUpgrades = spriteUpgrades.getOrDefault(templateName, new ArrayList<>());
            Map<String, Object> upgradeTemplate =
                    serializationUtils.deserializeElementTemplate(serializedTemplates.get(upgradeTemplateName));
            // TreeMap ensures correct ordering
            templateUpgrades.add(upgradeTemplate);
        }
        return spriteUpgrades;
    }


    private Map<String, Map<String, String>> loadTemplateSerializations(String gameName, boolean loadUpgrades)
            throws IOException {
        Map<String, Map<String, String>> spriteTemplates = new TreeMap<>();
        String directoryPath = createDirectoryPath(gameName);
        File propertiesDirectory = new File(directoryPath);
        File[] spritePropertiesFiles = propertiesDirectory.listFiles();
        if (spritePropertiesFiles != null) {
            for (File spritePropertiesFile : spritePropertiesFiles) {
                if (spritePropertiesFile.getPath().endsWith(PROPERTIES_EXTENSION)
                        && loadUpgrades == spritePropertiesFile.getPath().contains(UPGRADE_INDICATOR)) {
                    loadTemplateProperties(spriteTemplates, spritePropertiesFile.getName(),
                            new FileInputStream(spritePropertiesFile));
                }
            }
        } else {
            loadTemplatesInsideJar(loadUpgrades, spriteTemplates, directoryPath);
        }
        return spriteTemplates;
    }

    private void loadTemplatesInsideJar(boolean loadUpgrades, Map<String, Map<String, String>> spriteTemplates,
                                        String directoryPath) throws IOException {
        CodeSource src = getClass().getProtectionDomain().getCodeSource();
        if (src != null) {
            URL jar = src.getLocation();
            ZipFile zipFile = new ZipFile(jar.getPath());
            ZipInputStream zip = new ZipInputStream(jar.openStream());
            ZipEntry entry;
            while ((entry = zip.getNextEntry()) != null) {
                String name = entry.getName();
                if (name.startsWith(directoryPath) && name.endsWith(PROPERTIES_EXTENSION)
                        && loadUpgrades == name.contains(UPGRADE_INDICATOR)) {
                    loadTemplateProperties(spriteTemplates, name.substring(name.lastIndexOf(File.separator)+1),
                            zipFile.getInputStream(entry));
                }
            }
        }
    }


    private String createDirectoryPath(String gameName) {
        if (gameName.indexOf(DOT) != -1) {
            return TEMPLATE_FILE_OUTPUT_PATH + gameName.substring(0, gameName.indexOf(DOT)) + File.separator;
        } else {
            return TEMPLATE_FILE_OUTPUT_PATH + gameName + File.separator;
        }
    }

    private void loadTemplateProperties(Map<String, Map<String, String>> spriteTemplates, String fileName,
                                        InputStream spritePropertiesStream) throws IOException {
        Properties spriteProperties = new Properties();
        spriteProperties.load(spritePropertiesStream);
        Map<String, String> spritePropertiesMap = new HashMap<>();
        spriteProperties.stringPropertyNames().forEach(propertyName ->
                spritePropertiesMap.put(propertyName, spriteProperties.getProperty(propertyName)));
        String templateName = fileName.replace(PROPERTIES_EXTENSION, "");
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
