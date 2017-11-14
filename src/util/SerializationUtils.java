package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.GsonBuilder;

import sprites.Soldier;
import sprites.Sprite;
import sprites.Tower;

public class SerializationUtils {

	public static final String DESCRIPTION = "description";
	public static final String STATUS = "status";
	public static final String SPRITES = "sprites";
	public static final String DELIMITER = "\n";
	// Description, Status, Sprites
	public static final int NUM_SERIALIZATION_SECTIONS = 3;
	public static final int DESCRIPTION_SERIALIZATION_INDEX = 0;
	public static final int STATUS_SERIALIZATION_INDEX = 1;
	public static final int SPRITES_SERIALIZATION_INDEX = 2;
	private GsonBuilder gsonBuilder;

	public SerializationUtils() {
		gsonBuilder = new GsonBuilder();
	}

	/**
	 * Serialize all game data for the given level - description, status and
	 * collection of sprites, nest serialized data within level identifier
	 * 
	 * @param gameDescription
	 *            (level-specific) description of game
	 * @param level
	 *            the level corresponding to the status, elements and description
	 *            data
	 * @param status
	 *            top-level game status from Heads-Up-Display, i.e. all game state
	 *            other than the Sprites
	 * @param elements
	 *            the active Sprites in the game
	 * @return serialization of map of level to serialized level data
	 */
	public String serializeGameData(String gameDescription, int level, Map<String, String> status,
			Collection<Sprite> elements) {
		Map<String, String> serializedLevelData = new HashMap<>();
		serializedLevelData.put(Integer.toString(level), serializeLevelData(gameDescription, status, elements));
		return gsonBuilder.create().toJson(serializedLevelData);
	}

	/**
	 * Serialize map of levels to serialized level data to a multi-level game
	 * serialization
	 * 
	 * @param serializedLevelsData
	 *            map of level to serialized data for that level
	 * @return serialization of this map, useful for storing data for multiple
	 *         levels of a game in a single file
	 */
	public String serializeLevelsData(Map<Integer, String> serializedLevelsData) {
		Map<String, String> serializedLevelsDataMap = new HashMap<>();
		for (Integer level : serializedLevelsData.keySet()) {
			serializedLevelsDataMap.put(Integer.toString(level), serializedLevelsData.get(level));
		}
		return gsonBuilder.create().toJson(serializedLevelsDataMap);
	}

	/**
	 * Serialize data for a specific level - description, status and collection of
	 * sprites
	 * 
	 * @param gameDescription
	 *            (level-specific) description of game
	 * @param status
	 *            top-level game status from Heads-Up-Display, i.e. all game state
	 *            other than the Sprites
	 * @param elements
	 *            the active Sprites in the game
	 * @return serialization of level data
	 */
	public String serializeLevelData(String gameDescription, Map<String, String> status, Collection<Sprite> elements) {
		StringBuilder gameDataStringBuilder = new StringBuilder();
		gameDataStringBuilder.append(serializeGameDescription(gameDescription));
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
	public String deserializeGameDescription(String serializedGameData, int level) throws IllegalArgumentException {
		String[] serializedSections = retrieveSerializedSectionsForLevel(serializedGameData, level);
		return deserializeDescription(serializedSections[DESCRIPTION_SERIALIZATION_INDEX]);
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
	public Map<String, String> deserializeGameStatus(String serializedGameData, int level)
			throws IllegalArgumentException {
		String[] serializedPortions = retrieveSerializedSectionsForLevel(serializedGameData, level);
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
	public Map<String, List<Sprite>> deserializeGameSprites(String serializedGameData, int level)
			throws IllegalArgumentException {
		String[] serializedSections = retrieveSerializedSectionsForLevel(serializedGameData, level);
		return deserializeSprites(serializedSections[SPRITES_SERIALIZATION_INDEX]);
	}

	private String serializeGameDescription(String gameDescription) {
		Map<String, String> descriptionMap = new HashMap<>();
		descriptionMap.put(DESCRIPTION, gameDescription);
		return gsonBuilder.create().toJson(descriptionMap);
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

	private Map<String, String> deserializeStatus(String serializedStatus) {
		return gsonBuilder.create().fromJson(serializedStatus, Map.class);
	}

	// Return a map of sprite name to list of elements, which can be used by
	// ElementFactory to construct sprite objects
	private Map<String, List<Sprite>> deserializeSprites(String serializedSprites) {
		return gsonBuilder.create().fromJson(serializedSprites, Map.class);
	}

	private String[] retrieveSerializedSectionsForLevel(String serializedGameData, int level)
			throws IllegalArgumentException {
		Map<String, String> serializedLevelData = gsonBuilder.create().fromJson(serializedGameData, Map.class);
		String levelString = Integer.toString(level);
		if (!serializedLevelData.containsKey(levelString)) {
			throw new IllegalArgumentException();
		}
		String[] serializedSections = serializedLevelData.get(levelString).split(DELIMITER);
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
		System.out.println("Game Description: " + tester.deserializeGameDescription(serializedGameData, testLevel));
		Map<String, String> deserializedStatus = tester.deserializeGameStatus(serializedGameData, testLevel);
		Map<String, List<Sprite>> deserializedSprites = tester.deserializeGameSprites(serializedGameData, testLevel);
		for (String statusKey : deserializedStatus.keySet()) {
			System.out.println(statusKey + " : " + deserializedStatus.get(statusKey));
		}
		for (String elementName : deserializedSprites.keySet()) {
			System.out.println("Element name: " + elementName);
			System.out.println(deserializedSprites.get(elementName).toString());
		}
	}

}
