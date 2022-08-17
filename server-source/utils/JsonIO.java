package utils;

import java.io.File;
import java.io.FileReader;

import json.JSONObject;
import json.JSONTokener;

public class JsonIO {
	public static JSONObject loadJsonObject(String path) {
		if(!path.endsWith(".json"))
			path = path.concat(".json");
		File file = new File("./" + path);
		JSONObject json = null;
		if(file.exists()) {
			try {
				JSONTokener jsonParser = new JSONTokener(new FileReader("./" + path));
				json = new JSONObject(jsonParser);
			} catch(Exception e) {
				System.err.println("Cannot parse ban list, error in json format !");
				e.printStackTrace();
			}
		}
		return json;
	}

	public static void createDefaultConfig() {
		
	}
}