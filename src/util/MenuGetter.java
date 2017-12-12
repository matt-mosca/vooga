package util;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.TreeMap;

/**
 * Used to generate the IDE menu bar items from a properties file.
 *
 * @author Ben Schwennesen
 */
public class MenuGetter {

    public static final String INFO_DELIMITER = ",";
    private final int INFO_LENGTH = 2;
    private ResourceBundle menuProperties = ResourceBundle.getBundle("MenuBar");

    /**
     * Construct the menu bar item getter.
     *
     * @throws IOException - in the case that the menu properties file is not found
     */
    public MenuGetter() throws IOException {
        menuProperties = ResourceBundle.getBundle("MenuBar");
    }

    private void generateMenuItem(Map<String, Menu> dropdownsMap, String itemName/*,IDEWindow runner*/) {
        String[] dropdownInfo;
        if (menuProperties.containsKey(itemName)) {
            dropdownInfo = menuProperties.getString(itemName).split(INFO_DELIMITER);
            if (dropdownInfo.length != INFO_LENGTH) {
                return;
            }
            String dropdownName = dropdownInfo[0];
            Menu dropdown = dropdownsMap.getOrDefault(dropdownName, new Menu(dropdownName));
            //Method actionMethod = runner.getClass().getDeclaredMethod(dropdownInfo[1]);
            MenuItem menuItem = new MenuItem(itemName);
            menuItem.setOnAction(e -> System.out.println(e)/*runMenuAction(actionMethod, runner)*/);
            dropdown.getItems().add(menuItem);
            dropdownsMap.put(dropdownName, dropdown);
        }
    }

    private void runMenuAction(Method actionMethod /*,IDEWindow runner*/) {
        //try {
            actionMethod.setAccessible(true);
            System.out.println(actionMethod.getName());
            //actionMethod.invoke(runner);
        /*} catch (ReflectiveOperationException callFailure) {
            System.out.println("FUCK");
            return;
        }*/
    }

    /**
     * Retrieve the dropdown items for use in the IDE menu bar, as generated from the properties file.
     *
     * @param runner - the IDE window instance, needed to create button press associations via reflection
     * @return a list of dropdown items to be included in the IDE menu bar
     */
    public List<Menu> getMenuDropdowns(/*IDEWindow runner*/) /*throws SLogoException*/ {
        Map<String, Menu> dropdownsMap = new TreeMap<>(Collections.reverseOrder());
        for (String itemName : menuProperties.keySet()) {
            generateMenuItem(dropdownsMap, itemName/*, runner*/);
        }
        return new ArrayList<>(dropdownsMap.values());
    }
}