package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javafx.scene.paint.Color;

/**
 * Static utility class for retrieving information from properties files. All methods are static and fields are
 * initialized ina static block.
 *
 * @author Ben Schwennesen
 */
public final class PropertiesGetter {

    private static final String[] PROPERTIES_FILES = { /* fill pls */ };
    private static final Properties PROPERTIES;

    /**
     * Blank, private constructor to ensure no other class tries to create an instance of this
     * utility class.
     */
    private PropertiesGetter() {
        // do nothing
    }

    /** Use static block to initialize the static java.util.Properties member */
    static {
        PROPERTIES = new Properties();
        try {
            for (String propertiesFile : PROPERTIES_FILES) {
                InputStream propertiesStream = PropertiesGetter.class.getClassLoader()
                        .getResourceAsStream(propertiesFile);
                Properties properties = new Properties();
                properties.load(propertiesStream);
                PROPERTIES.putAll(properties);
            }
        } catch (IOException failure) {
            /* do nothing: if file fails to load, all methods are prepared to return
             * default/fallback value when getProperty() returns null */
        }
    }

    /**
     * Get a property that is know to be a string.
     *
     * @param key  the key used to index the desired configuration value
     * @return value  the string configuration value we want to get
     */
    private static String getProperty(String key) {
        return PROPERTIES.getProperty(key, "");
    }

    /**
     * Get a property that is know to be an integer.
     *
     * @param key the key used to index the desired configuration value
     * @return value the integer configuration value we want to get
     */
    private static int getIntegerProperty(String key) {
        String value = PROPERTIES.getProperty(key);
        // if the key is not found, Properties will return null and we should return a default value
        if (value == null) {
            return 0;
        }
        return Integer.parseInt(value);
    }

    /**
     * Get a property that is know to be a double.
     *
     * @param key  the key used to index the desired configuration value
     * @return value the double configuration value we want to get
     */
    private static double getDoubleProperty(String key) {
        String value = PROPERTIES.getProperty(key);
        // if the key is not found, Properties will return null and we should return a default value
        if (value == null) {
            return 0;
        }
        return Double.parseDouble(value);
    }
}