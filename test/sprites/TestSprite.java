package sprites;

import java.util.HashMap;
import java.util.Map;

/**
 * Tests sprite creation / field setting.
 *
 * @author Ben Schwennesen
 */
public class TestSprite extends Sprite {

    String imageUrl;

    public TestSprite(Map<String, ?> properties, String templateName) {
        super(properties, templateName);
    }

    @Override
    public void update() {

    }

    @Override
    public void move() {

    }

    @Override
    public void attack() {

    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("xCoord", 5.0);
        map.put("yCoord", 10.0);
        map.put("imageUrl", "http://google.com/definitely_a_valid_image.png");

        map.put("xVelocity", 55.0);
        map.put("yVelocity", 563.0);
        map.put("isActive", true);

        Sprite testSprite = new TestSprite(map, "imaginary template");

        testSprite.getFieldNames().forEach(fieldName -> {
            assert map.containsKey(fieldName) || fieldName.equals("$assertionsDisabled");
        });

        assert testSprite.getX() == 5.0;
        assert testSprite.getY() == 10.0;
        assert testSprite.getXVelocity() == 55.0;
        assert testSprite.getYVelocity() == 563.0;
        assert (((TestSprite) testSprite).imageUrl).equals("http://google.com/definitely_a_valid_image.png");
    }
}