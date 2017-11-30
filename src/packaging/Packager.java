package packaging;

import main.Main;
import networking.ChatTestWindow;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.regex.Pattern;

/**
 * Packages up a game for delivery as an executable JAR.
 *
 * Has not yet been setup to launch everything, just a test app.
 *
 * Ideas: use compiler programmatically -> package compiled files + resources + other JARs (gson) -> delete compiled
 *      files from src
 *
 * @author Ben Schwennesen
 */
public class Packager {

    public static final int MAX_ENTRY_LENGTH = 1024;
    // no need for these in properties files since they will never change
    private final String WINDOWS_PATH_DELIMITER_PATTERN = Pattern.quote("\\");
    private final String MANIFEST_VERSION = "1.0";
    private final String GAMES_ROOT = "data/games/";
    private final String JAR_EXTENSION = ".jar";

    // this will eventually be all game engine classes, among other things to include like resource files
    // for now, they're just a bunch of files for testing purposes
    private final String TESTING_PACKAGE = "networking";
    private final String[] FILES_TO_INCLUDE = { "src", };//"images", "resources", "authoring", "data" };
           /* TESTING_PACKAGE + File.separator + "ChatClient.class",
            TESTING_PACKAGE + File.separator + "ChatThread.class",
            TESTING_PACKAGE + File.separator + "ChatTestWindow.class"
    };*/

    private final Class LAUNCH_CLASS = Main.class;

    private JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();

    /**
     * Generate an executable JAR file for an authored game.
     *
     * Based on https://stackoverflow.com/questions/1281229/how-to-use-jaroutputstream-to-create-a-jar-file.
     *
     * @param gameName - the chosen name of the game
     */
    public void generateJar(String gameName) {
        //compile(new File("src"));
        //JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        //compiler.run(null, null, null, new File("src/main/Main.java").getAbsolutePath());
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, MANIFEST_VERSION);
        manifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS, ChatTestWindow.class.getName());
        //manifest.getMainAttributes().put(Attributes.Name.CLASS_PATH, "netwo");
        try {
            FileOutputStream outputStream = new FileOutputStream(GAMES_ROOT + gameName + JAR_EXTENSION);
            JarOutputStream target = new JarOutputStream(outputStream, manifest);
            /*for (String fileName : FILES_TO_INCLUDE) {
                addToJar(new File(fileName), target);
            }*/
            for (File directory : new File("src").listFiles()) {
                addToJar(directory, target);
            }
            target.close();
        } catch (IOException e) {
            // TODO - report failure to build the game
            e.printStackTrace();
        }
    }

    private void compile(File source) {
        if (source.isDirectory() && !source.getName().isEmpty()) {
            for (File subdirectoryOrFile : source.listFiles()) {
                compile(subdirectoryOrFile);
            }
        } else if (source.getName().endsWith(".java")){
            javaCompiler.run(null, null, null, source.getAbsolutePath());
        }
    }

    // This and its called methods are based on
    // https://stackoverflow.com/questions/1281229/how-to-use-jaroutputstream-to-create-a-jar-file
    private void addToJar(File source, JarOutputStream target) throws IOException {
        if (source.isDirectory() && !source.getName().isEmpty()) {
            addDirectoryToJar(source, target);
        } else if (!source.getName().endsWith(".java")){
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
        InputStream in = new BufferedInputStream(new FileInputStream(source));
        byte[] buffer = new byte[MAX_ENTRY_LENGTH];
        int count;
        System.out.println(source.getPath());
        while ((count = in.read(buffer)) != -1) {
            target.write(buffer, 0, count);
        }
        in.close();
    }

    private String convertPathToJarFormat(String path) {
        return path.replaceAll(WINDOWS_PATH_DELIMITER_PATTERN, File.separator);
    }
    
    
    /**
     * @param fileName
     * @return the resource as an InputStream
     * Based on code from 
     * https://www.cefns.nau.edu/~edo/Classes/CS477_WWW/Docs/pack_resources_in_jar.html
     */
    public InputStream accessProperties (String fileName) {
    	ClassLoader sampleClass = this.getClass().getClassLoader();
    	return sampleClass.getResourceAsStream(fileName);
    }
}
