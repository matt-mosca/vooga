package engine.game_elements;

import engine.behavior.ElementProperty;
import javafx.geometry.Point2D;
import util.ElementOptionsGetter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generates spite objects for displaying during authoring and gameplay.
 * <p>
 * TODO - change the way the aux map is handled (use the param annotation) ?
 *
 * @author Ben Schwennesen
 */
public final class GameElementFactory {

    private Map<String, Map<String, String>> spriteTemplates = new HashMap<>();

    private ElementOptionsGetter elementOptionsGetter = new ElementOptionsGetter();

    /**
     * Define a new template with specified properties. The template should not use
     * an identical name as an existing template; updating a template is achieved
     * with updateElementDefinition().
     *
     * @param spriteTemplateName the name of the sprite template
     * @param properties         a map of properties for sprites using this template
     * @throws IllegalArgumentException if the template already exists
     */
    public void defineElement(String spriteTemplateName, Map<String, String> properties)
            throws IllegalArgumentException {
        if (spriteTemplates.containsKey(spriteTemplateName)) {
            // TODO - custom exception?
            throw new IllegalArgumentException();
        }
        spriteTemplates.put(spriteTemplateName, properties);
        List<Map<String, String>> templateUpgrades = new ArrayList<>();
        templateUpgrades.add(properties);
    }


    /**
     * Generate a sprite from an existing template which specifies its properties.
     *
     * @param spriteTemplateName the name of the sprite template
     * @param startCoordinates
     * @return a sprite object with properties set to those specified in the
     * template
     */
    public GameElement generateSprite(String spriteTemplateName, Point2D startCoordinates) {
        return generateSprite(spriteTemplateName, startCoordinates, new HashMap<>());
    }

    /**
     * Generate a sprite from an existing template which specifies its properties.
     *
     * @param spriteTemplateName the name of the sprite template
     * @param startCoordinates
     * @param auxiliaryObjects   map of optional objects needed for certain types of elements
     * @return a sprite object with properties set to those specified in the
     * template
     */
    public GameElement generateSprite(String spriteTemplateName, Point2D startCoordinates, Map<String, ?> auxiliaryObjects) {
        Map<String, String> properties = spriteTemplates.getOrDefault(spriteTemplateName, new HashMap<>());
        GameElement gameElement = generateSprite(properties, auxiliaryObjects);
        gameElement.setX(startCoordinates.getX());
        gameElement.setY(startCoordinates.getY());
        return gameElement;
    }

    // generate a sprite based on a map of string properties and auxiliary elements which are not part of a template
    GameElement generateSprite(Map<String, String> spriteProperties, Map<String, ?> auxiliaryObjects) {
        Parameter[] spriteConstructionParameters = getSpriteParameters();
        // TODO - check that params are returned in the right order
        Object[] spriteConstructionArguments = new Object[spriteConstructionParameters.length];
        for (int i = 0; i < spriteConstructionArguments.length; i++) {
            Parameter parameter = spriteConstructionParameters[i];
            try {
                spriteConstructionArguments[i] = generateSpriteParameter(parameter.getType(), spriteProperties, auxiliaryObjects);
            } catch (ReflectiveOperationException reflectionException) {
                // TODO - throw custom exception or fallback to a default
                reflectionException.printStackTrace();
            }
        }
        try {
            return (GameElement) GameElement.class.getConstructors()[0].newInstance(spriteConstructionArguments);
        } catch (ReflectiveOperationException reflectionException) {
            // TODO - custom exception or default
            reflectionException.printStackTrace();
            return null;
        }
    }

    private Parameter[] getSpriteParameters() {
        return GameElement.class.getConstructors()[0].getParameters();
    }

    private Object generateSpriteParameter(Class parameterClass, Map<String, String> properties,
                                           Map<String, ?> auxiliaryObjects) throws ReflectiveOperationException {
        try {
            String chosenSubclassName = elementOptionsGetter.getChosenSubclassName(parameterClass, properties);
            Class chosenParameterSubclass = Class.forName(chosenSubclassName);
            List<String> constructorParameterIdentifiers = elementOptionsGetter
                    .getConstructorParameterIdentifiers(chosenParameterSubclass);
            Object[] constructorParameters = getParameterConstructorArguments(properties, auxiliaryObjects,
                    constructorParameterIdentifiers);
            System.out.println(parameterClass.getName());
            return chosenParameterSubclass.getConstructors()[0].newInstance(constructorParameters);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Case where constructor has the main objects encapsulated (i.e.,
            // MovementHandler and CollisionHandler)
            // or where constructor has aux parameter encapsulated (but not bottom level
            // behavior object)
            Constructor[] parameterClassConstructors = parameterClass.getConstructors();
            if (parameterClassConstructors.length > 0) {
                Parameter[] parameters = parameterClassConstructors[0].getParameters();
                Object[] constructorParameters = new Object[parameters.length];
                for (int i = 0; i < parameters.length; i++) {
                    ElementProperty parameterNameAnnotation = parameters[i].getAnnotation(ElementProperty.class);
                    if (parameterNameAnnotation != null) {
                        if (parameterNameAnnotation.isTemplateProperty()) {
                            constructorParameters[i] = setConstructorParameter(
                                    properties.get(parameterNameAnnotation.value()));
                        } else {
                            constructorParameters[i] = generateSpriteParameter(parameters[i].getType(), properties,
                                    auxiliaryObjects);
                        }
                    } else {
                        System.out.println("\n\n\nTHIS DEFINITELY SHOULD NOT HAPPEN\n\n\n");
                    }
                }
                return parameterClass.getConstructors()[0].newInstance(constructorParameters);
            } else {
                return null;
            }
        }
    }

    private Object[] getParameterConstructorArguments(Map<String, String> properties, Map<String, ?> auxiliaryObjects,
                                                      List<String> constructorParameterIdentifiers) throws ReflectiveOperationException {
        Object[] constructorParameters = new Object[constructorParameterIdentifiers.size()];
        for (int i = 0; i < constructorParameters.length; i++) {
            String parameterIdentifier = constructorParameterIdentifiers.get(i);
            String parameterDescription = elementOptionsGetter.translateParameterToDescription(parameterIdentifier);
            if (!properties.containsKey(parameterDescription)) {
                constructorParameters[i] = auxiliaryObjects.get(parameterIdentifier);
                // TODO - throw exception if aux objects doesn't contain key
            } else {
                String propertyValueAsString = properties.get(parameterDescription);
                constructorParameters[i] = setConstructorParameter(propertyValueAsString);
            }
        }
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
            } catch (NullPointerException nullptr) {
                return null;
            }
        }
    }

    /**
     * Obtain the base configuration options for sprites; specifically, obtain
     * descriptive names for the subclass options for the sprite's construction
     * parameters.
     *
     * @return a map from the (pretty) name of configuration parameter to its value
     * options
     */
    public Map<String, List<String>> getElementBaseConfigurationOptions() {
        return elementOptionsGetter.getSpriteParameterSubclassOptions();
    }

    /**
     * Get auxiliary configuration elements for a game element, based on top-level
     * configuration choices.
     *
     * @return a map from the (pretty) name of the configuration parameter to its
     * class type
     */
    public Map<String, Class> getAuxiliaryElementProperties(Map<String, String> subclassChoices) {
        return elementOptionsGetter.getAuxiliaryParametersFromSubclassChoices(subclassChoices);
    }

    /**
     * Update an existing template by overwriting the specified properties to their
     * new specified values. Should not be used to create a new template, the
     * defineElement method should be used for that.
     *
     * @param spriteTemplateName - the name of the sprite template to update
     * @param propertiesToUpdate - map of property keys to update and their new values
     * @throws IllegalArgumentException if the template does not already exist
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
     * @param spriteTemplateName the name of the template to delete
     * @throws IllegalArgumentException if the template does not already exist
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
     * Return a copy of current templates (for data protection).
     *
     * @return map of template names to their properties
     */
    public Map<String, Map<String, String>> getAllDefinedTemplateProperties() {
        return new HashMap<>(spriteTemplates);
    }

    /**
     * Load the authored templates for a game already authored.
     *
     * @param loadedTemplates the previously refined templates loaded in from memory
     */
    public void loadSpriteTemplates(Map<String, Map<String, String>> loadedTemplates) {
        spriteTemplates.putAll(loadedTemplates);
    }
}
