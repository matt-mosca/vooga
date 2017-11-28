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
        System.out.println(baseConfig.keySet());
        for (String k : baseConfig.keySet()) {
            System.out.println(String.format("Pick one of the following options for %s", k));
            baseConfig.get(k).forEach(option -> System.out.println("\t" + option));
            choices.put(k, in.nextLine().trim());
        }
        System.out.println(choices);

        for (String k : sf.getAuxiliaryElementProperties(choices)) {
            System.out.println(String.format("Set %s", k));
            choices.put(k, in.nextLine().trim());
        }
        System.out.println(choices);
        sf.defineElement("Tower1", choices);
        JFXPanel jfxPanel = new JFXPanel();
        Sprite tower = sf.generateSprite("Tower1", new Point2D(10,10), new ImageView(new Image("https://users.cs.duke.edu/~rcd/images/rcd.jpg")), new HashMap<>());
        System.out.println(tower.getX() + " " + tower.getY());

        /*
        sf.exportSpriteTemplates();*/
        // ^ need to manually examine the property file
    }
}
