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

	public static final String DESCRIPTION = "description";
	public static final String LEVEL = "LEVEL";
	public static final String STATUS = "status";
	public static final String SPRITES = "sprites";
	public static final String DELIMITER = "\n";
	// Description, Status, Sprites
	public static final int NUM_SERIALIZATION_SECTIONS = 3;
	public static final int DESCRIPTION_SERIALIZATION_INDEX = 0;
	public static final int LEVEL_SERIALIZATION_INDEX = 1;
	public static final int STATUS_SERIALIZATION_INDEX = 2;
	public static final int SPRITES_SERIALIZATION_INDEX = 3;
	private GsonBuilder gsonBuilder;

	public SerializationUtils() {
		gsonBuilder = new GsonBuilder();
	}

	// TODO - Modify logic to handle multiple levels, by having level as outer key of top-level map
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
	public String serializeGameData(String gameDescription, int level, Map<String, String> status,
			Collection<Sprite> elements) {
		StringBuilder gameDataStringBuilder = new StringBuilder();
		gameDataStringBuilder.append(serializeGameDescription(gameDescription));
		gameDataStringBuilder.append(DELIMITER);
		gameDataStringBuilder.append(serializeGameLevel(level));
		gameDataStringBuilder.append(DELIMITER);
		gameDataStringBuilder.append(serializeStatus(status));
		gameDataStringBuilder.append(DELIMITER);
		gameDataStringBuilder.append(serializeSprites(elements));
		return gameDataStringBuilder.toString();
	}

	// TODO - for all deserialization methods : take level as parameter
	
	/**
	 * Deserialize previously serialized game description into a string
	 * 
	 * @param serializedGameData
	 *            string of serialized game data, both top-level game status and
	 *            elements
	 * @return string corresponding to game description
	 * @throws IllegalArgumentException
	 *             if serialization is ill-formatted
	 */
	public String deserializeGameDescription(String serializedGameData) throws IllegalArgumentException {
		String[] serializedSections = retrieveSerializedSections(serializedGameData);
		return deserializeDescription(serializedSections[DESCRIPTION_SERIALIZATION_INDEX]);
	}

	/**
	 * Deserialize previously serialized game level
	 * 
	 * @param serializedGameData
	 *            string of serialized game data, both top-level game status and
	 *            elements
	 * @return int corresponding to level of the game data
	 * @throws IllegalArgumentException
	 */
	public int deserializeGameLevel(String serializedGameData) throws IllegalArgumentException {
		String[] serializedSections = retrieveSerializedSections(serializedGameData);
		return deserializeLevel(serializedSections[LEVEL_SERIALIZATION_INDEX]);
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
		String[] serializedPortions = retrieveSerializedSections(serializedGameData);
		return deserializeStatus(serializedPortions[STATUS_SERIALIZATION_INDEX]);
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
		String[] serializedSections = retrieveSerializedSections(serializedGameData);
		return deserializeSprites(serializedSections[SPRITES_SERIALIZATION_INDEX]);
	}

	private String serializeGameDescription(String gameDescription) {
		Map<String, String> descriptionMap = new HashMap<>();
		descriptionMap.put(DESCRIPTION, gameDescription);
		return gsonBuilder.create().toJson(descriptionMap);
	}

	private String serializeGameLevel(int level) {
		Map<String, Integer> levelMap = new HashMap<>();
		levelMap.put(LEVEL, level);
		return gsonBuilder.create().toJson(levelMap);
	}

	private String serializeStatus(Map<String, String> status) {
		StringBuilder bob = new StringBuilder();
		System.out.println("Serializing status");
		gsonBuilder.create().toJson(status, bob);
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

	private String deserializeDescription(String serializedDescription) {
		Map<String, String> descriptionMap = gsonBuilder.create().fromJson(serializedDescription, Map.class);
		return descriptionMap.get(DESCRIPTION);
	}

	private int deserializeLevel(String serializedLevel) {
		Map<String, Double> levelMap = gsonBuilder.create().fromJson(serializedLevel, Map.class);
		return (int) Math.floor(levelMap.get(LEVEL));
	}

	private Map<String, String> deserializeStatus(String serializedStatus) {
		return gsonBuilder.create().fromJson(serializedStatus, Map.class);
	}

	// Return a map of sprite name to list of elements, which can be used by
	// ElementFactory to construct sprite objects
	private Map<String, List<Sprite>> deserializeSprites(String serializedSprites) {
		return gsonBuilder.create().fromJson(serializedSprites, Map.class);
	}

	private String[] retrieveSerializedSections(String serializedGameData) throws IllegalArgumentException {
		String[] serializedSections = serializedGameData.split(DELIMITER);
		if (serializedSections.length < NUM_SERIALIZATION_SECTIONS) {
			throw new IllegalArgumentException();
		}
		return serializedSections;
	}

	// For testing
	public static void main(String[] args) {
		SerializationUtils tester = new SerializationUtils();
		String testDescription = "test_game";
		int testLevel = 1;
		Map<String, String> testStatus = new HashMap<>();
		testStatus.put("lives", "3");
		testStatus.put("gold", "100");
		Tower testTower = new Tower("testTower", 100, 1);
		Tower testTower2 = new Tower("testTower", 150, 2);
		Tower testTower3 = new Tower("testTower2", 200, 3);
		Soldier testSoldier = new Soldier("testSoldier", 25, 2);
		Soldier testSoldier2 = new Soldier("testSoldier", 50, 1);
		Soldier testSoldier3 = new Soldier("testSoldier2", 75, 3);
		Collection<Sprite> allSprites = Stream
				.of(testTower, testTower2, testTower3, testSoldier, testSoldier2, testSoldier3)
				.collect(Collectors.toList());
		String serializedGameData = tester.serializeGameData(testDescription, testLevel, testStatus, allSprites);
		System.out.println("Serialized sprites: " + serializedGameData);
		System.out.println("Game Description: " + tester.deserializeGameDescription(serializedGameData));
		System.out.println("Level: " + tester.deserializeGameLevel(serializedGameData));
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
