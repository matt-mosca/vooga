package sprites;

import engine.authoring_engine.AuthoringController;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.SerializationUtils;

import java.util.Arrays;
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
        FactoryTesting factoryTesting = new FactoryTesting();
        //factoryTesting.testWithConsole();
        SpriteFactory spriteFactory = new SpriteFactory();
        Sprite sprite = factoryTesting.generateSingleTestSprite(spriteFactory);
        factoryTesting.testExport(spriteFactory);
    }

    private void testWithConsole() {
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
    }

    public Sprite generateSingleTestSprite() {
        SpriteFactory spriteFactory = new SpriteFactory();
        return generateSingleTestSprite(spriteFactory);
    }

    private Sprite generateSingleTestSprite(SpriteFactory spriteFactory) {
        System.out.println(spriteFactory.getElementBaseConfigurationOptions());
        Map<String, String> choices = new HashMap<>();
        choices.put("Move an object", "Move in a straight line toward a desired location");
        choices.put("Collision effects", "Takes damage from collisions");
        choices.put("Collided-with effects", "Deal damage to colliding objects");
        choices.put("Firing Behavior", "Shoot periodically");
        Map<String, Class> auxProperties = spriteFactory.getAuxiliaryElementProperties(choices);
        choices.put("Target y-coordinate", "1.0");
        choices.put("imageWidth", "10.0");
        choices.put("Damage dealt to colliding objects", "1.0");
        choices.put("Health points", "50.0");
        choices.put("Target x-coordinate", "1.0");
        choices.put("imageUrl", "https://pbs.twimg.com/media/CeafUfjUUAA5eKY.png");
        choices.put("Speed of movement", "1");
        choices.put("Numerical \"team\" association", "1");
        choices.put("Attack period", "1");
        choices.put("imageHeight", "10.0");
        choices.put("Projectile Type Name", "fake name");
        spriteFactory.defineElement("test element", choices);
        JFXPanel jfxPanel = new JFXPanel(); // so that ImageView can be made
        return spriteFactory.generateSprite("test element", new Point2D(0, 0));
    }

    private void testExport(SpriteFactory spriteFactory) {
        // spriteFactory.exportSpriteTemplates("Test Game");
    }
}
