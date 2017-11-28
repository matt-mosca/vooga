package sprites;

import engine.behavior.collision.CollisionHandler;
import engine.behavior.collision.CollisionVisitable;
import engine.behavior.collision.CollisionVisitor;
import engine.behavior.firing.FiringStrategy;
import engine.behavior.movement.AbstractMovementStrategy;
import engine.behavior.movement.MovementHandler;

/**
 * Tests sprite creation / field setting.
 *
 * @author Ben Schwennesen
 */
public class SpriteTesting {

    public static void main(String[] args) {
        /*Map<String, Object> map = new HashMap<>();
        map.put("xCoord", 5.0);
        map.put("yCoord", 10.0);
        map.put("imageUrl", "http://google.com/definitely_a_valid_image.png");

        map.put("xVelocity", 55.0);
        map.put("yVelocity", 563.0);
        map.put("isAlive", true);

        Sprite testSprite = new SpriteTesting(map, "imaginary template");

        testSprite.getFieldNames().forEach(fieldName -> {
            assert map.containsKey(fieldName) || fieldName.equals("$assertionsDisabled");
        });

        assert testSprite.getX() == 5.0;
        assert testSprite.getCurrentY() == 10.0;
        //assert testSprite.getXVelocity() == 55.0;
        //assert testSprite.getYVelocity() == 563.0;
        assert (((SpriteTesting) testSprite).imageUrl).equals("http://google.com/definitely_a_valid_image.png");*/
    }
}