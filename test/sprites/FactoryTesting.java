package sprites;

import java.util.HashMap;
import java.util.Map;

/**
 * Tests the sprite factory.
 *
 * @author Ben Schwennesen
 */
public class FactoryTesting {

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        SpriteFactory sf = new SpriteFactory();
        map.put("health-points", "50.0");
        map.put("damage", "4.0");
        map.put("image", "http://veryrealurl.com/veryrealimage.png");
        try {
            sf.generateSprite("TestSprite", map);
        } catch (ReflectiveOperationException e) {
            System.out.println("reflection failure");
            e.printStackTrace();
        }
        sf.exportSpriteTemplates();
        // ^ need to manually examine the property file
    }
}
