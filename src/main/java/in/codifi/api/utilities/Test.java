package in.codifi.api.utilities;

import org.json.JSONException;
import org.json.JSONTokener;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Test {

	public static void main(String[] args) throws JSONException {
		  // Example input (can be either an object or array)
        String jsonInput = "[{\"name\":\"Alice\",\"age\":25}]";
        // String jsonInput = "[{\"name\":\"Alice\"}, {\"name\":\"Bob\"}]";

        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(jsonInput);

            if (obj instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) obj;
                System.out.println("It's a JSONObject: " + jsonObject);
                // Handle JSONObject
            } else if (obj instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) obj;
                System.out.println("It's a JSONArray: " + jsonArray);
                // Handle JSONArray
            } else {
                System.out.println("Invalid JSON input");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
	}
}
