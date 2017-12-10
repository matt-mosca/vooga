package util;

import engine.behavior.ElementProperty;
import engine.game_elements.GameElement;

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
public class ElementOptionsGetter {

    private final String PROPERTIES_EXTENSION = ".properties";
    private final String PARAMETER_TRANSLATIONS_FILE_NAME = "ParameterTranslations" + PROPERTIES_EXTENSION;
    private final String SPRITE_BASE_PARAMETER_NAME = GameElement.class.getName();
    private Properties parameterTranslationProperties;

    private Map<String, List<String>> spriteParameterSubclassOptions = new HashMap<>();

    private Map<String, String> classToDescription = new HashMap<>();
    private Map<String, String> descriptionToClass = new HashMap<>();
    //           spritep    param   descrip
    private Map<String, Map<String, Class>> spriteMemberParametersMap = new HashMap<>();
    private Map<String, String> parameterToDescription = new HashMap<>();
    private Map<String, String> descriptionToParameter = new HashMap<>();

    public ElementOptionsGetter() {
        loadTranslations();
    }

    private void loadTranslations() {
        initializeParameterTranslations();
        for (Parameter spriteParameter : GameElement.class.getConstructors()[0].getParameters()) {
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
                Map<String, Class> parameterDescriptions = spriteMemberParametersMap.getOrDefault
                        (subclassOptionName, new HashMap<>());
                loadTranslationsForSpriteParameter(subclassOptionName, parameterDescriptions);
                spriteMemberParametersMap.put(subclassOptionName, parameterDescriptions);
            }
            spriteParameterSubclassOptions.put(referenceClassDescription != null ?
                    referenceClassDescription : parameterClassFullName, subclassOptions);
        } else {
            // DIDNT FIND PROP FILE --> recur on parameter's parameters
            Class parameterClass = spriteParameter.getType();
            Map<String, Class> baseSpriteParameterMap = spriteMemberParametersMap.getOrDefault
                    (SPRITE_BASE_PARAMETER_NAME, new HashMap<>());
            if (spriteParameter.getAnnotation(ElementProperty.class) != null) {
                // property common to all sprites !!!! eg imageURL
                String parameterName = spriteParameter.getAnnotation(ElementProperty.class).value();
                baseSpriteParameterMap.put(parameterName, spriteParameter.getType());
                spriteMemberParametersMap.put(SPRITE_BASE_PARAMETER_NAME, baseSpriteParameterMap);
            }
            if (parameterClass.getConstructors().length > 0) {
                for (Parameter subparameter : parameterClass.getConstructors()[0].getParameters()){
                    loadTranslationsForSpriteParameter(subparameter);
                }
            }
        }
    }

    // TODO - refactor
    private void loadTranslationsForSpriteParameter(String spriteParameterSubclassName, Map<String, Class>
            parameterDescriptionsToClasses) throws ReflectiveOperationException {
        Class spriteParameterSubclass = Class.forName(spriteParameterSubclassName);
        Constructor[] subclassConstructors = spriteParameterSubclass.getConstructors();
        if (subclassConstructors.length > 0) {
            Constructor desiredConstructor = subclassConstructors[0];
            Parameter[] constructorParameters = desiredConstructor.getParameters();
            for (Parameter constructorParameter : constructorParameters) {
                processElementParameter(parameterDescriptionsToClasses, constructorParameter);
            }
        }
    }

    private void processElementParameter(Map<String, Class> parameterDescriptionsToClasses, Parameter constructorParameter) {
        ElementProperty elementPropertyAnnotation = constructorParameter.getAnnotation(ElementProperty.class);
        if (elementPropertyAnnotation != null && elementPropertyAnnotation.isTemplateProperty()) {
            // property that needs to be set in the frontend
            addTemplatePropertyTranslation(parameterDescriptionsToClasses, constructorParameter, elementPropertyAnnotation);
        } else {
            // property that we need to supply ourselves so don't pass it to them
            String parameterTypeSimple = constructorParameter.getType().getSimpleName();
            parameterToDescription.put(parameterTypeSimple, parameterTypeSimple);
            descriptionToParameter.put(parameterTypeSimple, parameterTypeSimple);
            parameterDescriptionsToClasses.put(parameterTypeSimple, constructorParameter.getType());
        }
    }

    private void addTemplatePropertyTranslation(Map<String, Class> parameterDescriptionsToClasses, Parameter constructorParameter, ElementProperty elementPropertyAnnotation) {
        String parameterName = elementPropertyAnnotation.value();
        String parameterDescription = parameterTranslationProperties.getProperty(parameterName);
        if (parameterDescription != null) {
            parameterToDescription.put(parameterName, parameterDescription);
            descriptionToParameter.put(parameterDescription, parameterName);
            // TODO - eliminate above?
            parameterDescriptionsToClasses.put(parameterDescription, constructorParameter.getType());
        } else {
            parameterToDescription.put(parameterName, parameterName);
            descriptionToParameter.put(parameterName, parameterName);
            parameterDescriptionsToClasses.put(parameterName, constructorParameter.getType());
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
        if (parameter.isAnnotationPresent(ElementProperty.class)) {
            return parameter.getAnnotation(ElementProperty.class).value();
        } else {
            return parameter.getType().getName();
        }
    }

    public Map<String, List<String>> getSpriteParameterSubclassOptions() {
        return spriteParameterSubclassOptions;
    }

    public Map<String, Class> getAuxiliaryParametersFromSubclassChoices(Map<String, String> subclassChoices)
            throws IllegalArgumentException {
        Map<String, Class> auxiliaryParameters = new HashMap<>();
        for (String subclassChoiceDescription : subclassChoices.values()) {
            String subclassChoiceName = descriptionToClass.get(subclassChoiceDescription);
            if (subclassChoiceName == null || !spriteMemberParametersMap.containsKey(subclassChoiceName)) {
                throw new IllegalArgumentException();
            }
            auxiliaryParameters.putAll(spriteMemberParametersMap.get(subclassChoiceName));
        }
        System.out.println(auxiliaryParameters);
        auxiliaryParameters.putAll(spriteMemberParametersMap.getOrDefault(SPRITE_BASE_PARAMETER_NAME, new HashMap<>()));
        return auxiliaryParameters;
    }

    private String translateDescriptionToClass(String description) {
        return descriptionToClass.get(description);
    }

    private String translateClassToDescription(String className) {
        return classToDescription.get(className);
    }

    public String translateParameterToDescription(String parameterName) {
        return parameterToDescription.get(parameterName);
    }

    public String getChosenSubclassName(Class parameterClass, Map<String, String> properties)
            throws IllegalArgumentException {
        String parameterClassDescription = translateClassToDescription(parameterClass.getName());
        String chosenSubclassDescription;
        if (parameterClassDescription == null ||
                (chosenSubclassDescription = properties.get(parameterClassDescription)) == null) {
            throw new IllegalArgumentException();
            // TODO - custom exception
        }
        return translateDescriptionToClass(chosenSubclassDescription);
    }
}
