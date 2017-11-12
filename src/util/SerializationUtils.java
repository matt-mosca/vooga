package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import sprites.Soldier;
import sprites.Sprite;
import sprites.Tower;

public class SerializationUtils {

	public static final String STATUS = "status";
	public static final String SPRITES = "sprites";
	public static final String DELIMITER = "\n";
	private GsonBuilder gsonBuilder;

	public SerializationUtils() {
		gsonBuilder = new GsonBuilder();
	}

	/**
	 * Serialize all game data - status and collection of sprites
	 * 
	 * @param status
	 *            top-level game status from Heads-Up-Display, i.e. all game state
	 *            other than the Sprites
	 * @param elements
	 *            the active Sprites in the game
	 * @return
	 */
	public String serializeGameData(Map<String, String> status, Collection<Sprite> elements) {
		StringBuilder gameDataStringBuilder = new StringBuilder();
		gameDataStringBuilder.append(serializeStatus(status));
		gameDataStringBuilder.append(DELIMITER);
		gameDataStringBuilder.append(serializeSprites(elements));
		return gameDataStringBuilder.toString();
	}

	/**
	 * Deserialize previously serialized game data into a game status map
	 * 
	 * @param serializedGameData
	 *            string of serialized game data, both top-level game status and
	 *            elements
	 * @return map corresponding to top-level status
	 * @throws IllegalArgumentException
	 *             if serialization is ill-formatted
	 */
	public Map<String, String> deserializeGameStatus(String serializedGameData) throws IllegalArgumentException {
		String[] serializedPortions = serializedGameData.split(DELIMITER);
		if (serializedPortions.length < 2) {
			throw new IllegalArgumentException();
		}
		return deserializeStatus(serializedPortions[0]);
	}

	/**
	 * Deserialize previously serialized game data into a sprite map where sprite
	 * name is key and a list of sprites is its value
	 * 
	 * @param serializedGameData
	 *            string of serialized game data, both top-level game status and
	 *            elements
	 * @return map of sprite name to list of sprites of that name / type
	 * @throws IllegalArgumentException
	 *             if serialization is ill-formatted
	 */
	public Map<String, List<Sprite>> deserializeGameSprites(String serializedGameData) throws IllegalArgumentException {
		String[] serializedPortions = serializedGameData.split(DELIMITER);
		if (serializedPortions.length < 2) {
			throw new IllegalArgumentException();
		}
		return deserializeSprites(serializedPortions[1]);
	}

	private Map<String, String> deserializeStatus(String serializedStatus) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(serializedStatus, Map.class);
	}

	// Return a map of sprite name to list of elements, which can be used by
	// ElementFactory to construct sprite objects
	private Map<String, List<Sprite>> deserializeSprites(String serializedSprites) {
		return gsonBuilder.create().fromJson(serializedSprites, Map.class);
	}

	private String serializeStatus(Map<String, String> status) {
		StringBuilder bob = new StringBuilder();
		Gson gson = new GsonBuilder().create();
		System.out.println("Serializing status");
		gson.toJson(status, bob);
		String serializedStatus = bob.toString();
		System.out.println(serializedStatus);
		return serializedStatus;
	}

	// Collect multiple sprites into a top-level map
	private String serializeSprites(Collection<Sprite> elements) {
		Map<String, List<Sprite>> spritesMap = new HashMap<>();
		for (Sprite sprite : elements) {
			String spriteName = sprite.getName();
			List<Sprite> spritesOfThisName;
			if (!spritesMap.containsKey(spriteName)) {
				spritesOfThisName = new ArrayList<>();
			} else {
				spritesOfThisName = spritesMap.get(spriteName);
			}
			spritesOfThisName.add(sprite);
			spritesMap.put(spriteName, spritesOfThisName);
		}
		return gsonBuilder.create().toJson(spritesMap);
	}

	// For testing
	public static void main(String[] args) {
		SerializationUtils tester = new SerializationUtils();
		Map<String, String> testStatus = new HashMap<>();
		testStatus.put("lives", "3");
		testStatus.put("gold", "100");
		testStatus.put("level", "1");
		Tower testTower = new Tower("testTower", 100, 1);
		Tower testTower2 = new Tower("testTower", 150, 2);
		Tower testTower3 = new Tower("testTower2", 200, 3);
		Soldier testSoldier = new Soldier("testSoldier", 25, 2);
		Soldier testSoldier2 = new Soldier("testSoldier", 50, 1);
		Soldier testSoldier3 = new Soldier("testSoldier2", 75, 3);
		Collection<Sprite> allSprites = Stream
				.of(testTower, testTower2, testTower3, testSoldier, testSoldier2, testSoldier3)
				.collect(Collectors.toList());
		String serializedGameData = tester.serializeGameData(testStatus, allSprites);
		System.out.println("Serialized sprites: " + serializedGameData);
		Map<String, String> deserializedStatus = tester.deserializeGameStatus(serializedGameData);
		Map<String, List<Sprite>> deserializedSprites = tester.deserializeGameSprites(serializedGameData);
		for (String statusKey : deserializedStatus.keySet()) {
			System.out.println(statusKey + " : " + deserializedStatus.get(statusKey));
		}
		for (String elementName : deserializedSprites.keySet()) {
			System.out.println("Element name: " + elementName);
			System.out.println(deserializedSprites.get(elementName).toString());
		}
	}

}
