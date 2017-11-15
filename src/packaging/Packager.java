package packaging;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.regex.Pattern;

/**
 * Packages up a game for delivery as an executable JAR.
 *
 * @author Ben Schwennesen
 */
public class Packager {

    public static final int MAX_ENTRY_LENGTH = 1024;
    // no need for these in properties files since they will never change
    private final String WINDOWS_PATH_DELIMITER_PATTERN = Pattern.quote("\\");
    private final String SRC = "src/";
    private final String MAIN_CLASS_KEY = "Main-Class";
    private final String MANIFEST_VERSION = "1.0";
    private final String GAMES_ROOT = "games/";
    private final String JAR_EXTENSION = ".jar";

    // this will eventually be all game engine classes, among other things to include like resource files
    // for now, they're just a bunch of files for testing purposes
    private final String TESTING_PACKAGE = "networking";
    private final String[] FILES_TO_INCLUDE = {
            TESTING_PACKAGE + File.separator + "ChatClient.class",
            TESTING_PACKAGE + File.separator + "ChatThread.class",
            TESTING_PACKAGE + File.separator + "ChatTestWindow.class"
    };

    /**
     * Generate an executable JAR file for an authored game.
     *
     * Based on https://stackoverflow.com/questions/1281229/how-to-use-jaroutputstream-to-create-a-jar-file.
     *
     * @param gameName - the chosen name of the game
     * @param launchClass - the class used to launch the game (the player class)
     */
    public void generateJar(String gameName, Class launchClass) {
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, MANIFEST_VERSION);
        manifest.getMainAttributes().putValue(MAIN_CLASS_KEY, launchClass.getName());
        try {
            FileOutputStream outputStream = new FileOutputStream(GAMES_ROOT + gameName + JAR_EXTENSION);
            JarOutputStream target = new JarOutputStream(outputStream, manifest);
            for (String fileName : FILES_TO_INCLUDE) {
                addToJar(new File(fileName), target);
            }
            target.close();
        } catch (IOException e) {
            // TODO - report failure to build the game
        }
    }

    // This and its called methods are based on
    // https://stackoverflow.com/questions/1281229/how-to-use-jaroutputstream-to-create-a-jar-file
    private void addToJar(File source, JarOutputStream target) throws IOException {
        if (source.isDirectory() && !source.getName().isEmpty()) {
            addDirectoryToJar(source, target);
        } else {
            JarEntry entry = new JarEntry(convertPathToJarFormat(source.getPath()));
            entry.setTime(source.lastModified());
            target.putNextEntry(entry);
            writeToJar(source, target);
            target.closeEntry();
        }
    }

    private void addDirectoryToJar(File source, JarOutputStream target) throws IOException {
        String name = convertPathToJarFormat(source.getPath());
        if (!name.endsWith(File.separator)) {
            name += File.separator;
        }
        JarEntry entry = new JarEntry(name);
        entry.setTime(source.lastModified());
        target.putNextEntry(entry);
        target.closeEntry();
        File[] nestedFilesAndDirectories = source.listFiles();
        if (nestedFilesAndDirectories != null) {
            for (File nestedElement : nestedFilesAndDirectories) {
                addToJar(nestedElement, target);
            }
        }
    }

    private void writeToJar(File source, JarOutputStream target) throws IOException {
        InputStream in = new BufferedInputStream(getClass().getClassLoader().getResourceAsStream(source.getPath()));
        byte[] buffer = new byte[MAX_ENTRY_LENGTH];
        int count;
        while ((count = in.read(buffer)) != -1) {
            target.write(buffer, 0, count);
        }
        in.close();
    }

    private String convertPathToJarFormat(String path) {
        return path.replaceAll(WINDOWS_PATH_DELIMITER_PATTERN, File.separator);
    }
}
