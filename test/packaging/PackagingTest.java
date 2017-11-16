package packaging;

import networking.ChatTestWindow;

import java.lang.reflect.Method;

/**
 * Tests the packager.
 *
 * @author Ben Schwennesen
 */
public class PackagingTest {

    private static void testJarCreation(Packager packager) {
        packager.generateJar("jar-package-testing", ChatTestWindow.class);
        // test the JAR manually with a launch
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
            testPathConversion(packager);
        } catch (Exception e) {
            // ignore
        }
    }

}
