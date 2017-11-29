package util;

import engine.Bank;
import sprites.FactoryTesting;
import sprites.Sprite;

import java.util.Arrays;
import java.util.HashMap;

public class SpriteSerializationTesting {

    public static void main(String[] args) {
        FactoryTesting factoryTesting = new FactoryTesting();
        Sprite sprite = factoryTesting.generateSingleTestSprite();
        System.out.println(sprite.getX() + " " + sprite.getY());
        SerializationUtils serializationUtils = new SerializationUtils();
        serializationUtils.serializeLevelData("bleh", new HashMap<>(), new Bank(), new HashMap<>(), Arrays.asList
                (sprite), 1);
    }
}
