package sprites;

import javafx.embed.swing.JFXPanel;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Tests the sprite factory.
 *
 * @author Ben Schwennesen
 */
public class FactoryTesting {

    public static void main(String[] args) {
        SpriteFactory sf = new SpriteFactory();
        Map<String, List<String>> baseConfig = sf.getElementBaseConfigurationOptions();
        Scanner in = new Scanner(System.in);
        Map<String, String> choices = new HashMap<>();
        for (String k : baseConfig.keySet()) {
            System.out.println(String.format("Pick one of the following options for %s", k));
            baseConfig.get(k).forEach(option -> System.out.println("\t" + option));
            choices.put(k, in.nextLine().trim());
        }
        for (Map.Entry<String, Class> e : sf.getAuxiliaryElementProperties(choices).entrySet()) {
            System.out.println(String.format("Set %s (%s)", e.getKey(), e.getValue().getName()));
            choices.put(e.getKey(), in.nextLine().trim());
        }
        sf.defineElement("Tower1", choices);
        JFXPanel jfxPanel = new JFXPanel(); // so that ImageView can be made
        Sprite tower = sf.generateSprite("Tower1", new Point2D(10,10));
        System.out.println(tower.getX() + " " + tower.getY());

        /*
        sf.exportSpriteTemplates();*/
        // ^ need to manually examine the property file
    }
}
