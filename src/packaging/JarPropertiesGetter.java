package packaging;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class JarPropertiesGetter {

    private static final String PROPERTIES_FILE = "Export.properties";
    private static final Properties PROPERTIES;

    /**
     * Blank, private constructor to ensure no other class tries to create an instance of this
     * utility class.
     */
    private JarPropertiesGetter() {
        // do nothing
    }

    /** static block to initialize the static java.util.Properties member */
    static {
        PROPERTIES = new Properties();
        InputStream properties = JarPropertiesGetter.class.getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE);
        try {
            PROPERTIES.load(properties);
        } catch (IOException failure) {
            /* do nothing: if file fails to load, all methods are prepared to return
             * default/fallback value when getProperty() returns null */
        }
    }



    public String getExportTargetPath(String gameName) {
        return null;
    }

    public String getSourceDirectoryPath() {
        return null;
    }

    public String[] getSourceDirectoriesToInclude() {
        return null;
    }

    public String getOutDirectoryPath() {
        return null;
    }

    public String[] getDataDirectories() {
        return null;
    }

    public String getMainClassFullName() {
        return null;
    }
}
