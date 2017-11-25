package sprites;

import engine.behavior.ParameterName;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Arrays;
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
        Map<String, String> bleh = new HashMap<>();
        String mc = "engine.behavior.collision.GenericCollider";
        try{
            Class clazz = Class.forName(mc);
            for (Constructor c : clazz.getConstructors()) {
                Parameter[] ps = c.getParameters();
                for (Parameter p : c.getParameters()) {
                    System.out.println(p.getName() + " " + p.getType()+ " " + p.getAnnotation(ParameterName.class)
                            .value());
                }
            }
            for (Method m : clazz.getDeclaredMethods()) {
                System.out.println(m.getName());
                for (Type t : m.getGenericParameterTypes()) {
                    System.out.println("\t" + t.getTypeName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(sf.getElementBaseConfigurationOptions());


        /*
        sf.exportSpriteTemplates();*/
        // ^ need to manually examine the property file
    }
}
