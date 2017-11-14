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

	public GamePersistence() {
	}
	
	public void saveGameState(String fileName, String serializedRepresentation) {
		File gameStateFile = new File(fileName);
		Writer gameStateWriter;
		try {
			if (gameStateFile.exists()) {
				// TODO - Warning if file exists
				// Overwrite file if it exists
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

	public String loadGameState(String fileName) throws FileNotFoundException {
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
