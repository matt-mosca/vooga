package util;

import com.google.gson.GsonBuilder;
import engine.Bank;
import sprites.FactoryTesting;
import sprites.Sprite;

import java.util.*;

public class SpriteSerializationTesting {

    public static void main(String[] args) {
        FactoryTesting factoryTesting = new FactoryTesting();
        Sprite sprite = factoryTesting.generateSingleTestSprite();
        sprite.setX(50000000);
        System.out.println(sprite.getX() + " " + sprite.getY());
        SerializationUtils serializationUtils = new SerializationUtils();
        String ss = serializationUtils.serializeLevelData("bleh", new HashMap<>(), new Bank(),
                new HashMap<>(), Arrays.asList(sprite), new HashSet<>(),1);
        Map<Integer, String> map = new HashMap<>();
        map.put(1, ss);
        String sssss = serializationUtils.serializeLevelsData(map);
        System.out.println(sssss);
        List<Sprite> ls = serializationUtils.deserializeGameSprites(sssss, 1);
        System.out.println("x: " + ls.get(0).getX());
        ls.get(0).setX(10000000);
        System.out.println("xAgain: " + ls.get(0).getX());
        /*GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setExclusionStrategies(new AnnotationExclusionStrategy());
        gsonBuilder.serializeSpecialFloatingPointValues();
        gsonBuilder.setLenient();
        String ss = gsonBuilder.create().toJson(sprite);
        System.out.println(ss);
        Sprite ds = gsonBuilder.create().fromJson(ss, Sprite.class);*/
    }
}
