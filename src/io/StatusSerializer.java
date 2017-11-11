package io;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class StatusSerializer {

	public StatusSerializer() {
		// TODO Auto-generated constructor stub
	}
	
	
	public static void main(String[] args) {
		Map<String, String> testMap = new HashMap<>();
		StringBuilder bob = new StringBuilder();
		testMap.put("a", "1");
		Gson gson = new GsonBuilder().create();
		gson.toJson(testMap, bob);
		System.out.println(bob.toString());
		testMap = gson.fromJson(bob.toString(), Map.class);
		for (String key : testMap.keySet()) {
			System.out.print(key + " : " + testMap.get(key));
		}
	}

}
