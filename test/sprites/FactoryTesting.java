package sprites;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import engine.behavior.collision.ImmortalCollider;
import engine.behavior.collision.ImperviousCollisionVisitable;
import engine.behavior.firing.NoopFiringStrategy;
import engine.behavior.movement.MovementStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * Tests the sprite factory.
 *
 * @author Ben Schwennesen
 */
public class FactoryTesting {

    public static void main(String[] args) {
        SpriteFactory sf = new SpriteFactory();
        Map<String, Object> bleh = new HashMap<>();
        bleh.put("movementStrategy", new MovementStrategy(0, 0) {
            @Override
            public void move() {
            }

            @Override
            public void handleBlock() {
            }
        });
        bleh.put("firingStrategy", new NoopFiringStrategy());
        bleh.put("collisionVisitor", new ImmortalCollider());
        bleh.put("collisionVisitable", new ImperviousCollisionVisitable());
        sf.generateSprite("blehSprite", bleh);
        XStream x = new XStream(new DomDriver());
        String saved = "";
        for (Sprite template : sf.getLevelSprites(1).get("blehSprite")) {
            saved = x.toXML(template);
            System.out.println(saved);
        }
        Sprite loaded = (Sprite) x.fromXML(saved);
        System.out.println(loaded.isAlive());
        sf.exportSpriteTemplates();
        // ^ need to manually examine the property file
    }
}
