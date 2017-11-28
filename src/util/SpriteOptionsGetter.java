package util;

import engine.behavior.ParameterName;
import sprites.Sprite;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Translates properties of sprites to and from friendly descriptions that are displayed in the frontend.
 *
 * @author Ben Schwennesen
 */
public class SpriteOptionsGetter {

    private final String PROPERTIES_EXTENSION = ".properties";
    private final String PARAMETER_TRANSLATIONS_FILE_NAME = "ParameterTranslations" + PROPERTIES_EXTENSION;
    private Properties parameterTranslationProperties;

    private Map<String, List<String>> spriteParameterSubclassOptions = new HashMap<>();

    private Map<String, String> classToDescription = new HashMap<>();
    private Map<String, String> descriptionToClass = new HashMap<>();
    //           spritep    param   descrip
    private Map<String, List<String>> spriteMemberParametersMap = new HashMap<>();
    private Map<String, String> parameterToDescription = new HashMap<>();
    private Map<String, String> descriptionToParameter = new HashMap<>();

    public SpriteOptionsGetter() {
        loadTranslations();
    }

    private void loadTranslations() {
        initializeParameterTranslations();
        Map<String, List<String>> spriteParameterSubclassOptions = new HashMap<>();
        for (Parameter spriteParameter : Sprite.class.getConstructors()[0].getParameters()) {
            try {
                loadTranslationsForSpriteParameter(spriteParameter);
            } catch (IOException | ReflectiveOperationException failedToLoadTranslationsException) {
                // TODO - handle
            }
        }
    }

    private void initializeParameterTranslations() {
        parameterTranslationProperties = new Properties();
        InputStream parameterTranslationStream = getClass().getClassLoader()
                .getResourceAsStream(PARAMETER_TRANSLATIONS_FILE_NAME);
        if (parameterTranslationStream != null) {
            try {
                parameterTranslationProperties.load(parameterTranslationStream);
            } catch (IOException fileDoesntExistException) {
                // TODO - handle
            }
        }
    }

    // TODO - refactor (strongly needed)
    private void loadTranslationsForSpriteParameter(Parameter spriteParameter) throws IOException,
            ReflectiveOperationException {
        Properties spriteParameterSubclassProperties = new Properties();
        String parameterClassSimpleName = spriteParameter.getType().getSimpleName();
        InputStream parameterClassPossibilitiesStream = getClass().getClassLoader()
                .getResourceAsStream(parameterClassSimpleName + PROPERTIES_EXTENSION);
        if (parameterClassPossibilitiesStream != null) {
            spriteParameterSubclassProperties.load(parameterClassPossibilitiesStream);
            String parameterClassFullName = spriteParameter.getType().getName();
            List<String> subclassOptions = new ArrayList<>();
            String referenceClassDescription = null;
            for (String subclassOptionName : spriteParameterSubclassProperties.stringPropertyNames()) {
                String subclassDescription = spriteParameterSubclassProperties.getProperty(subclassOptionName);
                classToDescription.put(subclassOptionName, subclassDescription);
                descriptionToClass.put(subclassDescription, subclassOptionName);
                if (!subclassOptionName.equals(parameterClassFullName)) {
                    subclassOptions.add(subclassDescription);
                } else {
                    referenceClassDescription = subclassDescription;
                }
                List<String> parameterDescriptions = spriteMemberParametersMap.getOrDefault
                        (subclassOptionName, new ArrayList<>());
                loadTranslationsForSpriteParameter(subclassOptionName, parameterDescriptions);
                spriteMemberParametersMap.put(subclassOptionName, parameterDescriptions);
            }
            spriteParameterSubclassOptions.put(referenceClassDescription != null ?
                    referenceClassDescription : parameterClassFullName, subclassOptions);
        } else {
            // DIDNT FIND PROP FILE --> recur on parameter's parameters
            Class parameterClass = spriteParameter.getType();
            if (parameterClass.getConstructors().length > 0) {
                for (Parameter subparameter : parameterClass.getConstructors()[0].getParameters()){
                    loadTranslationsForSpriteParameter(subparameter);
                }
            }
        }
    }

    // TODO - refactor
    private void loadTranslationsForSpriteParameter(String spriteParameterSubclassName, List<String>
            parameterDescriptions) throws ReflectiveOperationException {
        Class spriteParameterSubclass = Class.forName(spriteParameterSubclassName);
        Constructor[] subclassConstructors = spriteParameterSubclass.getConstructors();
        if (subclassConstructors.length > 0) {
            Constructor desiredConstructor = subclassConstructors[0];
            Parameter[] constructorParameters = desiredConstructor.getParameters();
            for (Parameter constructorParameter : constructorParameters) {
                String parameterName = constructorParameter.getAnnotation(ParameterName.class).value();
                String parameterDescription = parameterTranslationProperties.getProperty(parameterName);
                if (parameterDescription != null) {
                    parameterToDescription.put(parameterName, parameterDescription);
                    descriptionToParameter.put(parameterDescription, parameterName);
                    // TODO - eliminate above?
                    parameterDescriptions.add(parameterDescription);
                } else {
                    parameterToDescription.put(parameterName, parameterName);
                    descriptionToParameter.put(parameterName, parameterName);
                    parameterDescriptions.add(parameterName);
                }
            }
        }
    }

    public List<String> getConstructorParameterIdentifiers(Class clazz) {
        if (clazz.getConstructors().length == 0) {
            return new ArrayList<>();
        }
        Constructor constructor = clazz.getConstructors()[0];
        Parameter[] parameters = constructor.getParameters();
        // TODO - make sure getParameters() returns them in order
        return Arrays.stream(parameters).map(this::getParameterIdentifier).collect(Collectors.toList());
    }

    private String getParameterIdentifier(Parameter parameter) {
        if (parameter.isAnnotationPresent(ParameterName.class)) {
            return parameter.getAnnotation(ParameterName.class).value();
        } else {
            return parameter.getType().getName();
        }
    }

    public Map<String, List<String>> getSpriteParameterSubclassOptions() {
        return spriteParameterSubclassOptions;
    }

    public List<String> getAuxiliaryParametersFromSubclassChoices(Map<String, String> subclassChoices)
            throws IllegalArgumentException {
        List<String> auxiliaryParameters = new ArrayList<>();
        for (String subclassChoiceDescription : subclassChoices.values()) {
            System.out.println(subclassChoiceDescription);
            String subclassChoiceName = descriptionToClass.get(subclassChoiceDescription);
            if (subclassChoiceName == null || !spriteMemberParametersMap.containsKey(subclassChoiceName)) {
                throw new IllegalArgumentException();
            }
            auxiliaryParameters.addAll(spriteMemberParametersMap.get(subclassChoiceName));
        }
        return auxiliaryParameters;
    }

    public String translateDescriptionToClass(String description) {
        return descriptionToClass.get(description);
    }

    public String translateClassToDescription(String className) {
        return classToDescription.get(className);
    }

    public String translateParameterToDescription(String parameterName) {
        return parameterToDescription.get(parameterName);
    }

    public String getChosenSubclassName(Class parameterClass, Map<String, String> properties) throws IllegalArgumentException {
        String parameterClassDescription = translateClassToDescription(parameterClass.getName());
        String chosenSubclassDescription;
        if (parameterToDescription == null ||
                (chosenSubclassDescription = properties.get(parameterClassDescription)) == null) {
            throw new IllegalArgumentException();
            // TODO - custom exception
        }
        return translateDescriptionToClass(chosenSubclassDescription);
    }

    public static void main(String[] args) {
        SpriteOptionsGetter spriteTranslator = new SpriteOptionsGetter();
        System.out.println(spriteTranslator.classToDescription);
        System.out.println(spriteTranslator.parameterToDescription);
        System.out.println(spriteTranslator.spriteParameterSubclassOptions);
        System.out.println(spriteTranslator.spriteMemberParametersMap);
        Map<String, String> choices = new HashMap<>();
        choices.put("engine.behavior.movement.MovementStrategy", "Move in a straight line to a target location");
        choices.put("engine.behavior.collision.CollisionVisitor", "Invulnerable to collision damage");
        choices.put("engine.behavior.firing.FiringStrategy", "Do not fire projectiles");
        choices.put("engine.behavior.collision.CollisionVisitable", "Deal damage to colliding objects");
        System.out.println(spriteTranslator.getAuxiliaryParametersFromSubclassChoices(choices));
    }
}
