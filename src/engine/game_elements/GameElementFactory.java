package engine.game_elements;

import engine.behavior.ElementProperty;
import util.ElementOptionsGetter;
import util.io.SerializationUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.net.StandardSocketOptions;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generates spite objects for displaying during authoring and gameplay.
 *
 * @author Ben Schwennesen
 */
public final class GameElementFactory {

    private Map<String, Map<String, Object>> spriteTemplates = new HashMap<>();

    private ElementOptionsGetter elementOptionsGetter = new ElementOptionsGetter();
    private SerializationUtils serializationUtils;

    public GameElementFactory(SerializationUtils serializationUtils) {
        this.serializationUtils = serializationUtils;
    }

    /**
     * Define a new template with specified properties. The template should not use an identical name as an existing
     * template; updating a template is achieved with updateElementDefinition().
     *
     * @param spriteTemplateName the name of the sprite template
     * @param properties         a map of properties for sprites using this template
     * @throws IllegalArgumentException if the template already exists
     */
    public void defineElement(String spriteTemplateName, Map<String, Object> properties)
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
     * @param elementTemplateName  the name of the sprite template
     * @param nonTemplateArguments map of auxiliary, instance-specific objects needed for certain types of elements
     * @return a sprite object with properties set to those specified in the template
     * @throws ReflectiveOperationException if there are element arguments not specified in the template or the
     *                                      auxiliary arguments map
     */
    public GameElement generateElement(String elementTemplateName, Map<String, ?> nonTemplateArguments)
            throws ReflectiveOperationException {
        Map<String, Object> properties =
                new HashMap<>(spriteTemplates.getOrDefault(elementTemplateName, new HashMap<>()));
        properties.putAll(nonTemplateArguments);
        return generateElement(properties);
    }

    // generate a sprite based on a map of string properties and auxiliary elements which are not part of a template
    GameElement generateElement(Map<String, ?> properties)
            throws ReflectiveOperationException {
        Parameter[] spriteConstructionParameters = getSpriteParameters();
        // TODO - check that params are returned in the right order
        Object[] spriteConstructionArguments = new Object[spriteConstructionParameters.length];
        for (int i = 0; i < spriteConstructionArguments.length; i++) {
            Parameter parameter = spriteConstructionParameters[i];
            try {
                spriteConstructionArguments[i] = generateSpriteParameter(parameter.getType(), properties);
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

    private Object generateSpriteParameter(Class parameterClass, Map<String, ?> properties) throws
            ReflectiveOperationException {
        String chosenSubclassName = elementOptionsGetter.getChosenSubclassName(parameterClass, properties);
        if (chosenSubclassName != null) {
            return generateBehaviorObject(properties, chosenSubclassName);
        } else {
            // Case where constructor has the main objects encapsulated (i.e., MovementHandler and CollisionHandler)
            // or where constructor has aux parameter encapsulated (but not bottom level behavior object)
            return generateBehaviorWrapper(parameterClass, properties);
        }
    }

    private Object generateBehaviorObject(Map<String, ?> properties, String chosenSubclassName)
            throws ReflectiveOperationException {
        Class chosenParameterSubclass = Class.forName(chosenSubclassName);
        Object[] constructorParameters = getConstructorArguments(properties, chosenParameterSubclass);
        System.out.println("--------------------------------");
        System.out.println(chosenSubclassName);
        System.out.println(chosenParameterSubclass);
        System.out.println(Arrays.asList(constructorParameters));
        System.out.print("[");
        for(int i=0;i<constructorParameters.length;i++) {
        	 System.out.print(constructorParameters[i].getClass());
        	 System.out.print(", ");
        } 
        System.out.println("]");
        System.out.println(chosenParameterSubclass.getConstructors()[0].getParameterCount());
        System.out.println(Arrays.asList(chosenParameterSubclass.getConstructors()[0].getParameterTypes()));
        return chosenParameterSubclass.getConstructors()[0].newInstance(constructorParameters);
    }

    private Object generateBehaviorWrapper(Class parameterClass, Map<String, ?> properties) throws
            ReflectiveOperationException {
        Constructor[] parameterClassConstructors = parameterClass.getConstructors();
        if (parameterClassConstructors.length > 0) {
            Parameter[] parameters = parameterClassConstructors[0].getParameters();
            Object[] constructorParameters = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                constructorParameters[i] = supplyConstructorArgument(properties, parameters[i]);
            }
            return parameterClass.getConstructors()[0].newInstance(constructorParameters);
        } else {
            // TODO - exception here
            return null;
        }
    }

    private Object supplyConstructorArgument(Map<String, ?> properties,
                                             Parameter parameter) throws ReflectiveOperationException {
        ElementProperty propertyParameterName = parameter.getAnnotation(ElementProperty.class);
        if (propertyParameterName != null) {
            //if (propertyParameterName.isTemplateProperty()) {
                // String propertyArgument = properties.get(propertyParameterName.value());
                return properties.get(propertyParameterName.value());
            /*} else {
                return nonTemplateArguments.get(propertyParameterName.value());
            }*/
        } else {
            // nested behavior object
            return generateSpriteParameter(parameter.getType(), properties);
        }
    }

    private Object[] getConstructorArguments(Map<String, ?> properties, Class chosenParameterSubclass)
            throws ReflectiveOperationException {
        Parameter[] constructorParameters = chosenParameterSubclass.getConstructors()[0].getParameters();
        List<String> parameterIdentifiers =
                elementOptionsGetter.getConstructorParameterIdentifiers(constructorParameters);
        Object[] constructorArguments = new Object[parameterIdentifiers.size()];
        for (int i = 0; i < constructorArguments.length; i++) {
            String parameterIdentifier = parameterIdentifiers.get(i);
            String parameterDescription = elementOptionsGetter.translateParameterToDescription(parameterIdentifier);
            if (parameterDescription == null) {
                constructorArguments[i] = properties.get(parameterIdentifier);
            } else {
                // TODO - this should change now that everything should be a part of the map
            /*if (!properties.containsKey(parameterDescription)) {
                constructorArguments[i] = nonTemplateArguments.get(parameterIdentifier);
                // TODO - throw exception if aux objects doesn't contain key
            } else {*/
                //String propertyValueAsString = properties.get(parameterDescription);
                //Class parameterClass = constructorParameters[i].getType();
                constructorArguments[i] = properties.get(parameterDescription);//setConstructorParameter
                // (propertyValueAsString,
                // parameterClass);
                //}
            }
        }
        return constructorArguments;
    }

    // TODO - make more elegant if possible
    private Object setConstructorParameter(String propertyValueAsString, Class propertyClass) {
        /*try {
            return Integer.parseInt(propertyValueAsString);
        } catch (NumberFormatException nonIntegerProperty) {
            try {
                return Double.parseDouble(propertyValueAsString);
            } catch (NumberFormatException | NullPointerException nonDoubleProperty) {
                return propertyValueAsString;
            }
        }*/
        return serializationUtils.deserializeElementProperty(propertyValueAsString, propertyClass);
    }

    // todo - refactor these out
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
     * @param spriteTemplateName the name of the sprite template to update
     * @param propertiesToUpdate map of property keys to update and their new values
     * @throws IllegalArgumentException if the template does not already exist
     */
    public void updateElementDefinition(String spriteTemplateName, Map<String, ?> propertiesToUpdate)
            throws IllegalArgumentException {
        if (!spriteTemplates.containsKey(spriteTemplateName)) {
            // TODO - custom exception?
            throw new IllegalArgumentException();
        }
        Map<String, Object> outdatedTemplateProperties = spriteTemplates.get(spriteTemplateName);
        for (String propertyToUpdate : propertiesToUpdate.keySet()) {
            outdatedTemplateProperties.put(propertyToUpdate, propertiesToUpdate.get(propertyToUpdate));
        }
        outdatedTemplateProperties.putAll(propertiesToUpdate);
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

    public Map<String, Object> getTemplateProperties(String spriteTemplateName) throws IllegalArgumentException {
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
    public Map<String, Map<String, Object>> getAllDefinedTemplateProperties() {
        return new HashMap<>(spriteTemplates);
    }

    /**
     * Load the authored templates for a game already authored.
     *
     * @param loadedTemplates the previously refined templates loaded in from memory
     */
    public void loadSpriteTemplates(Map<String, Map<String, Object>> loadedTemplates) {
        spriteTemplates.putAll(loadedTemplates);
    }
}
