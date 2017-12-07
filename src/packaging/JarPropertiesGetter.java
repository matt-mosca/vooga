package packaging;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

/**
 * Utility class for retrieving game export file's properties, such as the location of the source directory.
 *
 * @author Ben Schwennesen
 */
public final class JarPropertiesGetter {

    // Relocate the file as fits your needs
    private static final String PROPERTIES_FILE = "Export.properties";
    private static Properties properties;

    // Property keys
    private final String EXPORT_PATH_KEY = "export-path";
    private final String SOURCE_DIRECTORY_KEY = "source";
    private final String MAIN_CLASS_NAME_KEY = "main-class";
    private final String INCLUDED_DIRECTORIES_KEY = "included-directories";
    private final String RESOURCE_ROOTS_KEY = "resource-roots";
    private final String DATA_DIRECTORIES_KEY = "data-directories";

    // Set to your heart's desire
    private final String DEFAULT = "";

    private final String MULTIPLE_VALUES_DELIMITER = ",";

    private final String JAR_EXTENSION = ".jar";

    /**
     * Load in the properties file.
     */
    public JarPropertiesGetter() {
        properties = new Properties();
        InputStream propertiesStream = this.getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE);
        try {
            properties.load(propertiesStream);
        } catch (NullPointerException | IOException failure) {
            /* do nothing: if file fails to load, all methods are prepared to return
             * default/fallback value when getProperty() returns null */
        }
    }

    /**
     * Get the target path for a game's JAR export file.
     *
     * @param gameName the name given to the game
     * @return the path of the export JAR (relative to the project root)
     */
    public String getExportTargetPath(String gameName) {
        return properties.getProperty(EXPORT_PATH_KEY, DEFAULT) + gameName + JAR_EXTENSION;
    }

    /**
     * Get the source directory path.
     *
     * @return the relative path to the project's source folder
     */
    public String getSourceDirectoryPath() {
        return properties.getProperty(SOURCE_DIRECTORY_KEY, DEFAULT);
    }

    /**
     * Get the name of the launching class.
     *
     * @return the name of the class used to launch the exported JAR
     */
    public String getMainClassFullName() {
        return properties.getProperty(MAIN_CLASS_NAME_KEY, DEFAULT);
    }

    /**
     * Get source directories that should be included in the export JAR. For example, this might include only engine
     * and player modules. By default--that is, if the property is left unset--this will include all source directories.
     *
     * @return the specified directories within the source that should be included in the exported JAR file
     */
    public Collection<String> getSourceDirectoriesToInclude() {
        return Arrays.asList(properties.getProperty(INCLUDED_DIRECTORIES_KEY, getSourceDirectoryPath())
                .split(MULTIPLE_VALUES_DELIMITER));
    }

    /**
     * Get resource roots for the export. This includes any directory which has been marked as a resource root in
     * the development environment used to write the project. Files and directories within these roots will be
     * included in the root of the JAR, allowing calls to getClass().getResourceAsRoot() to function as expected.
     *
     * @return all specified resource roots for the project needed to run the exported JAR
     */
    public Collection<String> getResourceRoots() {
        return Arrays.asList(properties.getProperty(RESOURCE_ROOTS_KEY, DEFAULT).split(MULTIPLE_VALUES_DELIMITER));
    }

    /**
     * Get data and library directories for the export. This includes any directories directly referred to by their
     * full paths relative to the project root directory.
     *
     * @return all specified data directories needed to run the exported JAR
     */
    public Collection<String> getDataAndLibraryDirectories() {
        return Arrays.asList(properties.getProperty(DATA_DIRECTORIES_KEY, DEFAULT).split(MULTIPLE_VALUES_DELIMITER));
    }
}
