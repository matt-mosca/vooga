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

    private final String TEMPLATE_FILE_OUTPUT_PATH = "data/sprite-properties/";
    private final String PROPERTIES_EXTENSION = ".properties";
    private final String PROPERTIES_COMMENT = "Programmatically generated sprite template file";

    // this might change - still don't see why the sprite needs to know its template name
    private final Class[] SPRITE_CONSTRUCTOR_ARGUMENT_CLASSES = {String.class};

    private Map<String, Map<String, String>> spriteTemplates = new HashMap<>();

    /**
     * Generate a sprite from a new/updated template which specifies its properties.
     *
     * @param spriteClassName - the subclass of Sprite to generate
     *                        TODO - can this just be a Class object?
     * @param spriteTemplateName - the name of the sprite template
     * @param properties - a map of properties for sprites using this template
     * @return a sprite object with properties set to those specified in the template
     * @throws ReflectiveOperationException - in the case that the spriteClassName is invalid
     */
    public Sprite generateSprite(String spriteClassName, String spriteTemplateName, Map<String, String> properties)
            throws ReflectiveOperationException {
        spriteTemplates.put(spriteTemplateName, properties);
        return generateSprite(spriteClassName, spriteTemplateName);
    }

    /**
     * Generate a sprite from an existing template which specifies its properties.
     *
     * @param spriteClassName - the subclass of Sprite to generate
     * @param spriteTemplateName - the name of the sprite template
     * @return a sprite object with properties set to those specified in the template
     * @throws ReflectiveOperationException - in the case that the spriteClassName is invalid
     */
    public Sprite generateSprite(String spriteClassName, String spriteTemplateName) throws
            ReflectiveOperationException {
        Class spriteClass = Class.forName(spriteClassName);
        Sprite sprite = (Sprite) spriteClass.getConstructor(SPRITE_CONSTRUCTOR_ARGUMENT_CLASSES)
                .newInstance(spriteTemplateName);
        setSpriteProperties(sprite, spriteTemplateName);
        return sprite;
    }

    private void setSpriteProperties(Sprite sprite, String spriteTemplateName) {
        Map<String, String> properties = spriteTemplates.getOrDefault(spriteTemplateName, new HashMap<>());
        // TODO - set the properties
        // Maybe get all the setters reflexively, check for "set" + property (ignore case)
    }

    /*
     * Generate a sprite with default properties (that is, without using a template).
     *
     * @param spriteClassName - the subclass of Sprite to generate
     * @return a sprite object with properties set to those specified in the template
     * @throws ReflectiveOperationException - in the case that the spriteClassName is invalid
     *
    public Sprite generateSprite(String spriteClassName) throws ReflectiveOperationException  {
        Class spriteClass = Class.forName(spriteClassName);
        return (Sprite) spriteClass.getConstructor(SPRITE_CONSTRUCTOR_ARGUMENT_CLASSES)
                .newInstance(spriteClassName);
    }*/

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

    public static void main(String[] args) {
        SpriteFactory sf = new SpriteFactory();
        Map<String, String> map = new HashMap<>();
        map.put("health-points", "50.0");
        map.put("damage", "4.0");
        map.put("image", "http://veryrealurl.com/veryrealimage.png");
        try {
            sf.generateSprite("sprites.DummySprite", "Cool Tower", map);
        } catch (ReflectiveOperationException e) {
            System.out.println("reflection failure");
            e.printStackTrace();
        }
        sf.exportSpriteTemplates();
    }

}
