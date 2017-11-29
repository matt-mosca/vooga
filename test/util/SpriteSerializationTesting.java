package util;

import com.google.gson.GsonBuilder;
import engine.Bank;
import sprites.FactoryTesting;
import sprites.Sprite;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SpriteSerializationTesting {

    public static void main(String[] args) {
        FactoryTesting factoryTesting = new FactoryTesting();
        Sprite sprite = factoryTesting.generateSingleTestSprite();
        /*System.out.println(sprite.getX() + " " + sprite.getY());
        SerializationUtils serializationUtils = new SerializationUtils();
        String ss = serializationUtils.serializeLevelData("bleh", new HashMap<>(), new Bank(), new HashMap<>(), Arrays
                .asList(sprite), 1);
        System.out.println(ss);
        List<Sprite> ls = serializationUtils.deserializeGameSprites(ss, 1);*/
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setExclusionStrategies(new AnnotationExclusionStrategy());
        gsonBuilder.serializeSpecialFloatingPointValues();
        gsonBuilder.setLenient();
        String ss = gsonBuilder.create().toJson(sprite);
        System.out.println(ss);
        Sprite ds = gsonBuilder.create().fromJson(ss, Sprite.class);
    }
}
