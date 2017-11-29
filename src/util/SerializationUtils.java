package util;

import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;

import engine.Bank;
import engine.behavior.collision.CollisionVisitor;
import engine.behavior.collision.DamageDealingCollisionVisitable;
import engine.behavior.collision.ImmortalCollider;
import engine.behavior.firing.FiringStrategy;
import engine.behavior.movement.AbstractMovementStrategy;
import sprites.Sprite;
import sprites.SpriteFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SerializationUtils {

	public static final String DESCRIPTION = "description";
	public static final String CONDITIONS = "conditions";
	public static final String BANK = "bank";
	public static final String STATUS = "status";
	public static final String SPRITES = "sprites";
	public static final String DELIMITER = "\n";
	// Description, Status, Sprites
	public static final int NUM_SERIALIZATION_SECTIONS = 5;
	public static final int DESCRIPTION_SERIALIZATION_INDEX = 0;
	public static final int CONDITIONS_SERIALIZATION_INDEX = 1;
	public static final int BANK_SERIALIZATION_INDEX = 2;
	public static final int STATUS_SERIALIZATION_INDEX = 3;
	public static final int SPRITES_SERIALIZATION_INDEX = 4;
	//private GsonBuilder gsonBuilder;
	XStream xStream;

	public SerializationUtils() {
		//gsonBuilder = new GsonBuilder();
		xStream = new XStream();
	}

	/**
	 * Serialize all game data for the given level - description, status and
	 * collection of sprites, nest serialized data within level identifier
	 *
	 * @param gameDescription
	 *            (level-specific) description of game
	 * @param gameConditions
	 *            map of result to string identifier for a boolean function, e.g.
	 *            {"victory" : "allEnemiesDead", "defeat" : "allTowersDestroyed"},
	 *            etc.
	 * @param level
	 *            the level corresponding to the status, elements and description
	 *            data
	 * @param status
	 *            top-level game status from Heads-Up-Display, i.e. all game state
	 *            other than the Sprites
	 * @param levelSprites
	 *            the cache of generated sprites for a level
	 * @return serialization of map of level to serialized level data
	 */
	public String serializeGameData(String gameDescription, Map<String, String> gameConditions, Bank gameBank,
			int level, Map<String, Double> status, List<Sprite> levelSprites) {
		Map<String, String> serializedLevelData = new HashMap<>();
		serializedLevelData.put(Integer.toString(level),
				serializeLevelData(gameDescription, gameConditions, gameBank, status, levelSprites, level));
		//return gsonBuilder.create().toJson(serializedLevelData);
		return xStream.toXML(serializedLevelData);
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
		//return gsonBuilder.create().toJson(serializedLevelsDataMap);
		return xStream.toXML(serializedLevelsDataMap);
	}

	/**
	 * Serialize data for a specific level - description, status and collection of
	 * sprites
	 *
	 * @param gameDescription
	 *            (level-specific) description of game
	 * @param levelConditions
	 *            map of result to string identifier for a boolean function, e.g.
	 *            {"victory" : "allEnemiesDead", "defeat" : "allTowersDestroyed"},
	 *            etc.
	 * @param status
	 *            top-level game status from Heads-Up-Display, i.e. all game state
	 *            other than the Sprites
	 * @param levelSprites
	 *            the cache of generated sprites for a level
	 * @return serialization of level data
	 */
	public String serializeLevelData(String gameDescription, Map<String, String> levelConditions, Bank bank,
			Map<String, Double> status, List<Sprite> levelSprites, int level) {
		StringBuilder gameDataStringBuilder = new StringBuilder();
		gameDataStringBuilder.append(serializeGameDescription(gameDescription));
		gameDataStringBuilder.append(DELIMITER);
		gameDataStringBuilder.append(serializeConditions(levelConditions));
		gameDataStringBuilder.append(DELIMITER);
		gameDataStringBuilder.append(serializeBank(bank));
		gameDataStringBuilder.append(DELIMITER);
		gameDataStringBuilder.append(serializeStatus(status));
		gameDataStringBuilder.append(DELIMITER);
		gameDataStringBuilder.append(serializeSprites(levelSprites, level));
		return gameDataStringBuilder.toString();
		
	}

	// TODO - for all deserialization methods : take level as parameter

	/**
	 * Deserialize previously serialized game description into a string
	 *
	 * @param serializedGameData
	 *            string of serialized game data, both top-level game status and
	 *            elements
	 * @param level
	 *            the level whose description is to be deserialized
	 * @return string corresponding to game description
	 * @throws IllegalArgumentException
	 *             if serialization is ill-formatted
	 */
	public String deserializeGameDescription(String serializedGameData, int level) throws IllegalArgumentException {
		String[] serializedSections = retrieveSerializedSectionsForLevel(serializedGameData, level);
		return deserializeDescription(serializedSections[DESCRIPTION_SERIALIZATION_INDEX]);
	}

	/**
	 * Deserialize previously serialized game conditions into a string
	 * 
	 * @param serializedGameData
	 *            string of serialized game conditions
	 * @param level
	 *            the level whose description is to be deserialized
	 * @return map corresponding to game conditions
	 * @throws IllegalArgumentException
	 *             if serialization is ill-formatted
	 */
	public Map<String, String> deserializeGameConditions(String serializedGameData, int level)
			throws IllegalArgumentException {
		String[] serializedSections = retrieveSerializedSectionsForLevel(serializedGameData, level);
		return deserializeConditions(serializedSections[CONDITIONS_SERIALIZATION_INDEX]);
	}

	/**
	 * Deserialize previously serialized bank into a Bank object
	 * 
	 * @param serializedGameData
	 *            string of serialized game conditions
	 * @param level
	 *            the level whose description is to be deserialized
	 * @return Bank instance corresponding to serialized bank data
	 * @throws IllegalArgumentException
	 *             if serialization is ill-formatted
	 */
	public Bank deserializeGameBank(String serializedGameData, int level) throws IllegalArgumentException {
		String[] serializedSections = retrieveSerializedSectionsForLevel(serializedGameData, level);
		return deserializeBank(serializedSections[BANK_SERIALIZATION_INDEX]);
	}

	/**
	 * Deserialize previously serialized game data into a game status map
	 *
	 * @param serializedGameData
	 *            string of serialized game data, both top-level game status and
	 *            elements
	 * @param level
	 *            the level whose description is to be deserialized
	 * @return map corresponding to top-level status
	 * @throws IllegalArgumentException
	 *             if serialization is ill-formatted
	 */
	public Map<String, Double> deserializeGameStatus(String serializedGameData, int level)
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
	 * @param level
	 *            the level whose description is to be deserialized
	 * @return map of sprite name to list of sprites of that name / type
	 * @throws IllegalArgumentException
	 *             if serialization is ill-formatted
	 */
	public List<Sprite> deserializeGameSprites(String serializedGameData, int level) throws IllegalArgumentException {
		String[] serializedSections = retrieveSerializedSectionsForLevel(serializedGameData, level);
		return deserializeSprites(serializedSections[SPRITES_SERIALIZATION_INDEX]);
	}

	/**
	 * The number of levels that exist in this game currently, as set by the
	 * authoring env
	 * 
	 * @param serializedGameData
	 *            string of serialized game data, both top-level game status and
	 *            elements
	 * @return the number of levels that currently exist in this game
	 * @throws IllegalArgumentException
	 *             if serialization is ill-formatted
	 */
	@SuppressWarnings("unchecked")
	public int getNumLevelsFromSerializedGame(String serializedGameData) throws IllegalArgumentException {
		//Map<String, String> serializedLevelData = gsonBuilder.create().fromJson(serializedGameData, Map.class);
		Map<String, String> serializedLevelData = (Map<String, String>) xStream.fromXML(serializedGameData, Map.class);
		return serializedLevelData.keySet().size();
	}

	private String serializeGameDescription(String gameDescription) {
		Map<String, String> descriptionMap = new HashMap<>();
		descriptionMap.put(DESCRIPTION, gameDescription);
		//return gsonBuilder.create().toJson(descriptionMap);
		return xStream.toXML(descriptionMap);
	}

	private String serializeConditions(Map<String, String> gameConditions) {
		Map<String, Map<String, String>> conditionsMap = new HashMap<>();
		conditionsMap.put(CONDITIONS, gameConditions);
		//return gsonBuilder.create().toJson(conditionsMap);
		return xStream.toXML(conditionsMap);
	}

	private String serializeBank(Bank bank) {
		Map<String, Bank> bankMap = new HashMap<>();
		bankMap.put(BANK, bank);
		//return gsonBuilder.create().toJson(bankMap);
		return xStream.toXML(bankMap);
	}

	private String serializeStatus(Map<String, Double> status) {
		Map<String, Map<String, Double>> statusMap = new HashMap<>();
		statusMap.put(STATUS, status);
		//return gsonBuilder.create().toJson(status);
		return xStream.toXML(status);
	}

	// Collect multiple sprites into a top-level map
	private String serializeSprites(List<Sprite> levelSprites, int level) {
		Map<String, List<Sprite>> spritesMap = new HashMap<>();
		spritesMap.put(SPRITES, levelSprites);
		//return gsonBuilder.create().toJson(levelSprites);
		return xStream.toXML(levelSprites);
	}

	@SuppressWarnings("unchecked")
	private String deserializeDescription(String serializedDescription) {
		//Map<String, String> descriptionMap = gsonBuilder.create().fromJson(serializedDescription, Map.class);
		Map<String, String> descriptionMap = (Map<String, String>) xStream.fromXML(serializedDescription, Map.class);
		return descriptionMap.get(DESCRIPTION);
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> deserializeConditions(String serializedConditions) {
		//Map<String, Map<String, String>> conditionsMap = gsonBuilder.create().fromJson(serializedConditions, Map.class);
		Map<String, Map<String, String>> conditionsMap = (Map<String, Map<String, String>>) xStream.fromXML(serializedConditions, Map.class);
		return conditionsMap.get(CONDITIONS);
	}

	@SuppressWarnings("unchecked")
	private Bank deserializeBank(String serializedBank) {
		//Map<String, Bank> bankMap = gsonBuilder.create().fromJson(serializedBank, Map.class);
		Map<String, Bank> bankMap = (Map<String, Bank>) xStream.fromXML(serializedBank, Map.class);
		return bankMap.get(BANK);
	}

	@SuppressWarnings("unchecked")
	private Map<String, Double> deserializeStatus(String serializedStatus) {
		//Map<String, Map<String, Double>> statusMap = gsonBuilder.create().fromJson(serializedStatus, Map.class);
		Map<String, Map<String, Double>> statusMap = (Map<String, Map<String, Double>>) xStream.fromXML(serializedStatus, Map.class);
		return statusMap.get(STATUS);
	}

	// Return a map of sprite name to list of elements, which can be used by
	// ElementFactory to construct sprite objects
	private List<Sprite> deserializeSprites(String serializedSprites) {
		// TODO - fix this, it will eventually cause:
		// Exception in thread "main" java.lang.ClassCastException:
		// com.google.gson.internal.LinkedTreeMap cannot be cast to sprites.Sprite
		//Map<String, List<Sprite>> spritesMap = gsonBuilder.create().fromJson(serializedSprites, Map.class);
		@SuppressWarnings("unchecked")
		Map<String, List<Sprite>> spritesMap = (Map<String, List<Sprite>>) xStream.fromXML(serializedSprites, Map.class);
		return spritesMap.get(SPRITES);
	}

	@SuppressWarnings("unchecked")
	private String[] retrieveSerializedSectionsForLevel(String serializedGameData, int level)
			throws IllegalArgumentException {
		//Map<String, String> serializedLevelData = gsonBuilder.create().fromJson(serializedGameData, Map.class);
		Map<String, String> serializedLevelData = (Map<String, String>) xStream.fromXML(serializedGameData, Map.class);
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

		SpriteFactory factory = new SpriteFactory();

		Map<String, Object> towerMap = new HashMap<>();
		towerMap.put("collisionVisitable", new DamageDealingCollisionVisitable(1.0));
		towerMap.put("collisionVisitor", new ImmortalCollider(1));
		// TODO - don't serialize sprites; cache their properties and reconstruct using
		// spriteFactory
		// since the sprite serialization is causing StackOverflowError
		// how to handle coordinates though?

		/*
		 * Sprite testTower = factory.defineElement("testTower", towerMap); Sprite
		 * testTower2 = factory.defineElement("testTower2", towerMap); Sprite testTower3
		 * = factory.defineElement("testTower"); Sprite testSoldier =
		 * factory.defineElement("testSoldier", soldierMap); Sprite testSoldier2 =
		 * factory.defineElement("testSoldier2", soldierMap); Sprite testSoldier3 =
		 * factory.defineElement("testSoldier"); List<Sprite> levelSprites =
		 * Arrays.asList(testTower, testTower2, testTower3, testSoldier, testSoldier2,
		 * testSoldier3); String serializedGameData =
		 * tester.serializeGameData(testDescription, testLevel, testStatus,
		 * levelSprites); System.out.println("Serialized sprites: " +
		 * serializedGameData); System.out.println("Game Description: " +
		 * tester.deserializeGameDescription(serializedGameData, testLevel));
		 * Map<String, String> deserializedStatus =
		 * tester.deserializeGameStatus(serializedGameData, testLevel); List<Sprite>
		 * deserializedSprites = tester.deserializeGameSprites(serializedGameData,
		 * testLevel); for (String statusKey : deserializedStatus.keySet()) {
		 * System.out.println(statusKey + " : " + deserializedStatus.get(statusKey)); }
		 * for (Sprite sprite : deserializedSprites) { System.out.println(sprite); }
		 */
	}

}
