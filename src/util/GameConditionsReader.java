package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GameConditionsReader {

	private final String CONDITIONS_PROPERTIES_FILE = "resources/Conditions.properties";
	private final Properties CONDITIONS_PROPERTIES;
	private final String REGEX_TO_REPLACE = "\\s+";
	private final String REGEX_TO_REPLACE_WITH = "_";
	
	public GameConditionsReader() {
		CONDITIONS_PROPERTIES = new Properties();
		try {
			InputStream conditionsPropertiesStream = getClass().getClassLoader().getResourceAsStream(CONDITIONS_PROPERTIES_FILE);
			CONDITIONS_PROPERTIES.load(conditionsPropertiesStream);
		} catch (IOException e) {		
			System.out.println("Couldn't load");
		}
	}
	
	public String getMethodNameForCondition(String condition) {
		return CONDITIONS_PROPERTIES.getProperty(condition.replaceAll(REGEX_TO_REPLACE, REGEX_TO_REPLACE_WITH));
	} 

}
