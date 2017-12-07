package packaging;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.zip.ZipException;

/**
 * Packages up a game for delivery as an executable JAR.
 *
 * @author Ben Schwennesen
 */
public class Packager {

    private final String WINDOWS_PATH_DELIMITER_PATTERN = Pattern.quote("\\");
    private final String MANIFEST_VERSION = "1.0";
    private final String JAR_EXTENSION = ".jar";
    private final String JAVA_EXTENSION = ".java";
    private final String CLASS_EXTENSION = ".class";

    private final int MAX_ENTRY_LENGTH = 2048;

    private JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();

    private Set<File> filesToDelete = new HashSet<>();

    /**
     * Generate an executable JAR file for an authored game.
     *
     * @param outputPath           output path for the exported game JAR file
     * @param sourceDirectoryPath  path to the source code directory
     * @param outDirectoryPath     path to the compiled code directory, needed for calls to getResourceAsStream()
     * @param launchClass          class used to launch the JAR
     * @param directoriesToInclude data or resource directories outside the source directory needed to run the game;
     *                             note that resources obtained through getResourceAsStream() are already handled
     * @throws IOException if one of the directories given does not exist or input is malformed
     */
    public void generateJar(String outputPath, String sourceDirectoryPath, String outDirectoryPath, Class launchClass,
                            String... directoriesToInclude) throws IOException {
        File sourceDirectory = new File(sourceDirectoryPath);
        compile(sourceDirectory);
        JarOutputStream target = initializeJarOutputStream(outputPath, launchClass);
        String pathToExcludeFromSourceEntries = addTrailingFileSeparatorToPath(sourceDirectoryPath);
        addToJar(sourceDirectory, target, pathToExcludeFromSourceEntries, JAVA_EXTENSION);
        String pathToExcludeFromOutEntries = addTrailingFileSeparatorToPath(outDirectoryPath);
        addToJar(new File(outDirectoryPath), target, pathToExcludeFromOutEntries, CLASS_EXTENSION);
        for (String directory : directoriesToInclude) {
            addToJar(new File(directory), target, "", CLASS_EXTENSION);
        }
        target.close();
        cleanUp();
    }

    private JarOutputStream initializeJarOutputStream(String outputPath, Class launchClass) throws IOException {
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, MANIFEST_VERSION);
        manifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS, launchClass.getName());
        FileOutputStream outputStream = new FileOutputStream(outputPath);
        return new JarOutputStream(outputStream, manifest);
    }

    private String addTrailingFileSeparatorToPath(String path) {
        return path.endsWith(File.separator) ? path : path + File.separator;
    }

    private void compile(File source) {
        if (source.isDirectory()) {
            File[] sourceFiles = source.listFiles();
            if (sourceFiles != null) {
                for (File subdirectoryOrFile : sourceFiles) {
                    compile(subdirectoryOrFile);
                }
            }
        } else if (source.getName().endsWith(JAVA_EXTENSION)) {
            javaCompiler.run(null, null, null, source.getAbsolutePath());
            filesToDelete.add(new File(source.getAbsolutePath().replace(JAVA_EXTENSION, CLASS_EXTENSION)));
        }
    }

    private void cleanUp() {
        filesToDelete.removeIf(file -> !file.getPath().endsWith(CLASS_EXTENSION));
        filesToDelete.forEach(File::delete);
    }

    private void addToJar(File source, JarOutputStream target, String excludeFromPath, String extensionToIgnore)
            throws IOException {
        if (source.getPath().endsWith(JAR_EXTENSION)) {
            addExternalLibrary(source, target);
        } else if (source.isDirectory()) {
            addDirectoryToJar(source, target, excludeFromPath, extensionToIgnore);
        } else if (!source.getPath().endsWith(extensionToIgnore)) {
            addFileToJar(target, source, excludeFromPath);
        }
    }

    private void addExternalLibrary(File libraryFile, JarOutputStream target) throws IOException {
        try {
            JarFile jarFile = new JarFile(libraryFile);
            Stream<JarEntry> jarEntryStream = jarFile.stream();
            jarEntryStream.forEach(jarEntry -> addLibraryJarEntry(target, jarFile, jarEntry));
        } catch (ZipException couldNotOpenZipException) {
            // ignore -- thrown because the "library file" is actually the target JAR
        }
    }

    private void addLibraryJarEntry(JarOutputStream target, JarFile jarFile, JarEntry jarEntry) {
        try {
            target.putNextEntry(jarEntry);
            writeFileToJar(jarFile.getInputStream(jarEntry), target);
            target.closeEntry();
        } catch (IOException entryAlreadyAddedException) {
            // ignore -- already added
        }
    }

    private void addDirectoryToJar(File source, JarOutputStream target, String excludeFromPath,
                                   String extensionToIgnore) throws IOException {
        writeDirectoryJarEntry(source, target, excludeFromPath);
        File[] nestedFilesAndDirectories = source.listFiles();
        if (nestedFilesAndDirectories != null) {
            for (File nestedElement : nestedFilesAndDirectories) {
                addToJar(nestedElement, target, excludeFromPath, extensionToIgnore);
            }
        }
    }

    private void writeDirectoryJarEntry(File directory, JarOutputStream target, String excludeFromPath) throws IOException {
        String directoryPathInJarFormat = convertPathToJarFormat(directory.getPath(), excludeFromPath,
                directory.isDirectory());
        JarEntry entry = new JarEntry(directoryPathInJarFormat);
        entry.setTime(directory.lastModified());
        try {
            target.putNextEntry(entry);
        } catch (ZipException duplicateEntryException) {
            // ignore -- duplicate
        }
        target.closeEntry();
    }

    private void addFileToJar(JarOutputStream target, File file, String excludeFromPath) throws IOException {
        String filePathInJarFormat = convertPathToJarFormat(file.getPath(), excludeFromPath, file.isDirectory());
        JarEntry entry = new JarEntry(filePathInJarFormat);
        entry.setTime(file.lastModified());
        try {
            target.putNextEntry(entry);
            writeFileToJar(new FileInputStream(file), target);
        } catch (ZipException duplicateEntryException) {
            // ignore -- duplicate
        }
        target.closeEntry();
    }

    private void writeFileToJar(InputStream sourceStream, JarOutputStream target) throws IOException {
        InputStream in = new BufferedInputStream(sourceStream);
        byte[] buffer = new byte[MAX_ENTRY_LENGTH];
        int count;
        while ((count = in.read(buffer)) != -1) {
            target.write(buffer, 0, count);
        }
        in.close();
    }


    private String convertPathToJarFormat(String path, String partOfPathToExclude, boolean isDirectory) {
        String convertedPath = path.replaceAll(WINDOWS_PATH_DELIMITER_PATTERN, File.separator);
        if (isDirectory && !convertedPath.endsWith(File.separator)) {
            convertedPath += File.separator;
        }
        return convertedPath.replace(partOfPathToExclude, "");
    }
}
