package sprites;

import engine.behavior.ParameterName;
import javafx.geometry.Bounds;
import util.SpriteOptionsGetter;

import javax.swing.text.html.ImageView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
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

    private SpriteOptionsGetter spriteOptionsGetter = new SpriteOptionsGetter();

	/**
	 * Define a new template with specified properties. The template should not use an identical name as an existing
	 * template; updating a template is achieved with updateElementDefinition().
	 *
	 * @param spriteTemplateName the name of the sprite template
	 * @param properties a map of properties for sprites using this template
	 * @throws IllegalArgumentException if the template already exists
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
     * Generate a sprite from an existing template which specifies its properties.
     *
     * @param spriteTemplateName - the name of the sprite template
     * @return a sprite object with properties set to those specified in the template
     */
    public Sprite generateSprite(String spriteTemplateName) {
        Map<String, String> properties = spriteTemplates.getOrDefault(spriteTemplateName, new HashMap<>());
		return generateSprite(properties);
    }

	/**
	 * Generate a sprite from an existing template which specifies its properties.
	 *
	 * @param spriteTemplateName - the name of the sprite template
	 * @return a sprite object with properties set to those specified in the template
	 */
	public Sprite generateSprite(String spriteTemplateName,
								 @ParameterName("startX") double startX, @ParameterName("startY") double startY,
								 @ParameterName("graphicalRepresentation") ImageView graphicalRepresentation) {
		Map<String, String> properties = spriteTemplates.getOrDefault(spriteTemplateName, new HashMap<>());
		return generateSprite(properties);
	}

	/**
	 * TODO
	 * @param spriteProperties
	 * @return
	 */
	public Sprite generateSprite(Map<String, String> spriteProperties) {
		Parameter[] spriteConstructionParameters = getSpriteParameters();
		// TODO - check that params are returned in the right order
		Object[] spriteConstructionArguments = new Object[spriteConstructionParameters.length];
		for (int i = 0; i < spriteConstructionArguments.length; i++) {
			Parameter parameter = spriteConstructionParameters[i];
			try {
				spriteConstructionArguments[i] = generateSpriteParameter(parameter.getType(), spriteProperties);
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
        String parmeterClassDescription = spriteOptionsGetter.translateClassToDescription(parameterClass.getName());
        String chosenSubclassDescription = properties.get(parmeterClassDescription);
        String chosenSubclassName = spriteOptionsGetter.translateDescriptionToClass(chosenSubclassDescription);
        // TODO - more elegant (this is due to imageUrl) -- encapsulate ImageView in CollisionHandler? 
        if (chosenSubclassName == null) {
            return "";
        }
        Class chosenParameterSubclass = Class.forName(chosenSubclassName);
        List<String> constructorParameterNames =
                spriteOptionsGetter.getConstructorParameterNames(chosenParameterSubclass);
        Object[] constructorParameters = new Object[constructorParameterNames.size()];
        for (int i = 0; i < constructorParameters.length; i++) {
            String parameterDescription = spriteOptionsGetter.translateParameterToDescription
                    (constructorParameterNames.get(i));
            String propertyValueAsString = properties.getOrDefault(parameterDescription, "0");
            // TODO - change default ^
			// TODO - more elegant
            try {
                constructorParameters[i] = Integer.parseInt(propertyValueAsString);
            } catch (NumberFormatException nonIntegerProperty) {
            	try {
					constructorParameters[i] = Double.parseDouble(propertyValueAsString);
				} catch (NumberFormatException nonDoubleProperty) {
					constructorParameters[i] = propertyValueAsString;
				}
            }
        }
        System.out.println(Arrays.asList(chosenParameterSubclass.getConstructors()[0].getParameters()));
        System.out.println(Arrays.stream(constructorParameters).map(cp -> cp.getClass().getName()).collect(Collectors
				.toList()));
        return chosenParameterSubclass.getConstructors()[0].newInstance(constructorParameters);
    }

    /**
     * Obtain the base configuration options for sprites; specifically, obtain descriptive names for the subclass
     * options for the sprite's construction parameters.
     *
     * @return a map from the (prettily translated) name of configuration parameter to its value options
     */
    public Map<String, List<String>> getElementBaseConfigurationOptions() {
        return spriteOptionsGetter.getSpriteParameterSubclassOptions();
    }

    /**
     *
     * @param subclassChoices
     * @return
     */
    public List<String> getAuxiliaryElementProperties(Map<String, String> subclassChoices) {
        return spriteOptionsGetter.getAuxiliaryParametersFromSubclassChoices(subclassChoices);
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
