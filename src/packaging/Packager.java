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

    private final String EMPTY_STRING = "";
    private final String WINDOWS_PATH_DELIMITER_PATTERN = Pattern.quote("\\");
    private final String MANIFEST_VERSION = "1.0";
    private final String JAR_EXTENSION = ".jar";
    private final String JAVA_EXTENSION = ".java";
    private final String CLASS_EXTENSION = ".class";
    private final String LOG_EXTENSION = ".log";
    private final char DOT = '.';

    private final int MAX_ENTRY_LENGTH = 2048;

    private JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();

    private Set<File> filesToDelete = new HashSet<>();

    /**
     * Generate an executable JAR file for an authored game, obtaining the necessary information about parts of the
     * project being included from a properties file.
     *
     * @param gameExportName the name to give the exported JAR file, not including path or extension
     */
    public void generateJar(String gameExportName) throws IOException {
        JarPropertiesGetter propertiesGetter = new JarPropertiesGetter();
        String outputPath = propertiesGetter.getExportTargetPath(gameExportName);
        JarOutputStream target = initializeJarOutputStream(outputPath, propertiesGetter.getMainClassFullName());
        compileAndAddSourceFiles(propertiesGetter, outputPath, target);
        addResources(propertiesGetter, target);
        target.close();
    }

    private void addResources(JarPropertiesGetter propertiesGetter, JarOutputStream target) throws IOException {
        for (String resourceRootPath : propertiesGetter.getResourceRoots()) {
            addResourcesRoot(resourceRootPath, target);
        }
        for (String dataDirectoryPath : propertiesGetter.getDataAndLibraryDirectories()) {
            addToJar(new File(dataDirectoryPath), target, EMPTY_STRING, CLASS_EXTENSION);
        }
    }

    private void compileAndAddSourceFiles(JarPropertiesGetter propertiesGetter, String outputPath,
                                          JarOutputStream target) throws IOException {
        String sourceDirectoryPath = propertiesGetter.getSourceDirectoryPath();
        String pathToExcludeFromSourceEntries = addTrailingFileSeparatorToPath(sourceDirectoryPath);
        OutputStream compilationLogger = createLogFileOutput(outputPath);
        for (String directoryToIncludePath : propertiesGetter.getSourceDirectoriesToInclude()) {
            File directoryToInclude = new File(directoryToIncludePath);
            compile(directoryToInclude, compilationLogger);
            addToJar(directoryToInclude, target, pathToExcludeFromSourceEntries, JAVA_EXTENSION);
        }
        cleanUp();
    }

    private OutputStream createLogFileOutput(String outputPath) throws FileNotFoundException {
        String logPath = outputPath.substring(0, outputPath.lastIndexOf(DOT)) + LOG_EXTENSION;
        File logFile = new File(logPath);
        return new FileOutputStream(logFile);
    }

    private void compile(File source, OutputStream compilationLogger) {
        if (source.isDirectory()) {
            File[] sourceFiles = source.listFiles();
            if (sourceFiles != null) {
                for (File subdirectoryOrFile : sourceFiles) {
                    compile(subdirectoryOrFile, compilationLogger);
                }
            }
        } else if (source.getName().endsWith(JAVA_EXTENSION)) {
            javaCompiler.run(null, compilationLogger, compilationLogger, source.getAbsolutePath());
            filesToDelete.add(new File(source.getAbsolutePath().replace(JAVA_EXTENSION, CLASS_EXTENSION)));
        }
    }

    private void cleanUp() {
        filesToDelete.removeIf(file -> !file.getPath().endsWith(CLASS_EXTENSION));
        filesToDelete.forEach(File::delete);
    }

    private JarOutputStream initializeJarOutputStream(String outputPath, String launchClassName) throws IOException {
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, MANIFEST_VERSION);
        manifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS, launchClassName);
        FileOutputStream outputStream = new FileOutputStream(outputPath);
        return new JarOutputStream(outputStream, manifest);
    }

    private String addTrailingFileSeparatorToPath(String path) {
        return path.endsWith(File.separator) ? path : path + File.separator;
    }

    private void addResourcesRoot(String resourceDirectoryPath, JarOutputStream target) throws IOException {
        File resourceDirectory = new File(resourceDirectoryPath);
        File[] resourceSources = resourceDirectory.listFiles();
        if (resourceSources != null) {
            for (File resourceSource : resourceSources) {
                System.out.println(resourceSource.getPath());
                addToJar(resourceSource, target, resourceDirectoryPath, EMPTY_STRING);
            }
        }
    }

    private void addToJar(File additionSource, JarOutputStream target, String excludeFromPath, String extensionToIgnore)
            throws IOException {
        if (additionSource.getPath().endsWith(JAR_EXTENSION)) {
            addExternalLibrary(additionSource, target);
        } else if (additionSource.isDirectory()) {
            addDirectoryToJar(additionSource, target, excludeFromPath, extensionToIgnore);
        } else if (extensionToIgnore.isEmpty() || !additionSource.getPath().endsWith(extensionToIgnore)) {
            addFileToJar(target, additionSource, excludeFromPath);
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
        return convertedPath.replace(partOfPathToExclude, EMPTY_STRING);
    }
}
