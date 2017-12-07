package packaging;

import main.Main;
import main.MainJarRunner;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * Tests the packager.
 *
 * @author Ben Schwennesen
 */
public class PackagingTest {

    private static void testJarCreation(Packager packager) {
        try {
            Properties properties = new Properties();
            properties.load(PackagingTest.class.getClassLoader().getResourceAsStream("ExportedGameName.properties"));
            properties.setProperty("gameFile", "");
            packager.generateJar("data/games/jar-package-testing.jar", "src",
                    "out/production/voogasalad_duvallinthistogether/", MainJarRunner.class,
                    "resources/", "authoring/", "data/", "lib/");
            // test the JAR manually with a launch
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void testPathConversion(Packager packager) throws Exception {
        String test = "C:\\some\\windows\\path";
        String expected = "C:/some/windows/path";
        Method convertPath = packager.getClass().getDeclaredMethod("convertPathToJarFormat", String.class);
        convertPath.setAccessible(true);
        String result = (String) convertPath.invoke(packager, test);
        assert result.equals(expected);
    }

    public static void main(String[] args) {
        Packager packager = new Packager();
        try{
            testJarCreation(packager);
            // testPathConversion(packager);
        } catch (Exception e) {
            // ignore
            e.printStackTrace();
        }
    }

}
