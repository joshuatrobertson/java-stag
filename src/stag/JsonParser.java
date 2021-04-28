package stag;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class JsonParser {

    private final String filename;
    private JSONParser parser;
    private JSONObject jsonObject;

    public JsonParser(String filename) {
        this.filename = filename;
        parser = new JSONParser();
        jsonObject = null;
        try {
            jsonObject = (JSONObject) parser.parse(new FileReader(filename));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    // Parse the JSON file
    public void parseJson(List<Action> actions, List<String> triggersArray) {
        // Create an array of actions
        JSONArray array;
        if (jsonObject != null) {
            array = (JSONArray) jsonObject.get("actions");
        }
        else {
            throw new NullPointerException();
        }

        // Loop through the actions and create separate Action classes
        for (Object value : array) {
            var action = new Action();
            var jsonObject2 = (JSONObject) value;
            getActionItems(triggersArray, action, jsonObject2);
            action.addNarration((String) jsonObject2.get("narration"));
            actions.add(action);
        }
    }

    private void getActionItems(List<String> triggersArray, Action action, JSONObject obj2) {
        JSONArray triggers = (JSONArray) obj2.get("triggers");
        for (Object trigger : triggers) {
            action.addTrigger((String) trigger);
            // Add to a separate list for easy lookup
            triggersArray.add((String) trigger);
        }
        // Fetch the subjects and add them to the action
        JSONArray subjects = (JSONArray) obj2.get("subjects");
        for (Object subject : subjects) {
            action.addSubject((String) subject);
        }
        // Add the consumed items
        JSONArray consumed = (JSONArray) obj2.get("consumed");
        for (Object o : consumed) {
            action.addConsumed((String) o);
        }
        // Add the produced items
        JSONArray produced = (JSONArray) obj2.get("produced");
        for (Object o : produced) {
            action.addProduced((String) o);
        }
    }
}
