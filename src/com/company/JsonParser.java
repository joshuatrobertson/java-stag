package com.company;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class JsonParser {

    private final String filename;

    public JsonParser(String filename) {
        this.filename = filename;
    }

    public void parse(List<Action> actions, List<String> triggersArray) {

        JSONParser parser = new JSONParser();

        JSONObject jObj = null;
        try {
            jObj = (JSONObject) parser.parse(new FileReader(filename));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        // Create an array of actions
        JSONArray array;
        if (jObj != null) {
            array = (JSONArray) jObj.get("actions");
        }
        else {
            throw new NullPointerException();
        }

        // Loop through the actions and create separate Action classes
        for (Object value : array) {
            Action action = new Action();
            JSONObject obj2 = (JSONObject) value;
            JSONArray triggers = (JSONArray) obj2.get("triggers");
            for (Object trigger : triggers) {
                action.addTrigger((String) trigger);
                // Add to a separate list for easy lookup
                triggersArray.add((String) trigger);
            }
            JSONArray subjects = (JSONArray) obj2.get("subjects");
            for (Object subject : subjects) {
                action.addSubject((String) subject);
            }
            JSONArray consumed = (JSONArray) obj2.get("consumed");
            for (Object o : consumed) {
                action.addConsumed((String) o);
            }
            JSONArray produced = (JSONArray) obj2.get("produced");
            for (Object o : produced) {
                action.addProduced((String) o);
            }
            action.addNarration((String) obj2.get("narration"));
            actions.add(action);
        }
    }
}
