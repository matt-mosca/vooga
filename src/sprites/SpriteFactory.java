package sprites;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import util.SpriteOptionsGetter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Parameter;
import java.util.*;
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
     * @param spriteTemplateName the name of the sprite template
     * @return a sprite object with properties set to those specified in the template
     */
    public Sprite generateSprite(String spriteTemplateName) {
        Map<String, String> properties = spriteTemplates.getOrDefault(spriteTemplateName, new HashMap<>());
        return null;
        //TODO -- remove this method (always need coordinates and ImageView)
		//return generateSprite(properties);
    }

	/**
	 * Generate a sprite from an existing template which specifies its properties.
	 *
	 * @param spriteTemplateName the name of the sprite template
	 * @param startCoordinates
	 * @param graphicalRepresentation
	 * @param auxiliaryObjects map of optional objects needed for certain types of elements
	 * @return a sprite object with properties set to those specified in the template
	 */
	public Sprite generateSprite(String spriteTemplateName, Point2D startCoordinates,
								 ImageView graphicalRepresentation, Map<String, ?> auxiliaryObjects) {
		Map<String, String> properties = spriteTemplates.getOrDefault(spriteTemplateName, new HashMap<>());
		Sprite sprite = generateSprite(properties, auxiliaryObjects);
		sprite.setX(startCoordinates.getX());
		sprite.setY(startCoordinates.getY());
		sprite.setGraphicalRepresentation(graphicalRepresentation);
		return sprite;
	}

	/**
	 * TODO
	 * @param spriteProperties
	 * @return
	 */
	public Sprite generateSprite(Map<String, String> spriteProperties, Map<String, ?> auxiliaryObjects) {
		Parameter[] spriteConstructionParameters = getSpriteParameters();
		// TODO - check that params are returned in the right order
		Object[] spriteConstructionArguments = new Object[spriteConstructionParameters.length];
		for (int i = 0; i < spriteConstructionArguments.length; i++) {
			Parameter parameter = spriteConstructionParameters[i];
			try {
				spriteConstructionArguments[i] = generateSpriteParameter(parameter.getType(),
                        spriteProperties, auxiliaryObjects);
			} catch (ReflectiveOperationException reflectionException) {
				// TODO - throw custom exception or fallback to a default
                reflectionException.printStackTrace();
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

    private Object generateSpriteParameter(Class parameterClass, Map<String, String> properties,
										   Map<String, ?> auxiliaryObjects) throws ReflectiveOperationException {
	    try {
            String chosenSubclassName = spriteOptionsGetter.getChosenSubclassName(parameterClass, properties);
            System.out.println(chosenSubclassName);
            Class chosenParameterSubclass = Class.forName(chosenSubclassName);
            List<String> constructorParameterIdentifiers =
                    spriteOptionsGetter.getConstructorParameterIdentifiers(chosenParameterSubclass);
            Object[] constructorParameters = getParameterConstructorArguments(properties, auxiliaryObjects,
                    constructorParameterIdentifiers);
            return chosenParameterSubclass.getConstructors()[0].newInstance(constructorParameters);
        } catch (IllegalArgumentException illegalArgumentException) {
	        // Case where constructor has the main objects encapsulated (i.e., MovementHandler and CollisionHandler)
            System.out.println(parameterClass.getTypeName());
	        Parameter[] parameters = parameterClass.getConstructors()[0].getParameters();
	        Object[] constructorParameters = new Object[parameters.length];
	        for (int i = 0; i < parameters.length; i++) {
	            System.out.println("\t"+parameters[i].getType().getTypeName());
                constructorParameters[i] = generateSpriteParameter(parameters[i].getType(), properties, auxiliaryObjects);
            }
            System.out.println(parameterClass.getTypeName() + " " +
                    Arrays.stream(parameterClass.getConstructors()[0].getParameters())
                            .map(Parameter::getType).collect(Collectors.toList())
                    + " " + Arrays.asList(constructorParameters));
            return parameterClass.getConstructors()[0].newInstance(constructorParameters);
        }
    }

    private Object[] getParameterConstructorArguments(Map<String, String> properties, Map<String, ?> auxiliaryObjects,
                                                      List<String> constructorParameterIdentifiers)
            throws ReflectiveOperationException {
        Object[] constructorParameters = new Object[constructorParameterIdentifiers.size()];
        for (int i = 0; i < constructorParameters.length; i++) {
        	String parameterIdentifier = constructorParameterIdentifiers.get(i);
            String parameterDescription = spriteOptionsGetter.translateParameterToDescription(parameterIdentifier);
            if (!properties.containsKey(parameterDescription)) {
                constructorParameters[i] = auxiliaryObjects.get(parameterIdentifier);
                // TODO - throw exception if aux objects doesn't contain key
			} else {
				String propertyValueAsString = properties.get(parameterDescription);
				constructorParameters[i] = setConstructorParameter(propertyValueAsString);
			}
		}
        /*System.out.println(Arrays.asList(chosenParameterSubclass.getConstructors()[0].getParameters()));
        System.out.println(Arrays.stream(constructorParameters).map(cp -> cp.getClass().getName()).collect(Collectors
				.toList()));*/
        return constructorParameters;
    }

    // TODO - make more elegant if possible
	private Object setConstructorParameter(String propertyValueAsString) {
		try {
            return Integer.parseInt(propertyValueAsString);
        } catch (NumberFormatException nonIntegerProperty) {
            try {
                return Double.parseDouble(propertyValueAsString);
            } catch (NumberFormatException nonDoubleProperty) {
                return propertyValueAsString;
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
	 * Return a COPY of current templates for data protection
	 * @return map of template names to their properties
	 */
	public Map<String, Map<String, String>> getAllDefinedTemplateProperties() {
		return spriteTemplates;
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
