package sprites;

import util.SpriteOptionsGetter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Parameter;
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
    private final String TEMPLATE_FILE_OUTPUT_PATH = "data/sprite-templates/";
    private final String PROPERTIES_EXTENSION = ".properties";

    private Map<String, Map<String, String>> spriteTemplates = new HashMap<>();

    private SpriteOptionsGetter spriteTranslator = new SpriteOptionsGetter();

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
        Parameter[] spriteConstructionParameters = getSpriteParameters();
        // TODO - check that params are returned in the right order
        Object[] spriteConstructionArguments = new Object[spriteConstructionParameters.length];
        for (int i = 0; i < spriteConstructionArguments.length; i++) {
            Parameter parameter = spriteConstructionParameters[i];
            try {
                spriteConstructionArguments[i] = generateSpriteParameter(parameter.getType(), properties);
            } catch (ReflectiveOperationException reflectionException) {
                // TODO - throw custom exception or fallback to a default
            }
        }
        try {
            return (Sprite) Sprite.class.getConstructors()[0].newInstance(spriteConstructionArguments);
        } catch (ReflectiveOperationException reflectionException) {
            // TODO - custom exception or default
            return null;
        }
    }

    private Parameter[] getSpriteParameters() {
        return Sprite.class.getConstructors()[0].getParameters();
    }

    private Object generateSpriteParameter(Class parameterClass, Map<String, String> properties) throws
            ReflectiveOperationException {
        List<String> constructorParameterNames = spriteTranslator.getConstructorParameterNames(parameterClass);
        Object[] constructorParameters = new Object[constructorParameterNames.size()];
        for (int i = 0; i < constructorParameters.length; i++) {
            String propertyValueAsString = properties.getOrDefault(constructorParameterNames.get(i), "0");
            // TODO - change default
            try {
                constructorParameters[i] = Double.parseDouble(propertyValueAsString);
            } catch (NumberFormatException nonNumbericProperty) {
                constructorParameters[i] = propertyValueAsString;
            }
        }
        return parameterClass.getConstructors()[0].newInstance(constructorParameters);
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

    /**
     * Obtain the base configuration options for sprites; specifically, obtain descriptive names for the subclass
     * options for the sprite's construction parameters.
     *
     * @return a map from the (prettily translated) name of configuration parameter to its value options
     */
    public Map<String, List<String>> getElementBaseConfigurationOptions() {
        return spriteTranslator.getSpriteParameterSubclassOptions();
    }

    public List<String> getAuxiliaryElementProperties(Map<String, String> baseConfiguration) {
        return null;
    }
}
