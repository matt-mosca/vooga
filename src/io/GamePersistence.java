package io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Class responsible for writing of serialized data to files / reading
 * serialized data from files
 * 
 * @author radithya
 *
 */
public class GamePersistence {

	public static final String EOF_DELIMITER = "\\Z";
	public static final String AUTHORING_GAMES_FOLDER = "authoring/";
	public static final String PLAY_GAMES_FOLDER = "play/";

	public GamePersistence() {
	}

	/**
	 * 
	 * @param fileName
	 *            chosen file name for saved authoring game state, create if file
	 *            does not exist, overwrite if file exists
	 * @param serializedRepresentation
	 *            of current game
	 */
	public void saveAuthoringGameState(String fileName, String serializedRepresentation) {
		saveGameState(AUTHORING_GAMES_FOLDER + fileName, serializedRepresentation);
	}

	/**
	 * 
	 * @param fileName
	 *            chosen file name for saved play game state, create if file does
	 *            not exist, overwrite if file exists
	 * @param serializedRepresentation
	 *            of current game
	 */
	public void savePlayGameState(String fileName, String serializedRepresentation) {
		saveGameState(PLAY_GAMES_FOLDER + fileName, serializedRepresentation);
	}

	// TODO - throw custom exception
	/**
	 * 
	 * @param fileName
	 *            file name for authored game to be loaded
	 */
	public String loadAuthoringGameState(String fileName) throws FileNotFoundException {
		return loadGameState(AUTHORING_GAMES_FOLDER + fileName);
	}

	// TODO - throw custom exception
	/**
	 * 
	 * @param fileName
	 *            file name for played game to be loaded
	 */
	public String loadPlayGameState(String fileName) throws FileNotFoundException {
		return loadGameState(PLAY_GAMES_FOLDER + fileName);
	}

	/**
	 * Retrieve a map of authored game names to their description
	 * 
	 * @return map of {game_name : game_description}
	 */
	public Map<String, String> getAuthoredGameSerializations() {
		Map<String, String> authoredGameSerializationMap = new HashMap<>();
		// iterate over file names in authored_games folder, serialize each
		File authoredGamesDirectory = new File(AUTHORING_GAMES_FOLDER);
		File[] authoredGames = authoredGamesDirectory.listFiles();
		if (authoredGames == null) {
			throw new IllegalStateException();
		}
		for (File authoredGame : authoredGames) {
			String authoredGameName = authoredGame.getName();
			// FileNotFoundException would not make sense here as we are iterating through
			// files found in directory, so safe to ignore
			try {
				authoredGameSerializationMap.put(authoredGameName, loadAuthoringGameState(authoredGameName));
			} catch (FileNotFoundException e) {
				continue;
			}
		}
		return authoredGameSerializationMap;
	}

	private void saveGameState(String fileName, String serializedRepresentation) {
		File gameStateFile = new File(fileName);
		Writer gameStateWriter;
		try {
			if (gameStateFile.exists()) {
				// TODO - Warning if file exists
				gameStateWriter = new FileWriter(gameStateFile, false);
			} else {
				gameStateFile.createNewFile();
				gameStateWriter = new FileWriter(gameStateFile);
			}
			gameStateWriter.write(serializedRepresentation);
		} catch (IOException e) {
			// TODO - throw custom exception
		}
	}

	private String loadGameState(String fileName) throws FileNotFoundException {
		File gameStateFile = new File(fileName);
		if (!gameStateFile.exists()) {
			throw new FileNotFoundException();
		}
		Scanner gameStateScanner = new Scanner(gameStateFile);
		gameStateScanner.useDelimiter(EOF_DELIMITER);
		if (!gameStateScanner.hasNext()) {
			gameStateScanner.close();
			throw new IllegalArgumentException(); // TODO - throw custom exception
		}
		String gameStateString = gameStateScanner.next();
		gameStateScanner.close();
		return gameStateString;
	}

}
