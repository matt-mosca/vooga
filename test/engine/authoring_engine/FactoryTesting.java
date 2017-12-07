package engine.authoring_engine;

import engine.game_elements.GameElement;
import engine.game_elements.GameElementFactory;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Point2D;

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
        GameElementFactory gameElementFactory = new GameElementFactory();
        GameElement gameElement = factoryTesting.generateSingleTestSprite(gameElementFactory);
        factoryTesting.testExport(gameElementFactory);
    }

    private void testWithConsole() {
        GameElementFactory sf = new GameElementFactory();
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
        GameElement tower = sf.generateSprite("Tower1", new Point2D(10,10));
        System.out.println(tower.getX() + " " + tower.getY());
    }

    public GameElement generateSingleTestSprite() {
        GameElementFactory gameElementFactory = new GameElementFactory();
        return generateSingleTestSprite(gameElementFactory);
    }

    private GameElement generateSingleTestSprite(GameElementFactory gameElementFactory) {
        System.out.println(gameElementFactory.getElementBaseConfigurationOptions());
        gameElementFactory.getElementBaseConfigurationOptions();
        Map<String, String> choices = new HashMap<>();
        choices.put("Move an object", "Track an object as it moves");
        choices.put("Collision effects", "Invulnerable to collision damage");
        choices.put("Collided-with effects", "Deal damage to colliding objects");
        choices.put("Firing Behavior", "Do not fire projectiles");
        Map<String, Class> auxProperties = gameElementFactory.getAuxiliaryElementProperties(choices);
        System.out.println(auxProperties);
        choices.put("Numerical \"team\" association", "0");
        choices.put("imageWidth", "42.0");
        choices.put("imageUrl", "https://pbs.twimg.com/media/CeafUfjUUAA5eKY.png");
        choices.put("imageHeight", "42.0");
        gameElementFactory.defineElement("test element", choices);
        JFXPanel jfxPanel = new JFXPanel(); // so that ImageView can be made
        return gameElementFactory.generateSprite("test element", new Point2D(0, 0));
    }

    private void testExport(GameElementFactory gameElementFactory) {
        // gameElementFactory.exportSpriteTemplates("Test Game");
    }
}
