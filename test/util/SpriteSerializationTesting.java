package util;

import com.thoughtworks.xstream.XStream;
import sprites.FactoryTesting;
import sprites.Sprite;

public class SpriteSerializationTesting {

    public static void main(String[] args) {
        FactoryTesting factoryTesting = new FactoryTesting();
        Sprite sprite = factoryTesting.generateSingleTestSprite();
        System.out.println(sprite.getX() + " " + sprite.getY());
        XStream xStream = new XStream();
        String spriteSerialization = xStream.toXML(sprite);
        System.out.println(spriteSerialization);
        Sprite deserialized = (Sprite) xStream.fromXML(spriteSerialization);
        System.out.println(deserialized.getX() + " " + deserialized.getY());
        //YaGson yaGson = new YaGson();
        //String ss = yaGson.toJson(sprite);
        //Sprite ds = (Sprite) yaGson.fromJson(ss, Sprite.class.getComponentType());
        //System.out.println(ds.getX());
        //SerializationUtils serializationUtils = new SerializationUtils();
        //serializationUtils.serializeLevelData("bleh", new HashMap<>(), new HashMap<>(), Arrays.asList(sprite), 1);
    }
}
