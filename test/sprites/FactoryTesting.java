package sprites;

import engine.behavior.ParameterName;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
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
        SpriteFactory sf = new SpriteFactory();
        Map<String, List<String>> baseConfig = sf.getElementBaseConfigurationOptions();
        Scanner in = new Scanner(System.in);
        Map<String, String> choices = new HashMap<>();
        for (String k : baseConfig.keySet()) {
            System.out.println(String.format("Pick one of the following options for %s", k));
            baseConfig.get(k).forEach(option -> System.out.println("\t" + option));
            System.out.println();
            choices.put(k, in.nextLine().trim());
        }
        System.out.println(choices);

        for (String k : sf.getAuxiliaryElementProperties(choices)) {
            System.out.println(String.format("Set %s", k));
            System.out.println();
            choices.put(k, in.nextLine().trim());
        }
        System.out.println(choices);
        sf.defineElement("Tower1", choices);
        Sprite tower = sf.generateSprite("Tower1");
        System.out.println(tower.getX() + " " + tower.getY());
        /*Map<String, String> bleh = new HashMap<>();
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
        System.out.println(sf.getElementBaseConfigurationOptions());*

        /*
        sf.exportSpriteTemplates();*/
        // ^ need to manually examine the property file
    }
}
