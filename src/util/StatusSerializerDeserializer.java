package util;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class StatusSerializerDeserializer {

	public StatusSerializerDeserializer() {
	}
		
	// TODO
	public String serializeStatus(Map<String, String> status) {
		StringBuilder bob = new StringBuilder();
		Gson gson = new GsonBuilder().create();
		System.out.println("Serializing status");
		gson.toJson(status, bob);
		String serializedStatus = bob.toString();
		System.out.println(serializedStatus);
		return serializedStatus;
	}
	
	public Map<String, String> deserializeStatus(String serializedStatus) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(serializedStatus, Map.class);
	}
	
	public static void main(String[] args) {
		StatusSerializerDeserializer tester = new StatusSerializerDeserializer();
		Map<String, String> testMap = new HashMap<>();
		testMap.put("a", "1");
		String serializedMap = tester.serializeStatus(testMap);
		testMap = tester.deserializeStatus(serializedMap);
		for (String key : testMap.keySet()) {
			System.out.print(key + " : " + testMap.get(key));
		}
	}
}
