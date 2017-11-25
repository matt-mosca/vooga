package sprites;

import com.sun.xml.internal.bind.v2.TODO;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import engine.behavior.ParameterName;
import engine.behavior.movement.MovementStrategy;
import engine.behavior.movement.RandomMovementStrategy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

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

    // used to map pretty names of properties (displayed in frontend) to backend, ugly names
    private Map<String, String> propertyTranslations = new HashMap<>();

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
        String[] constructorParameterNames = getConstructorParameterNames(parameterClass);
        Object[] constructorParameters = new Object[constructorParameterNames.length];
        for (int i = 0; i < constructorParameters.length; i++) {
            String propertyValueAsString = properties.getOrDefault(constructorParameterNames[i], "0");
            // TODO - change default
            try {
                constructorParameters[i] = Double.parseDouble(propertyValueAsString);
            } catch (NumberFormatException nonNumbericProperty) {
                constructorParameters[i] = propertyValueAsString;
            }
        }
        return parameterClass.getConstructors()[0].newInstance(constructorParameters);
    }

    private String[] getConstructorParameterNames(Class clazz) {
        Constructor constructor = clazz.getConstructors()[0];
        Parameter[] parameters = constructor.getParameters();
        String[] parameterNames = new String[parameters.length];
        // TODO - make sure getParameters() returns them in order
        for (int i = 0; i < parameterNames.length; i++) {
            parameterNames[i] = parameters[i].getAnnotation(ParameterName.class).value();
        }
        return parameterNames;
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
        Map<String, List<String>> spriteParameterSubclassOptions = new HashMap<>();
        for (Parameter spriteParameter : getSpriteParameters()) {
            try {
                String parameterClassName = spriteParameter.getType().getSimpleName();
                loadOptionsForParameter(parameterClassName, spriteParameterSubclassOptions);
            } catch (IOException e) {
                // TODO - handle
            }
        }
        System.out.println(propertyTranslations);
        return spriteParameterSubclassOptions;
    }

    private void loadOptionsForParameter(String parameterClassName, Map<String, List<String>> parameterSubclassOptions)
            throws IOException {
        Properties spriteConstructionProperties = new Properties();
        System.out.println(parameterClassName);
        InputStream parameterPossibilitiesProperties = getClass().getClassLoader()
                .getResourceAsStream(parameterClassName + PROPERTIES_EXTENSION);
        if (parameterPossibilitiesProperties != null) {
            spriteConstructionProperties.load(parameterPossibilitiesProperties);
            String parameterPrettyConfigurationName = (String) spriteConstructionProperties.remove(parameterClassName);
            spriteConstructionProperties.forEach((subclassOption, description) ->
                    propertyTranslations.put((String) description, (String) subclassOption));
            List<String> prettyOptions = spriteConstructionProperties.values().stream()
                    .map(option -> (String) option)
                    .collect(Collectors.toList());
            parameterSubclassOptions.put(parameterPrettyConfigurationName, prettyOptions);
        }
    }

    public Map<String, String> getAuxiliaryElementProperties(Map<String, String> baseConfiguration) {
        return null;
    }
}
